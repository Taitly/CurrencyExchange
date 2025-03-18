package com.taitly.currencyexchange.dto;

import com.taitly.currencyexchange.entity.Currency;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

public record ExchangeRateResponseDto(
        Long id,
        CurrencyResponseDto baseCurrency,
        CurrencyResponseDto targetCurrency,
        BigDecimal rate
) {}