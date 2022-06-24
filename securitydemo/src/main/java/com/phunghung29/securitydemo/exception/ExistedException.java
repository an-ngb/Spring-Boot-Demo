package com.phunghung29.securitydemo.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExistedException extends CommonException{
    public ExistedException(String errorCode, String errorType, String message) {
        super(errorCode, errorType, message);
    }
    public ExistedException(String errorCode, String message) {
        this.errorCode = "409-".concat(errorCode);
        this.errorType = "CONFLICT";
        this.message = message;
    }
}