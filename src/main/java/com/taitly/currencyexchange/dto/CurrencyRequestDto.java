package com.taitly.currencyexchange.dto;

public record CurrencyRequestDto(
        String code,
        String name,
        String sign
) {}