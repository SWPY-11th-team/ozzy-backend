package com.example.ozzy.common.exception.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ExceptionType {

    USER_NICK_NAME("닉네임을 입력하세요.", 1001),
    USER_TERMS_AGREED1("OZZY 서비스 이용 약관 동의가 필요합니다.", 1002),
    USER_TERMS_AGREED2("개인정보 수입 및 이용 동의가 필요합니다.", 1002),
    USER_TERMS_AGREED3("개인정보 처리 방침 동의가 필요합니다.", 1002),
    USER_TERMS_AGREED4("네이버클라우드 플랫폼 clova Studio 서비스 이용 약관 동의가 필요합니다.", 1002);

    private final String message;
    private final int code;

    ExceptionType(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
