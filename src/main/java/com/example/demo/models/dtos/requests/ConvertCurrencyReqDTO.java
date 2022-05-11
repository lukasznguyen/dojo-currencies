package com.example.demo.models.dtos.requests;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ConvertCurrencyReqDTO {
    private String currencyCodeFrom;
    private String currencyCodeTo;
    private BigDecimal amount;
}
