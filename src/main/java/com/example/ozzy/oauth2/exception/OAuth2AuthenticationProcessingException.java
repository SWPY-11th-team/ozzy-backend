package com.example.ozzy.oauth2.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;

@Slf4j
public class OAuth2AuthenticationProcessingException extends AuthenticationException {
    public OAuth2AuthenticationProcessingException(String msg) {
        super(msg);
        log.info("OAuth2AuthenticationProcessingException ==> {}", msg);
    }
}
