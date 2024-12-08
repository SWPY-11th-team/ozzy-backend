package com.example.ozzy.oauth2.util;

import com.example.ozzy.oauth2.service.OAuth2UserPrincipal;
import com.example.ozzy.oauth2.user.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class JwtTokenUtil {

    // todo value application 이동 , !! HS512에 적합한 비밀 키 재 생성
    private static final String secretKey = "I4u+zERuz0fEHWx5qYN1guJ4gRUzIxdwyy+qHtM3V4LRO2WVtYVcnFw1E5fhtEvQfH8HMyhChCz3csQsdbZcXw==";
    private static final long accessTokenValidity = 1000 * 10; // 10초
//    private static final long accessTokenValidity = 1000 * 60;  // 1분
//    private static final long accessTokenValidity = 1000 * 60 * 30; // 30분
    private static final long refreshTokenValidity = 1000 * 60 * 60 * 24 * 7; // 7일

    public Token generateToken(int seq, OAuth2UserPrincipal principal) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userSeq", seq);
        claims.put("roles","user");
        claims.put("email",principal.getUserInfo().getEmail());
        claims.put("provider", principal.getUserInfo().getProvider());

        String accessToken = doGenerateToken(claims, principal.getUserInfo().getEmail(), accessTokenValidity);
        String refreshToken = doGenerateToken(claims, principal.getUserInfo().getEmail(), refreshTokenValidity);

        return new Token(accessToken, refreshToken);
    }

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

    // refreshToken에서 만료 날짜를 가져오는 방법
    public String getExpirationDateFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String expirationDateStr = sdf.format(claims.getExpiration());

        return expirationDateStr;
    }
}
