package com.taitly.currencyexchange.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.taitly.currencyexchange.dto.ExchangeRateResponseDto;
import com.taitly.currencyexchange.entity.ExchangeRate;

@Mapper(uses = CurrencyMapper.class)
public interface ExchangeRateMapper {
    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);

    ExchangeRateResponseDto toDto(ExchangeRate exchangeRate);
}