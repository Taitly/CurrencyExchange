package com.taitly.currencyexchange.dto;

import com.taitly.currencyexchange.entity.Currency;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

public record CurrencyExchangeDto (
    Currency baseCurrency,
    Currency targetCurrency,
    BigDecimal rate,
    BigDecimal amount,
    BigDecimal convertedAmount
) {}