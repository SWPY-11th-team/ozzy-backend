package com.example.ozzy.user.dto.response;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class ServiceTermsResponse {

    private int userSeq;
    private String agreed1;
    private String agreed2;
    private String agreed3;
    private String agreed4;
    private String createAt;

}
