package com.taitly.currencyexchange.dto;

public record CurrencyResponseDto(
        Long id,
        String code,
        String name,
        String sign
) {
}