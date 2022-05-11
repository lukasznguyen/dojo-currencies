package com.example.demo.configs;

import com.example.demo.models.dtos.responses.AvailableCurrenciesResDTO;
import com.example.demo.models.dtos.responses.CurrenciesExchangeRatesResDTO;
import com.example.demo.nbp.dtos.SingleCurrencyDetails;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ModelMapperTest {

    private ModelMapper underTest = ModelMapperTestConfig.modelMapper();

    @Test
    void mapperShouldMapSingleCurrencyDetailsToAvailableCurrenciesResDTO() {
        //given
        SingleCurrencyDetails currencyDetails = SingleCurrencyDetails.builder()
                .currency("dolar amerykański")
                .code("USD")
                .bid(BigDecimal.valueOf(4.4069))
                .ask(BigDecimal.valueOf(4.4959))
                .build();

        //when
        AvailableCurrenciesResDTO result = underTest.map(currencyDetails, AvailableCurrenciesResDTO.class);

        //then
        assertAll(
                () -> assertThat(result.getCurrencyName(), equalTo("dolar amerykański")),
                () -> assertThat(result.getCurrencyCode(), equalTo("USD"))
        );
    }

    @Test
    void mapperShouldMapSingleCurrencyDetailsToCurrenciesExchangeRatesResDTO() {
        //given
        SingleCurrencyDetails currencyDetails = SingleCurrencyDetails.builder()
                .currency("dolar amerykański")
                .code("USD")
                .bid(BigDecimal.valueOf(4.4069))
                .ask(BigDecimal.valueOf(4.4959))
                .build();

        //when
        CurrenciesExchangeRatesResDTO result = underTest.map(currencyDetails, CurrenciesExchangeRatesResDTO.class);

        //then
        assertAll(
                () -> assertThat(result.getCurrencyName(), equalTo("dolar amerykański")),
                () -> assertThat(result.getCurrencyCode(), equalTo("USD")),
                () -> assertThat(result.getPurchaseRate(), equalTo(BigDecimal.valueOf(4.4069))),
                () -> assertThat(result.getSellingRate(), equalTo(BigDecimal.valueOf(4.4959)))
        );
    }
}
