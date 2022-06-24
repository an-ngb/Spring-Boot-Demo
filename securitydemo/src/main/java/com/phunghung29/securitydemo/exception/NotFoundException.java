package com.phunghung29.securitydemo.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotFoundException extends CommonException{
    public NotFoundException(String errorCode, String errorType, String message) {
        super(errorCode, errorType, message);
    }
    public NotFoundException(String errorCode, String message) {
        this.errorCode = "404-".concat(errorCode);
        this.errorType = "NOT FOUND";
        this.message = message;
    }
}
