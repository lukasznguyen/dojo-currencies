package com.example.demo.configs;

import com.example.demo.models.dtos.responses.AvailableCurrenciesResDTO;
import com.example.demo.nbp.dtos.SingleCurrencyDetails;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        map_SingleCurrencyDetails_to_AvailableCurrenciesResDTO(mapper);
        mapper.validate();
        return mapper;
    }

    private void map_SingleCurrencyDetails_to_AvailableCurrenciesResDTO(ModelMapper mapper) {
        TypeMap<SingleCurrencyDetails, AvailableCurrenciesResDTO> config = mapper.createTypeMap(SingleCurrencyDetails.class, AvailableCurrenciesResDTO.class);
        config.addMapping(SingleCurrencyDetails::getCurrency, AvailableCurrenciesResDTO::setCurrencyName);
    }
}
