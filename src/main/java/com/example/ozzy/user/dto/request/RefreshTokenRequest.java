package com.example.ozzy.user.dto.request;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class RefreshTokenRequest {

    private String token;
    private LocalDateTime expiredAt;
    private int userSeq;

    public RefreshTokenRequest() {}

    public RefreshTokenRequest(String token, LocalDateTime expiredAt, int userSeq) {
        this.token = token;
        this.expiredAt = expiredAt;
        this.userSeq = userSeq;
    }
}
