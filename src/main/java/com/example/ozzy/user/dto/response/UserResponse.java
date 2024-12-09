package com.example.ozzy.user.dto.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserResponse {

    private int userSeq;
    private String nickName;
    private String provider;
    private String email;


}
