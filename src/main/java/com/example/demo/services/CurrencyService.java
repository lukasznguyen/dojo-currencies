package com.example.demo.services;

import com.example.demo.exceptions.BadRequestException;
import com.example.demo.models.dtos.requests.ConvertCurrencyReqDTO;
import com.example.demo.models.dtos.responses.AvailableCurrenciesResDTO;
import com.example.demo.models.dtos.responses.ConvertCurrencyResDTO;
import com.example.demo.models.dtos.responses.CurrenciesExchangeRatesResDTO;
import com.example.demo.nbp.NbpClient;
import com.example.demo.nbp.dtos.CurrenciesTable;
import com.example.demo.nbp.dtos.SingleCurrencyDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CurrencyService {

    private final NbpClient nbpClient;
    private final ModelMapper mapper;
    private final String plnCurrencyCode = "PLN";

    public List<AvailableCurrenciesResDTO> getAllAvailableCurrencies() {
        CurrenciesTable currencies = nbpClient.getCurrencies();
        List<AvailableCurrenciesResDTO> result = new ArrayList<>();
        for (SingleCurrencyDetails currencyDetails : currencies.getRates()) {
            AvailableCurrenciesResDTO item = mapper.map(currencyDetails, AvailableCurrenciesResDTO.class);
            result.add(item);
        }
        return result;
    }

    public List<CurrenciesExchangeRatesResDTO> getActualExchangeRates(List<String> request) {
        List<CurrenciesExchangeRatesResDTO> result;
        CurrenciesTable currencies = nbpClient.getCurrencies();

        if (Optional.ofNullable(request).isPresent()) {
            List<String> preparedRequest = prepareRequest(request);
            result = currencies.getRates().stream()
                    .filter(currencyDetail -> preparedRequest.contains(currencyDetail.getCode()))
                    .map(currencyDetail -> mapper.map(currencyDetail, CurrenciesExchangeRatesResDTO.class))
                    .collect(Collectors.toList());
        } else {
            result = currencies.getRates().stream()
                    .map(currencyDetail -> mapper.map(currencyDetail, CurrenciesExchangeRatesResDTO.class))
                    .collect(Collectors.toList());
        }
        return result;
    }

    private List<String> prepareRequest(List<String> request) {
        List<String> result = new ArrayList<>();
        request.forEach(currencyCode -> result.add(currencyCode.toUpperCase()));
        return result;
    }

    public ConvertCurrencyResDTO convertCurrency(ConvertCurrencyReqDTO request) {
        validateRequest(request);
        CurrenciesTable currencies = nbpClient.getCurrencies();
        ConvertCurrencyResDTO result;
        if (request.getCurrencyCodeFrom().equalsIgnoreCase(plnCurrencyCode)) {
            result = calculateResultForPurchaseCase(request, currencies);
        } else {
            result = calculateResultForSellingRate(request, currencies);
        }
        return result;
    }

    private ConvertCurrencyResDTO calculateResultForPurchaseCase(ConvertCurrencyReqDTO request, CurrenciesTable currencies) {
        String currencyCode = request.getCurrencyCodeTo();
        Optional<BigDecimal> purchaseRate = currencies.getRates().stream()
                .filter(currencyDetails -> currencyDetails.getCode().equalsIgnoreCase(currencyCode))
                .map(SingleCurrencyDetails::getBid)
                .findFirst();

        if (purchaseRate.isEmpty()) {
            throw new BadRequestException("Currency code not found");
        }

        BigDecimal result = request.getAmount().divide(purchaseRate.get(), 2, RoundingMode.HALF_UP);

        return ConvertCurrencyResDTO.builder()
                .amount(request.getAmount())
                .currencyCodeFrom(request.getCurrencyCodeFrom().toUpperCase())
                .currencyCodeTo(request.getCurrencyCodeTo().toUpperCase())
                .exchangeRate(purchaseRate.get())
                .calculatedValue(result)
                .build();
    }

    private ConvertCurrencyResDTO calculateResultForSellingRate(ConvertCurrencyReqDTO request, CurrenciesTable currencies) {
        String currencyCode = request.getCurrencyCodeFrom();
        Optional<BigDecimal> purchaseRate = currencies.getRates().stream()
                .filter(currencyDetails -> currencyDetails.getCode().equalsIgnoreCase(currencyCode))
                .map(SingleCurrencyDetails::getAsk)
                .findFirst();

        if (purchaseRate.isEmpty()) {
            throw new BadRequestException("Currency code not found");
        }

        BigDecimal result = request.getAmount().multiply(purchaseRate.get()).setScale(2, RoundingMode.HALF_UP);

        return ConvertCurrencyResDTO.builder()
                .amount(request.getAmount())
                .currencyCodeFrom(request.getCurrencyCodeFrom().toUpperCase())
                .currencyCodeTo(request.getCurrencyCodeTo().toUpperCase())
                .exchangeRate(purchaseRate.get())
                .calculatedValue(result)
                .build();
    }

    private void validateRequest(ConvertCurrencyReqDTO request) {
        String currencyCodeFrom = request.getCurrencyCodeFrom().toUpperCase();
        String currencyCodeTo = request.getCurrencyCodeTo().toUpperCase();
        boolean isAmountNegativeValue = request.getAmount().compareTo(BigDecimal.ZERO) < 0;
        boolean isBothCurrenciesSame = currencyCodeFrom.equals(currencyCodeTo);
        boolean isCurrencyFromPLN = currencyCodeFrom.equals(plnCurrencyCode);
        boolean isCurrencyToPLN = currencyCodeTo.equals(plnCurrencyCode);

        if (isAmountNegativeValue) {
            throw new BadRequestException("Amount value cannot be negative");
        }

        if (isBothCurrenciesSame) {
            throw new BadRequestException("CurrencyFrom and currencyTo are equal");
        }

        if (!isCurrencyFromPLN && !isCurrencyToPLN) {
            throw new BadRequestException("CurrencyFrom or currencyTo should have PLN value");
        }
    }
}
