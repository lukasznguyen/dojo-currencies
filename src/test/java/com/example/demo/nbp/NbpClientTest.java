package com.example.demo.nbp;

import com.example.demo.nbp.dtos.CurrenciesTable;
import com.example.demo.nbp.dtos.SingleCurrencyDetails;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NbpClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NbpClient underTest;

    @Test
    void getCurrenciesShouldReturnCurrenciesTable() {
        //given
        CurrenciesTable[] curr = Collections.singletonList(prepareCurrenciesTable()).toArray(CurrenciesTable[]::new);
        given(restTemplate.getForObject("http://api.nbp.pl/api/exchangerates/tables/c", CurrenciesTable[].class)).willReturn(curr);

        //when
        CurrenciesTable result = underTest.getCurrencies();

        //then
        verify(restTemplate).getForObject(any(String.class), any());

        assertThat(result.getRates(), hasSize(3));

        assertAll("Testing currency names",
                () -> assertThat(result.getRates().get(0).getCurrency(), equalTo("dolar amerykański")),
                () -> assertThat(result.getRates().get(1).getCurrency(), equalTo("euro")),
                () -> assertThat(result.getRates().get(2).getCurrency(), equalTo("funt szterling"))
        );

        assertAll("Testing currency codes",
                () -> assertThat(result.getRates().get(0).getCode(), equalTo("USD")),
                () -> assertThat(result.getRates().get(1).getCode(), equalTo("EUR")),
                () -> assertThat(result.getRates().get(2).getCode(), equalTo("GBP"))
        );

        assertAll("Testing bid rates",
                () -> assertThat(result.getRates().get(0).getBid(), equalTo(BigDecimal.valueOf(4.4069))),
                () -> assertThat(result.getRates().get(1).getBid(), equalTo(BigDecimal.valueOf(4.6589))),
                () -> assertThat(result.getRates().get(2).getBid(), equalTo(BigDecimal.valueOf(5.4306)))
        );

        assertAll("Testing ask rates",
                () -> assertThat(result.getRates().get(0).getAsk(), equalTo(BigDecimal.valueOf(4.4959))),
                () -> assertThat(result.getRates().get(1).getAsk(), equalTo(BigDecimal.valueOf(4.7531))),
                () -> assertThat(result.getRates().get(2).getAsk(), equalTo(BigDecimal.valueOf(5.5404)))
        );
    }

    private CurrenciesTable prepareCurrenciesTable() {
        SingleCurrencyDetails usd = SingleCurrencyDetails.builder()
                .currency("dolar amerykański")
                .code("USD")
                .bid(BigDecimal.valueOf(4.4069))
                .ask(BigDecimal.valueOf(4.4959))
                .build();

        SingleCurrencyDetails eur = SingleCurrencyDetails.builder()
                .currency("euro")
                .code("EUR")
                .bid(BigDecimal.valueOf(4.6589))
                .ask(BigDecimal.valueOf(4.7531))
                .build();

        SingleCurrencyDetails gbp = SingleCurrencyDetails.builder()
                .currency("funt szterling")
                .code("GBP")
                .bid(BigDecimal.valueOf(5.4306))
                .ask(BigDecimal.valueOf(5.5404))
                .build();

        CurrenciesTable curr = CurrenciesTable.builder()
                .rates(List.of(usd, eur, gbp))
                .build();
        System.out.println(curr.getRates());
        return curr;
    }
}