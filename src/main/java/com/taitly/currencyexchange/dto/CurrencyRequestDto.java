package com.taitly.currencyexchange.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class CurrencyRequestDto {
    String code;
    String name;
    String sign;
}