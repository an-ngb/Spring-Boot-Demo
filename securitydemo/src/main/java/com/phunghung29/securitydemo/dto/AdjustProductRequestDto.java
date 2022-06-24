package com.phunghung29.securitydemo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class AdjustProductRequestDto {
    String productName;
    Float productPrice;
    Integer productQuantity;
    Boolean removeProduct;
}
