package com.taitly.currencyexchange.mapper;

import com.taitly.currencyexchange.dto.CurrencyRequestDto;
import com.taitly.currencyexchange.dto.CurrencyResponseDto;
import com.taitly.currencyexchange.entity.Currency;

public class CurrencyMapper {
    private static final CurrencyMapper INSTANCE = new CurrencyMapper();

    public CurrencyResponseDto toDto(Currency currency) {
        return CurrencyResponseDto.builder()
                .id(currency.getId())
                .code(currency.getCode())
                .name(currency.getName())
                .sign(currency.getSign())
                .build();
    }

    public Currency toEntity(CurrencyRequestDto currencyRequestDto) {
        return new Currency(
                null,
                currencyRequestDto.getCode(),
                currencyRequestDto.getName(),
                currencyRequestDto.getCode()
        );
    }

    public static CurrencyMapper getInstance() {
        return INSTANCE;
    }

    private CurrencyMapper() {
    }
}