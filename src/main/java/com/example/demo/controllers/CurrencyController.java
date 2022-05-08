package com.example.demo.controllers;

import com.example.demo.models.dtos.responses.AvailableCurrenciesResDTO;
import com.example.demo.models.dtos.responses.CurrenciesExchangeRatesResDTO;
import com.example.demo.services.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/currencies")
@RequiredArgsConstructor
@Slf4j
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<List<AvailableCurrenciesResDTO>> getAllAvailableCurrencies() {
        log.info("Started processing request - getAllAvailableCurrencies");
        List<AvailableCurrenciesResDTO> result = currencyService.getAllAvailableCurrencies();
        log.info("Finished processing request - getAllAvailableCurrencies");
        return ResponseEntity.ok(result);
    }

    @GetMapping("/exchange-rates")
    public ResponseEntity<List<CurrenciesExchangeRatesResDTO>> getActualExchangeRates(@RequestParam(required = false) List<String> currencies) {
        log.info("Started processing request - getActualExchangeRates");
        List<CurrenciesExchangeRatesResDTO> result = currencyService.getActualExchangeRates(currencies);
        log.info("Finished processing request - getActualExchangeRates");
        return ResponseEntity.ok(result);
    }
}
