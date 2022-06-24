package com.phunghung29.securitydemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponeObject {
    private String status;
    private String message;
    private Instant time;
    private Object data;
}
