package com.taitly.currencyexchange.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

public record ExchangeRateRequestDto(
        String baseCurrencyCode,
        String targetCurrencyCode,
        BigDecimal rate
) {}