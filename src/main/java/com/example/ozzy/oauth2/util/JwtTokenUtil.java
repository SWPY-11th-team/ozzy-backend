package com.example.ozzy.oauth2.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {

    // todo value application 이동 , !! HS512에 적합한 비밀 키 재 생성
    private static final String secretKey = "I4u+zERuz0fEHWx5qYN1guJ4gRUzIxdwyy+qHtM3V4LRO2WVtYVcnFw1E5fhtEvQfH8HMyhChCz3csQsdbZcXw==";
    private static final long accessTokenValidity = 1000 * 60 * 30; // 30분
    private static final long refreshTokenValidity = 1000 * 60 * 60 * 24 * 7; // 7일

    // Access Token 생성
    public String generateAccessToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, username, accessTokenValidity);
    }

    // Refresh Token 생성
    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, username, refreshTokenValidity);
    }

    // 토큰 생성 로직
    private String doGenerateToken(Map<String, Object> claims, String subject, long expirationTime) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

}
