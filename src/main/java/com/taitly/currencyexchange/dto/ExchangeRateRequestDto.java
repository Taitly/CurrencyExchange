package com.taitly.currencyexchange.dto;

import com.taitly.currencyexchange.entity.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
@AllArgsConstructor
public class ExchangeRateRequestDto {
    String baseCurrencyCode;
    String targetCurrencyCode;
    BigDecimal rate;
}