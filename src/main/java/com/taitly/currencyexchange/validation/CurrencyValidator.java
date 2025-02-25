package com.taitly.currencyexchange.validation;

import com.taitly.currencyexchange.exception.InvalidDataException;

import java.util.Currency;
import java.util.regex.Pattern;

public class CurrencyValidator {
    private static final String NAME_REGEX = "^[a-zA-Z]+(\\s+[a-zA-Z]+)*$";
    private static final String CODE_REGEX = "^[A-Z]{3}$";
    private static final String CURRENCY_SIGN_REGEX = "^[\\p{L}\\p{S}]{1,5}$";

    public void checkName(String name) {
        if (name == null || name.isEmpty()) {
            throw new InvalidDataException("Currency name cannot be empty.");
        }

        if (!Pattern.matches(NAME_REGEX, name)) {
            throw new InvalidDataException("Currency name must start with a letter and contain only letters and spaces.");
        }

        if (name.length() > 30) {
            throw new InvalidDataException("Maximum length for currency name is 30 characters.");
        }
    }

    public void checkCode(String code) {
        if (code == null || code.isEmpty()) {
            throw new InvalidDataException("Currency code cannot be empty.");
        }

        if (!Pattern.matches(CODE_REGEX, code)) {
            throw new InvalidDataException("Currency code must be exactly three uppercase letters.");
        }

        checkCurrencyExist(code);
    }

    public void checkSign(String sign) {
        if (sign == null || sign.isEmpty()) {
            throw new InvalidDataException("Currency sign cannot be empty.");
        }

        if (!Pattern.matches(CURRENCY_SIGN_REGEX, sign)) {
            throw new InvalidDataException("Currency sign must be 1 to 5 characters long and contain only letters or special symbols.");
        }
    }

    private void checkCurrencyExist(String code) {
        try {
            Currency.getInstance(code);
        } catch (IllegalArgumentException e) {
            throw new InvalidDataException("This currency doesn't exist.");
        }
    }
}