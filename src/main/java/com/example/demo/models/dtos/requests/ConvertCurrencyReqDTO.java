package com.example.demo.models.dtos.requests;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConvertCurrencyReqDTO {
    private String currencyCodeFrom;
    private String currencyCodeTo;
    private BigDecimal amount;
}
