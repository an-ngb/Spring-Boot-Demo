package com.phunghung29.securitydemo.controller;

import com.phunghung29.securitydemo.dto.ResponeObject;
import com.phunghung29.securitydemo.exception.NotEnoughFieldException;
import com.phunghung29.securitydemo.exception.NotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponeObject> handleNotFoundExp(NotFoundException exception, WebRequest webRequest) {
        Map<String, String> body = new HashMap<>();
        body.put("error_code", exception.getErrorCode());
        body.put("error_type", exception.getErrorType());
        body.put("message", exception.getMessage());
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponeObject("Fail","", Instant.now(), body)
        );
    }
    @ExceptionHandler(NotEnoughFieldException.class)
    public ResponseEntity<ResponeObject> handleNotEnoughFieldExp(NotEnoughFieldException exception, WebRequest webRequest) {
        Map<String, String> body = new HashMap<>();
        body.put("error_code", exception.getErrorCode());
        body.put("error_type", exception.getErrorType());
        body.put("message", exception.getMessage());
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponeObject("Fail","", Instant.now(), body)
        );
    }
}
