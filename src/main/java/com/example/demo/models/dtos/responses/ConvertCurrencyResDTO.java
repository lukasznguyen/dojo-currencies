package com.example.demo.models.dtos.responses;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ConvertCurrencyResDTO {
    private BigDecimal amount;
    private String currencyCodeFrom;
    private String currencyCodeTo;
    private BigDecimal exchangeRate;
    private BigDecimal calculatedValue;
}
