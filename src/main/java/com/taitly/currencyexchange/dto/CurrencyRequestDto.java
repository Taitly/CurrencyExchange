package com.taitly.currencyexchange.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

public record CurrencyRequestDto(
        String code,
        String name,
        String sign
) {}