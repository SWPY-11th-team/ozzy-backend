package com.example.ozzy.common.exception.controller;

import com.example.ozzy.common.exception.domain.CommonException;
import com.example.ozzy.common.exception.domain.DefaultResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<DefaultResponse<Void>> exception(final CommonException e) {
        return new ResponseEntity<>(new DefaultResponse<>(e.getMessage(), e.getCode()), HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DefaultResponse<Void>> exception(final Exception e) {
        return new ResponseEntity<>(new DefaultResponse<>(e.getMessage(), -1), HttpStatus.OK);
    }

}
