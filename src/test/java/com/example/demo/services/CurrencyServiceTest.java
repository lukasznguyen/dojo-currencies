package com.example.demo.services;

import com.example.demo.exceptions.BadRequestException;
import com.example.demo.models.dtos.requests.ConvertCurrencyReqDTO;
import com.example.demo.models.dtos.responses.AvailableCurrenciesResDTO;
import com.example.demo.models.dtos.responses.ConvertCurrencyResDTO;
import com.example.demo.models.dtos.responses.CurrenciesExchangeRatesResDTO;
import com.example.demo.models.enums.ActionType;
import com.example.demo.nbp.NbpClient;
import com.example.demo.nbp.dtos.CurrenciesTable;
import com.example.demo.nbp.dtos.SingleCurrencyDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumingThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    private NbpClient nbpClient;

    @Mock
    private ModelMapper mapper;

    @Mock
    private LoggingService loggingService;

    @InjectMocks
    private CurrencyService underTest;

    @Test
    void getAllAvailableCurrenciesShouldReturnListAvailableCurrenciesDTO() {
        //given
        AvailableCurrenciesResDTO mapperRes1 = new AvailableCurrenciesResDTO("dolar amerykański", "USD");
        AvailableCurrenciesResDTO mapperRes2 = new AvailableCurrenciesResDTO("euro", "EUR");
        AvailableCurrenciesResDTO mapperRes3 = new AvailableCurrenciesResDTO("funt szterling", "GBP");

        willDoNothing().given(loggingService).createDatabaseLog(ArgumentMatchers.any(ActionType.class), isNull(), isNull());
        given(nbpClient.getCurrencies()).willReturn(prepareCurrenciesTable());
        given(mapper.map(any(), any())).willReturn(mapperRes1, mapperRes2, mapperRes3);

        //when
        List<AvailableCurrenciesResDTO> result = underTest.getAllAvailableCurrencies();

        //then
        assertThat(result, hasSize(3));

        assertAll("Testing currency names",
                () -> assertThat(result.get(0).getCurrencyName(), equalTo("dolar amerykański")),
                () -> assertThat(result.get(1).getCurrencyName(), equalTo("euro")),
                () -> assertThat(result.get(2).getCurrencyName(), equalTo("funt szterling"))
        );

        assertAll("Testing currency codes",
                () -> assertThat(result.get(0).getCurrencyCode(), equalTo("USD")),
                () -> assertThat(result.get(1).getCurrencyCode(), equalTo("EUR")),
                () -> assertThat(result.get(2).getCurrencyCode(), equalTo("GBP"))
        );

    }

    @Test
    void getActualExchangeRatesShouldReturnAllExchangeRatesWhenRequestIsNull() {
        //given
        CurrenciesExchangeRatesResDTO mapperRes1 = new CurrenciesExchangeRatesResDTO(
                "dolar amerykański",
                "USD",
                BigDecimal.valueOf(4.4069),
                BigDecimal.valueOf(4.4959));

        CurrenciesExchangeRatesResDTO mapperRes2 = new CurrenciesExchangeRatesResDTO(
                "euro",
                "EUR",
                BigDecimal.valueOf(4.6589),
                BigDecimal.valueOf(4.7531));

        CurrenciesExchangeRatesResDTO mapperRes3 = new CurrenciesExchangeRatesResDTO(
                "funt szterling",
                "GBP",
                BigDecimal.valueOf(5.4306),
                BigDecimal.valueOf(5.5404));

        given(nbpClient.getCurrencies()).willReturn(prepareCurrenciesTable());
        given(mapper.map(any(), any())).willReturn(mapperRes1, mapperRes2, mapperRes3);

        //when
        List<CurrenciesExchangeRatesResDTO> result = underTest.getActualExchangeRates(null);

        //then
        assertThat(result, hasSize(3));

        assertAll("Testing currency names",
                () -> assertThat(result.get(0).getCurrencyName(), equalTo("dolar amerykański")),
                () -> assertThat(result.get(1).getCurrencyName(), equalTo("euro")),
                () -> assertThat(result.get(2).getCurrencyName(), equalTo("funt szterling"))
        );

        assertAll("Testing currency codes",
                () -> assertThat(result.get(0).getCurrencyCode(), equalTo("USD")),
                () -> assertThat(result.get(1).getCurrencyCode(), equalTo("EUR")),
                () -> assertThat(result.get(2).getCurrencyCode(), equalTo("GBP"))
        );

        assertAll("Testing purchase rates",
                () -> assertThat(result.get(0).getPurchaseRate(), equalTo(BigDecimal.valueOf(4.4069))),
                () -> assertThat(result.get(1).getPurchaseRate(), equalTo(BigDecimal.valueOf(4.6589))),
                () -> assertThat(result.get(2).getPurchaseRate(), equalTo(BigDecimal.valueOf(5.4306)))
        );

        assertAll("Testing selling rates",
                () -> assertThat(result.get(0).getSellingRate(), equalTo(BigDecimal.valueOf(4.4959))),
                () -> assertThat(result.get(1).getSellingRate(), equalTo(BigDecimal.valueOf(4.7531))),
                () -> assertThat(result.get(2).getSellingRate(), equalTo(BigDecimal.valueOf(5.5404)))
        );
    }

    @Test
    void getActualExchangeRatesShouldReturnSpecificExchangeRatesBasedOnRequest() {
        //given
        List<String> request = List.of("USD", "euR");
        CurrenciesExchangeRatesResDTO mapperRes1 = new CurrenciesExchangeRatesResDTO(
                "dolar amerykański",
                "USD",
                BigDecimal.valueOf(4.4069),
                BigDecimal.valueOf(4.4959));

        CurrenciesExchangeRatesResDTO mapperRes2 = new CurrenciesExchangeRatesResDTO(
                "euro",
                "EUR",
                BigDecimal.valueOf(4.6589),
                BigDecimal.valueOf(4.7531));

        given(nbpClient.getCurrencies()).willReturn(prepareCurrenciesTable());
        given(mapper.map(any(), any())).willReturn(mapperRes1, mapperRes2);

        //when
        List<CurrenciesExchangeRatesResDTO> result = underTest.getActualExchangeRates(request);

        //then
        assertThat(result, hasSize(2));

        assertAll("Testing currency names",
                () -> assertThat(result.get(0).getCurrencyName(), equalTo("dolar amerykański")),
                () -> assertThat(result.get(1).getCurrencyName(), equalTo("euro"))
        );

        assertAll("Testing currency codes",
                () -> assertThat(result.get(0).getCurrencyCode(), equalTo("USD")),
                () -> assertThat(result.get(1).getCurrencyCode(), equalTo("EUR"))
        );

        assertAll("Testing purchase rates",
                () -> assertThat(result.get(0).getPurchaseRate(), equalTo(BigDecimal.valueOf(4.4069))),
                () -> assertThat(result.get(1).getPurchaseRate(), equalTo(BigDecimal.valueOf(4.6589)))
        );

        assertAll("Testing selling rates",
                () -> assertThat(result.get(0).getSellingRate(), equalTo(BigDecimal.valueOf(4.4959))),
                () -> assertThat(result.get(1).getSellingRate(), equalTo(BigDecimal.valueOf(4.7531)))
        );
    }

    @Test
    void getActualExchangeRatesShouldReturnEmptyListWhenRequestIsInvalid() {
        //given
        List<String> request = List.of("test", "arsss");

        given(nbpClient.getCurrencies()).willReturn(prepareCurrenciesTable());

        //when
        List<CurrenciesExchangeRatesResDTO> result = underTest.getActualExchangeRates(request);

        //then
        assertThat(result, is(emptyCollectionOf(CurrenciesExchangeRatesResDTO.class)));
    }

    @Test
    void prepareRequestShouldReturnUpperCasedListOfStringsWhenRequestIsListString() {
        //given
        List<String> request = List.of("Ala", "mA", "kota");

        //when
        List<String> result = underTest.prepareRequest(request);

        //then
        assertAll(
                () -> assertThat(result, hasSize(3)),
                () -> assertThat(result.get(0), equalTo("ALA")),
                () -> assertThat(result.get(1), equalTo("MA")),
                () -> assertThat(result.get(2), equalTo("KOTA"))
        );
    }

    @Test
    void prepareRequestShouldThrowNullPointerExceptionWhenRequestIsNull() {
        //given
        List<String> request = null;

        //when
        //then
        assertThrows(NullPointerException.class, () -> underTest.prepareRequest(request));
    }

    @Test
    void validateRequestShouldThrowBadRequestExcWhenAmountValueIsNegative() {
        //given
        ConvertCurrencyReqDTO request = ConvertCurrencyReqDTO.builder()
                .currencyCodeFrom("PLN")
                .currencyCodeTo("USD")
                .amount(BigDecimal.valueOf(-1))
                .build();

        //when
        //then
        assertThrows(BadRequestException.class, () -> underTest.validateRequest(request));
    }

    @Test
    void validateRequestShouldThrowBadRequestExcWhenBothCurrenciesAreSame() {
        //given
        ConvertCurrencyReqDTO request = ConvertCurrencyReqDTO.builder()
                .currencyCodeFrom("PLN")
                .currencyCodeTo("PLN")
                .amount(BigDecimal.ONE)
                .build();

        //when
        //then
        assertThrows(BadRequestException.class, () -> underTest.validateRequest(request));
    }

    @Test
    void validateRequestShouldThrowBadRequestExcWhenNoneOfTheCurrenciesArePLN() {
        //given
        ConvertCurrencyReqDTO request = ConvertCurrencyReqDTO.builder()
                .currencyCodeFrom("EUR")
                .currencyCodeTo("USD")
                .amount(BigDecimal.ONE)
                .build();

        //when
        //then
        assertThrows(BadRequestException.class, () -> underTest.validateRequest(request));
    }

    @Test
    void calculateResultForPurchaseCaseShouldReturnCorrectRespWhenReqIsCorrect() {
        //given
        ConvertCurrencyReqDTO request = ConvertCurrencyReqDTO.builder()
                .currencyCodeFrom("PLN")
                .currencyCodeTo("USD")
                .amount(BigDecimal.valueOf(10))
                .build();

        //when
        ConvertCurrencyResDTO result = underTest.calculateResultForPurchaseCase(request, prepareCurrenciesTable());

        //then
        assertAll(
                () -> assertThat(result.getCurrencyCodeFrom(), equalTo("PLN")),
                () -> assertThat(result.getCurrencyCodeTo(), equalTo("USD")),
                () -> assertThat(result.getExchangeRate(), comparesEqualTo(BigDecimal.valueOf(4.4069))),
                () -> assertThat(result.getCalculatedValue(), comparesEqualTo(BigDecimal.valueOf(2.27)))
        );
    }

    @Test
    void calculateResultForPurchaseCaseShouldThrowBadRequestExWhenCurrencyCodeToIsInvalid() {
        //given
        ConvertCurrencyReqDTO request = ConvertCurrencyReqDTO.builder()
                .currencyCodeFrom("PLN")
                .currencyCodeTo("AAAA")
                .amount(BigDecimal.valueOf(10))
                .build();

        //when
        //then
        assertThrows(BadRequestException.class, () -> underTest.calculateResultForPurchaseCase(request, prepareCurrenciesTable()));
    }

    @Test
    void calculateResultForSellingRateShouldReturnCorrectRespWhenReqIsCorrect() {
        //given
        ConvertCurrencyReqDTO request = ConvertCurrencyReqDTO.builder()
                .currencyCodeFrom("USD")
                .currencyCodeTo("PLN")
                .amount(BigDecimal.valueOf(10))
                .build();

        //when
        ConvertCurrencyResDTO result = underTest.calculateResultForSellingRate(request, prepareCurrenciesTable());

        //then
        assertAll(
                () -> assertThat(result.getCurrencyCodeFrom(), equalTo("USD")),
                () -> assertThat(result.getCurrencyCodeTo(), equalTo("PLN")),
                () -> assertThat(result.getExchangeRate(), comparesEqualTo(BigDecimal.valueOf(4.4959))),
                () -> assertThat(result.getCalculatedValue(), comparesEqualTo(BigDecimal.valueOf(44.96)))
        );
    }

    @Test
    void calculateResultForSellingRateShouldThrowBadRequestExWhenCurrencyCodeFromIsInvalid() {
        //given
        ConvertCurrencyReqDTO request = ConvertCurrencyReqDTO.builder()
                .currencyCodeFrom("AAAA")
                .currencyCodeTo("PLN")
                .amount(BigDecimal.valueOf(10))
                .build();

        //when
        //then
        assertThrows(BadRequestException.class, () -> underTest.calculateResultForSellingRate(request, prepareCurrenciesTable()));
    }

    @Test
    void convertCurrencyShouldReturnCorrectRespWhenCurrencyFromIsPLN() {
        //given
        ConvertCurrencyReqDTO request = ConvertCurrencyReqDTO.builder()
                .currencyCodeFrom("PLN")
                .currencyCodeTo("USD")
                .amount(BigDecimal.valueOf(10))
                .build();

        given(nbpClient.getCurrencies()).willReturn(prepareCurrenciesTable());

        //when
        ConvertCurrencyResDTO result = underTest.convertCurrency(request);

        //then
        assertAll(
                () -> assertThat(result.getCurrencyCodeFrom(), equalTo("PLN")),
                () -> assertThat(result.getCurrencyCodeTo(), equalTo("USD")),
                () -> assertThat(result.getExchangeRate(), comparesEqualTo(BigDecimal.valueOf(4.4069))),
                () -> assertThat(result.getCalculatedValue(), comparesEqualTo(BigDecimal.valueOf(2.27)))
        );
    }

    @Test
    void convertCurrencyShouldReturnCorrectRespWhenCurrencyToIsPLN() {
        //given
        ConvertCurrencyReqDTO request = ConvertCurrencyReqDTO.builder()
                .currencyCodeFrom("USD")
                .currencyCodeTo("PLN")
                .amount(BigDecimal.valueOf(10))
                .build();

        given(nbpClient.getCurrencies()).willReturn(prepareCurrenciesTable());

        //when
        ConvertCurrencyResDTO result = underTest.convertCurrency(request);

        //then
        assertAll(
                () -> assertThat(result.getCurrencyCodeFrom(), equalTo("USD")),
                () -> assertThat(result.getCurrencyCodeTo(), equalTo("PLN")),
                () -> assertThat(result.getExchangeRate(), comparesEqualTo(BigDecimal.valueOf(4.4959))),
                () -> assertThat(result.getCalculatedValue(), comparesEqualTo(BigDecimal.valueOf(44.96)))
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

        return curr;
    }
}