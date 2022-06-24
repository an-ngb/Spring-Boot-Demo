package com.phunghung29.securitydemo.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommonException extends RuntimeException{
    String errorCode;
    String errorType;
    String message;

    public CommonException(String errorCode, String errorType, String message) {
        this.errorCode = errorCode;
        this.errorType = errorType;
        this.message = message;
    }
}
