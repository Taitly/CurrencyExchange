package com.taitly.currencyexchange.dto;

import lombok.Builder;
import lombok.Value;

public record CurrencyResponseDto(
        Long id,
        String code,
        String name,
        String sign
) {}