package com.taitly.currencyexchange.validation;

import com.taitly.currencyexchange.exception.InvalidDataException;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class ExchangeRateValidator {
    private static final String PAIR_CODE_REGEX = "^[A-Z]{3}[A-Z]{3}$";
    private static final String RATE_REGEX = "^(0|[1-9][0-9]*)(\\.[0-9])?$";
    private static final String AMOUNT_REGEX = "^\\d+$";
    private static final int MAX_RATE_LENGTH = 10;


    public void checkPairCode(String pairCode) {
        if (!Pattern.matches(PAIR_CODE_REGEX, pairCode)) {
            throw new InvalidDataException("Invalid currency pair code. It should be in the format XXXYYY, where XXX and YYY are three-letter currency codes (e.g., USDEUR).");
        }

        String baseCode = pairCode.substring(0, 3);
        String targetCode = pairCode.substring(3, 6);
        if (baseCode.equals(targetCode)) {
            throw new InvalidDataException("Codes in pair cannot be equal.");
        }
    }

    public void checkRate(String rate) {
        if (rate == null || rate.isEmpty()) {
            throw new InvalidDataException("Exchange rate cannot be empty.");
        }

        if (!Pattern.matches(RATE_REGEX, rate)) {
            throw new InvalidDataException("Exchange rate must be a number, which can be an integer or decimal.");
        }

        BigDecimal rateValue = new BigDecimal(rate);
        if (rateValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidDataException("Exchange rate must be greater than 0.");
        }

        if (rate.length() > MAX_RATE_LENGTH) {
            throw new InvalidDataException("Exchange rate string is too long. Maximum length is %d characters.".formatted(MAX_RATE_LENGTH));
        }
    }

    public void checkAmount(String amount) {
        if (amount == null || amount.isEmpty()) {
            throw new InvalidDataException("Amount for exchange cannot be empty.");
        }

        if (!Pattern.matches(AMOUNT_REGEX, amount)) {
            throw new InvalidDataException("Amount must be integer number.");
        }

        int amountValue = Integer.parseInt(amount);
        if (!(amountValue > 0)) {
            throw new InvalidDataException("Amount must be greater than 0.");
        }
    }
}