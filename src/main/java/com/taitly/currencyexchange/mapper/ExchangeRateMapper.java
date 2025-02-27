package com.taitly.currencyexchange.mapper;


import com.taitly.currencyexchange.dto.ExchangeRateResponseDto;
import com.taitly.currencyexchange.entity.ExchangeRate;

public class ExchangeRateMapper {
    private static final ExchangeRateMapper INSTANCE = new ExchangeRateMapper();

    public ExchangeRateResponseDto toDto(ExchangeRate exchangeRate) {
        return ExchangeRateResponseDto.builder()
                .id(exchangeRate.getId())
                .BaseCurrency(exchangeRate.getBaseCurrency())
                .TargetCurrency(exchangeRate.getTargetCurrency())
                .rate(exchangeRate.getRate())
                .build();
    }

    public static ExchangeRateMapper getInstance() {
        return INSTANCE;
    }

    private ExchangeRateMapper() {

    }
}