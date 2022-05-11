package com.example.demo.configs;

import com.example.demo.models.dtos.responses.AvailableCurrenciesResDTO;
import com.example.demo.models.dtos.responses.CurrenciesExchangeRatesResDTO;
import com.example.demo.nbp.dtos.SingleCurrencyDetails;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

public class ModelMapperTestConfig {

    public static ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        map_SingleCurrencyDetails_to_AvailableCurrenciesResDTO(mapper);
        map_SingleCurrencyDetails_to_CurrenciesExchangeRatesResDTO(mapper);
        mapper.validate();
        return mapper;
    }

    private static void map_SingleCurrencyDetails_to_AvailableCurrenciesResDTO(ModelMapper mapper) {
        TypeMap<SingleCurrencyDetails, AvailableCurrenciesResDTO> config;
        config = mapper.createTypeMap(SingleCurrencyDetails.class, AvailableCurrenciesResDTO.class);
        config.addMapping(SingleCurrencyDetails::getCurrency, AvailableCurrenciesResDTO::setCurrencyName);
    }

    private static void map_SingleCurrencyDetails_to_CurrenciesExchangeRatesResDTO(ModelMapper mapper) {
        TypeMap<SingleCurrencyDetails, CurrenciesExchangeRatesResDTO> config;
        config = mapper.createTypeMap(SingleCurrencyDetails.class, CurrenciesExchangeRatesResDTO.class);
        config.addMapping(SingleCurrencyDetails::getCurrency, CurrenciesExchangeRatesResDTO::setCurrencyName);
        config.addMapping(SingleCurrencyDetails::getBid, CurrenciesExchangeRatesResDTO::setPurchaseRate);
        config.addMapping(SingleCurrencyDetails::getAsk, CurrenciesExchangeRatesResDTO::setSellingRate);
    }
}