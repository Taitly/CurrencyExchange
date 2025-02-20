package com.taitly.currencyexchange.dto;

import com.taitly.currencyexchange.entity.Currency;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class CurrencyExchangeDto {
    Currency baseCurrency;
    Currency targetCurrency;
    BigDecimal rate;
    BigDecimal amount;
    BigDecimal convertedAmount;
}
