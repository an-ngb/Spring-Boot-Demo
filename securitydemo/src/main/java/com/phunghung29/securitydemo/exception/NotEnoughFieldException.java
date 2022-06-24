package com.phunghung29.securitydemo.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotEnoughFieldException extends CommonException{
    public NotEnoughFieldException(String errorCode, String errorType, String message) {
        super(errorCode, errorType, message);
    }
    public NotEnoughFieldException(String errorCode, String message) {
        this.errorCode = "400-".concat(errorCode);
        this.errorType = "NOT ENOUGH FIELD";
        this.message = message;
    }
}
