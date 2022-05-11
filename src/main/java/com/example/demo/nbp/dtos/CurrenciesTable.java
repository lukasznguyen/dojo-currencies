package com.example.demo.nbp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrenciesTable {
    private String table;
    private String no;
    private String tradingDate;
    private String effectiveDate;
    private List<SingleCurrencyDetails> rates;
}
