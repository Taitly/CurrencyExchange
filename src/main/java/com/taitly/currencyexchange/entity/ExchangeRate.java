package com.taitly.currencyexchange.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRate {
    private Long id;
    private Currency BaseCurrency;
    private Currency TargetCurrency;
    private BigDecimal rate;
}