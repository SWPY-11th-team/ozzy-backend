package com.example.ozzy.user.controller;

import com.example.ozzy.oauth2.user.Token;
import com.example.ozzy.oauth2.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final JwtTokenUtil jwtTokenUtil;

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
    public Token refreshAccessToken(@RequestBody Token token) {
        String refreshToken = token.getRefreshToken();

        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new IllegalArgumentException("Refresh token is required");
        }

        if (jwtTokenUtil.isTokenExpired(refreshToken)) {
            throw new IllegalArgumentException("Refresh token has expired. Please login again.");
        }

        return jwtTokenUtil.reGenerateToken(refreshToken);
    }

}
