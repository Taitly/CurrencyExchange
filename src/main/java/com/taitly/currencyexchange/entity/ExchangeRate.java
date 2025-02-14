package com.taitly.currencyexchange.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRate {
    Long id;
    Currency BaseCurrency;
    Currency TargetCurrency;
    BigDecimal rate;
}