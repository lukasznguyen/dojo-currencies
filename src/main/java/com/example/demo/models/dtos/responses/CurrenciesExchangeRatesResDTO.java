package com.example.demo.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrenciesExchangeRatesResDTO {
    private String currencyName;
    private String currencyCode;
    private BigDecimal purchaseRate;
    private BigDecimal sellingRate;
}
