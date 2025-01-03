package com.example.ozzy.oauth2.util;

import com.example.ozzy.oauth2.service.OAuth2UserPrincipal;
import com.example.ozzy.oauth2.user.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtTokenUtil {

    private static final long accessTokenValidity = 1000 * 60 * 60 * 24 * 7;  // 7일
    private static final long refreshTokenValidity = 1000 * 60 * 60 * 24 * 7; // 7일

    @Value("${jwt.secret}")
    private String secretKey;

    // 로그인 발급
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

    // 재발급
    public Token reGenerateToken(String token) {
        Claims user = getClaims(token);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userSeq", user.get("userSeq"));
        claims.put("roles","user");
        claims.put("email",user.get("email"));
        claims.put("provider", user.get("provider"));

        String accessToken = doGenerateToken(claims, user.get("email").toString(), accessTokenValidity);

        return new Token(accessToken, token);
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
    public LocalDateTime getExpirationDateFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        log.info("expired-Time: " + claims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toString());

        return claims.getExpiration().toInstant()
                .atZone(ZoneId.systemDefault()) // 시스템 기본 시간대
                .toLocalDateTime();
    }

    /**
     * 토큰이 만료되었는지 확인
     * true : 만료
     * false : 토큰 살아있음
     * */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaims(token);
            Date expirationDate = claims.getExpiration();
            return expirationDate.before(new Date());
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    // 토큰에서 사용자 정보 추출
    private Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid or expired token", e);
        }
    }

    public int getUserId(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .get("userSeq")
                    .hashCode();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid or expired token", e);
        }
    }
}
