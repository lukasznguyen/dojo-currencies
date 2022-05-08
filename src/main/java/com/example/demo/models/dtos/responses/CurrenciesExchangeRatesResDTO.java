package com.example.demo.models.dtos.responses;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrenciesExchangeRatesResDTO {
    private String currencyName;
    private String currencyCode;
    private BigDecimal purchaseRate;
    private BigDecimal sellingRate;
}
