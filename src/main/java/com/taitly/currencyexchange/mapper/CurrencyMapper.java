package com.taitly.currencyexchange.mapper;

import com.taitly.currencyexchange.dto.CurrencyDto;
import com.taitly.currencyexchange.entity.Currency;

public class CurrencyMapper {
    private static final CurrencyMapper INSTANCE = new CurrencyMapper();

    public CurrencyDto toDto(Currency currency) {
        return CurrencyDto.builder()
                .id(currency.getId())
                .code(currency.getCode())
                .name(currency.getName())
                .sign(currency.getSign())
                .build();
    }

    public Currency toEntity(CurrencyDto currencyDto) {
        return new Currency(
                currencyDto.getId(),
                currencyDto.getCode(),
                currencyDto.getName(),
                currencyDto.getSign()
        );
    }

    public static CurrencyMapper getInstance() {
        return INSTANCE;
    }

    private CurrencyMapper() {
    }
}