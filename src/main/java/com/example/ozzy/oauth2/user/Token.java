package com.example.ozzy.oauth2.user;

import lombok.*;

@Getter
@ToString
public class Token {

    private String accessToken;
    private String refreshToken;

    public Token() {}

    public Token(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static Token addAccessToken(String token) {
        return new Token(token, null);
    }

    public static Token addRefreshToken(String token) {
        return new Token(null, token);
    }

}
