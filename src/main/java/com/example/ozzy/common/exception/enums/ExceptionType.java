package com.example.ozzy.common.exception.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ExceptionType {

    USER_NICK_NAME("닉네임을 입력하세요.", 1001);

    private final String message;
    private final int code;

    ExceptionType(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
