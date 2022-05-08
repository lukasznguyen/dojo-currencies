package com.example.demo.nbp.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SingleCurrencyDetails {
    private String currency;
    private String code;
    private BigDecimal bid;
    private BigDecimal ask;
}
