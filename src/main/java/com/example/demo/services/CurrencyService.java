package com.example.demo.services;

import com.example.demo.models.dtos.responses.AvailableCurrenciesResDTO;
import com.example.demo.models.dtos.responses.CurrenciesExchangeRatesResDTO;
import com.example.demo.nbp.NbpClient;
import com.example.demo.nbp.dtos.CurrenciesTable;
import com.example.demo.nbp.dtos.SingleCurrencyDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
        List<CurrenciesExchangeRatesResDTO> result = new ArrayList<>();
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
}
