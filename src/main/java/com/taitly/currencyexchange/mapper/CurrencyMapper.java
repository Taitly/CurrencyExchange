package com.taitly.currencyexchange.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.taitly.currencyexchange.dto.CurrencyRequestDto;
import com.taitly.currencyexchange.dto.CurrencyResponseDto;
import com.taitly.currencyexchange.entity.Currency;

@Mapper
public interface CurrencyMapper {
    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    CurrencyResponseDto toDto(Currency currency);
    Currency toEntity(CurrencyRequestDto currencyRequestDto);
}