package com.example.demo.nbp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SingleCurrencyDetails {
    private String currency;
    private String code;
    private BigDecimal bid;
    private BigDecimal ask;
}
