package com.taitly.currencyexchange.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CurrencyDto {
    String code;
    String name;
    String sign;
}
