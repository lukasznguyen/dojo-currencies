package com.example.demo.nbp;

import com.example.demo.nbp.dtos.CurrenciesTable;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NbpClient {

    @Value("${nbp.api.url}")
    private final String NBP_URL= "http://api.nbp.pl/api/exchangerates/tables/c";
    private final RestTemplate restTemplate;

    public CurrenciesTable getCurrencies() {
        log.info("Fetching data from NBP api");
        return restTemplate.getForObject(NBP_URL, CurrenciesTable[].class)[0];
    }

}
