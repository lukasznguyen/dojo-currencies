package com.example.demo.services;

import com.example.demo.models.dtos.responses.AvailableCurrenciesResDTO;
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
}
