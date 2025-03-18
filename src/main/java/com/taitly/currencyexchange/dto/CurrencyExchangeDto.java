package com.taitly.currencyexchange.dto;

import java.math.BigDecimal;

import com.taitly.currencyexchange.entity.Currency;

public record CurrencyExchangeDto (
    Currency baseCurrency,
    Currency targetCurrency,
    BigDecimal rate,
    BigDecimal amount,
    BigDecimal convertedAmount
) {}