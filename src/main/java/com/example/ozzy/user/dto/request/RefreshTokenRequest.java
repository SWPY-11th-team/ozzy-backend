package com.example.ozzy.user.dto.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RefreshTokenRequest {

    private String token;
    private String expiredAt;
    private int userSeq;

    public RefreshTokenRequest() {}

    public RefreshTokenRequest(String token, String expiredAt, int userSeq) {
        this.token = token;
        this.expiredAt = expiredAt;
        this.userSeq = userSeq;
    }
}
