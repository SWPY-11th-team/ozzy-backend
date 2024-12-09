package com.example.ozzy.oauth2.handler;

import com.example.ozzy.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.example.ozzy.oauth2.service.OAuth2UserPrincipal;
import com.example.ozzy.oauth2.user.OAuth2Provider;
import com.example.ozzy.oauth2.user.OAuth2UserUnlinkManager;
import com.example.ozzy.oauth2.user.Token;
import com.example.ozzy.oauth2.util.CookieUtils;
import com.example.ozzy.oauth2.util.JwtTokenUtil;
import com.example.ozzy.user.dto.request.RefreshTokenRequest;
import com.example.ozzy.user.dto.request.UserRequest;
import com.example.ozzy.user.dto.response.UserResponse;
import com.example.ozzy.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.ozzy.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.MODE_PARAM_COOKIE_NAME;
import static com.example.ozzy.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

/**
 * OAuth2 인증성공 handler
 * */
@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final OAuth2UserUnlinkManager oAuth2UserUnlinkManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String targetUrl;

        targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {

        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        final String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        final String mode = CookieUtils.getCookie(request, MODE_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse("");

        final OAuth2UserPrincipal principal = getOAuth2UserPrincipal(authentication);

        if (principal == null) {
            return UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("error", "Login failed")
                    .build().toUriString();
        }

        if ("login".equalsIgnoreCase(mode)) {
            log.info("email={}, name={}, provider={}, nickname={}, accessToken={}",
                    principal.getUserInfo().getEmail(),
                    principal.getUserInfo().getProvider(),
                    principal.getUserInfo().getName(),
                    principal.getUserInfo().getNickname(),
                    principal.getUserInfo().getAccessToken());

            // 기존 사용자 처리
            int userId = userService.findUser(principal);
            Token token;

            if (userId != 0) {  // 기존 사용자
                log.info("기존 사용자");

                // 기존 사용자의 정보를 바탕으로 토큰 발급
                token = jwtTokenUtil.generateToken(userId, principal);

                // RefreshToken 저장
                LocalDateTime expirationDateFromToken = jwtTokenUtil.getExpirationDateFromToken(token.getRefreshToken());
                RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(token.getRefreshToken(), expirationDateFromToken, userId);
                userService.insertRefreshToken(refreshTokenRequest);  // 기존 사용자에게도 Refresh Token 저장

                log.info("accessToken : {}", token.getAccessToken());
                log.info("refreshToken : {}", token.getRefreshToken());

                // 쿠키에 토큰 저장
                CookieUtils.createCookieToken(token, response);

                return UriComponentsBuilder.fromUriString(targetUrl)
                        .queryParam("accessToken", token.getAccessToken())
                        .queryParam("refreshToken", token.getRefreshToken())
                        .build().toUriString();
            }

            // 신규 사용자 처리
            int seq = userService.getNextUserSeq();
            UserRequest userRequest = UserRequest.add(seq, principal);

            // 토큰 발급
            token = jwtTokenUtil.generateToken(seq, principal);

            log.info("new accessToken : {}", token.getAccessToken());
            log.info("new refreshToken : {}", token.getRefreshToken());
            log.info("new provider : {}", principal.getUserInfo().getProvider());

            // 신규 사용자 저장 및 Refresh Token 저장
            userService.insertUser(userRequest, token);

            // 쿠키에 토큰 저장
            CookieUtils.createCookieToken(token, response);

            return UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("accessToken", token.getAccessToken())
                    .queryParam("refreshToken", token.getRefreshToken())
                    .build().toUriString();
        }
        else if ("logout".equalsIgnoreCase(mode)) {

            final String accessToken = principal.getUserInfo().getAccessToken();
            OAuth2Provider provider = principal.getUserInfo().getProvider();

            oAuth2UserUnlinkManager.unlink(provider, accessToken);

            return UriComponentsBuilder.fromUriString(targetUrl)
                    .build().toUriString();
        }

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", "Login failed")
                .build().toUriString();
    }

    private OAuth2UserPrincipal getOAuth2UserPrincipal(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2UserPrincipal) {
            return (OAuth2UserPrincipal) principal;
        }
        return null;
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
