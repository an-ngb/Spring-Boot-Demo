package com.phunghung29.securitydemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponeObject {
    private String status;
    private String message;
    private Object data;
}
