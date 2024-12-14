package com.example.ozzy.user.controller;

import com.example.ozzy.common.exception.domain.CommonException;
import com.example.ozzy.common.exception.domain.DefaultResponse;
import com.example.ozzy.oauth2.user.Token;
import com.example.ozzy.oauth2.util.CookieUtils;
import com.example.ozzy.oauth2.util.JwtTokenUtil;
import com.example.ozzy.user.dto.request.ServiceTermsRequest;
import com.example.ozzy.user.dto.request.UserRequest;
import com.example.ozzy.user.dto.response.ServiceTermsResponse;
import com.example.ozzy.user.dto.response.UserFromDate;
import com.example.ozzy.user.dto.response.UserResponse;
import com.example.ozzy.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    /**
     * 토큰 만료 체크
     * */
    @PostMapping("/is-token-expired/{name}")
    public boolean isTokenExpired(@RequestBody Token token, @PathVariable String name) {
        if (name.equals("accessToken")) {
            return jwtTokenUtil.isTokenExpired(token.getAccessToken());
        }
        return jwtTokenUtil.isTokenExpired(token.getRefreshToken());
    }

    /**
     *  refreshToken 으로 accessToken 재발급
     * */
    @PostMapping("/refresh-token")
    public ResponseEntity<Token> refreshAccessToken(@RequestBody Token token, HttpServletResponse response) {
        String refreshToken = token.getRefreshToken();

        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new IllegalArgumentException("Refresh token is required");
        }

        if (jwtTokenUtil.isTokenExpired(refreshToken)) {
            throw new IllegalArgumentException("Refresh token has expired. Please login again.");
        }

        Token reGenerateToken = jwtTokenUtil.reGenerateToken(refreshToken);
        CookieUtils.createCookieToken(reGenerateToken, response);

        return new ResponseEntity<>(reGenerateToken, HttpStatus.OK);
    }

    /**
     * 로그아읏
     * */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        CookieUtils.deleteUserCookie(response);
        return ResponseEntity.ok("logout");
    }

    /**
     * 유저 정보 받아오기
     * */
    @PostMapping("/info")
    public ResponseEntity<DefaultResponse<UserResponse>> selectUserInfo() {
        try {
            UserResponse info = userService.selectUserInfo();
            return ResponseEntity.ok(new DefaultResponse<>(info));
        } catch (CommonException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new DefaultResponse<>(e.getMessage(), e.getCode(), null));
        }
    }

    /**
     * 유저 정보 수정
     * */
    @PostMapping("/update")
    public ResponseEntity<DefaultResponse<Void>> updateUser(@RequestBody UserRequest request) {
        try {
            userService.updateUserName(request);
        } catch (CommonException e) {
            return new ResponseEntity<>(new DefaultResponse<>(e.getMessage(), e.getCode()), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new DefaultResponse<>(), HttpStatus.OK);
    }

    // 약관 등록
    @PostMapping("/insert/terms")
    public ResponseEntity<DefaultResponse<Void>> insertServiceTerms(@RequestBody ServiceTermsRequest serviceTermsRequest) {
        try {
            userService.insertServiceTerms(serviceTermsRequest);
        } catch (CommonException e) {
            return new ResponseEntity<>(new DefaultResponse<>(e.getMessage(), e.getCode()), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new DefaultResponse<>(), HttpStatus.OK);
    }

    // 약관 가입 정보
    @GetMapping("/get/terms")
    public ResponseEntity<DefaultResponse<ServiceTermsResponse>> getTerms() {
        try {
            ServiceTermsResponse terms = userService.getTerms();
            return new ResponseEntity<>(new DefaultResponse<>(terms), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<>(new DefaultResponse<>(e.getMessage(), e.getCode()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new DefaultResponse<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 마이페이지 상단
    @GetMapping("/date")
    public ResponseEntity<DefaultResponse<UserFromDate>> getUserFromDate() {
        try {
            UserFromDate userFromDate = userService.getUserFromDate();
            return new ResponseEntity<>(new DefaultResponse<>(userFromDate), HttpStatus.OK);
        } catch (CommonException e) {
            return new ResponseEntity<>(new DefaultResponse<>(e.getMessage(), e.getCode()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new DefaultResponse<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
