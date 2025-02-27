package com.taitly.currencyexchange.dto;

import com.taitly.currencyexchange.entity.Currency;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class ExchangeRateResponseDto {
    Long id;
    Currency BaseCurrency;
    Currency TargetCurrency;
    BigDecimal rate;
}