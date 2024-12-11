package com.example.ozzy.common.exception.domain;

import com.example.ozzy.common.exception.enums.ExceptionType;
import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {

    private final int code;

    public CommonException(String message, int code) {
        super(message);
        this.code = code;
    }

    public CommonException(final ExceptionType loginExceptionType) {
        super(loginExceptionType.getMessage());
        this.code = loginExceptionType.getCode();
    }

    @Override
    public String toString() {
        return "LoginException{" +
                "message='" + super.getMessage() + '\'' +
                ", code=" + code +
                '}';
    }
}
