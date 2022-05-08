package com.example.demo.nbp.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CurrenciesTable {
    private String table;
    private String no;
    private String tradingDate;
    private String effectiveDate;
    private List<SingleCurrencyDetails> rates;
}
