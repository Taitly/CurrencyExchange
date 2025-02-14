package com.taitly.currencyexchange.mapper;


import com.taitly.currencyexchange.dto.ExchangeRateDto;
import com.taitly.currencyexchange.entity.ExchangeRate;

public class ExchangeRateMapper {
    private static final ExchangeRateMapper INSTANCE = new ExchangeRateMapper();

    public ExchangeRateDto toDto(ExchangeRate exchangeRate) {
        return ExchangeRateDto.builder()
                .id(exchangeRate.getId())
                .BaseCurrency(exchangeRate.getBaseCurrency())
                .TargetCurrency(exchangeRate.getTargetCurrency())
                .rate(exchangeRate.getRate())
                .build();
    }

    public ExchangeRate toEntity(ExchangeRateDto exchangeRateDto) {
        return new ExchangeRate(
                exchangeRateDto.getId(),
                exchangeRateDto.getBaseCurrency(),
                exchangeRateDto.getTargetCurrency(),
                exchangeRateDto.getRate()
        );
    }

    public static ExchangeRateMapper getInstance() {
        return INSTANCE;
    }

    private ExchangeRateMapper() {

    }
}