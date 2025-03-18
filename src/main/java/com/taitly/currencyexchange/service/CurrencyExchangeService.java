package com.taitly.currencyexchange.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import com.taitly.currencyexchange.dao.ExchangeRateDao;
import com.taitly.currencyexchange.dto.CurrencyExchangeDto;
import com.taitly.currencyexchange.entity.Currency;
import com.taitly.currencyexchange.entity.ExchangeRate;
import com.taitly.currencyexchange.exception.DataNotFoundException;

public class CurrencyExchangeService {
    private static final CurrencyExchangeService INSTANCE = new CurrencyExchangeService();
    private static final String BASE_CROSS_CURRENCY_CODE = "USD";
    private final ExchangeRateDao exchangeRateDao = ExchangeRateDao.getInstance();

    public CurrencyExchangeDto exchange(String from, String to, String amount) {
        Optional<ExchangeRate> optionalExchangeRate = getOptionalExchangeRate(from, to);

        if (optionalExchangeRate.isPresent()) {
            Currency fromCurrency = optionalExchangeRate.get().getBaseCurrency();
            Currency toCurrency = optionalExchangeRate.get().getTargetCurrency();
            BigDecimal rate = optionalExchangeRate.get().getRate();
            BigDecimal amountToConvert = new BigDecimal(amount);
            BigDecimal convertedAmount = amountToConvert.multiply(rate).setScale(2, RoundingMode.HALF_UP);

            return new CurrencyExchangeDto(fromCurrency, toCurrency, rate, amountToConvert, convertedAmount);
        }
        throw new DataNotFoundException("No exchange rate was found for the specified currency code pair");
    }

    private Optional<ExchangeRate> getOptionalExchangeRate(String from, String to) {
        Optional<ExchangeRate> optionalExchangeRate = getDirectExchangeRate(from, to);

        if (optionalExchangeRate.isEmpty()) {
            optionalExchangeRate = getReverseExchangeRate(from, to);
        }

        if (optionalExchangeRate.isEmpty()) {
            optionalExchangeRate = getCrossRateExchangeRate(from, to);
        }

        return optionalExchangeRate;
    }

    private Optional<ExchangeRate> getDirectExchangeRate(String from, String to) {
        return exchangeRateDao.findByPairCode(from, to);
    }

    private Optional<ExchangeRate> getReverseExchangeRate(String from, String to) {
        Optional<ExchangeRate> optionalExchangeRate = exchangeRateDao.findByPairCode(to, from);
        ExchangeRate directExchangeRate = null;

        if (optionalExchangeRate.isPresent()) {
            BigDecimal reverseRate = optionalExchangeRate.get().getRate();
            BigDecimal directRate = BigDecimal.ONE.divide(reverseRate, 6, RoundingMode.HALF_UP);

            directExchangeRate = buildExchangeRate(optionalExchangeRate.get().getBaseCurrency(),
                    optionalExchangeRate.get().getTargetCurrency(), directRate);
        }

        return Optional.ofNullable(directExchangeRate);
    }

    private Optional<ExchangeRate> getCrossRateExchangeRate(String from, String to) {
        Optional<ExchangeRate> exchangeRateFrom = exchangeRateDao.findByPairCode(BASE_CROSS_CURRENCY_CODE, from);
        Optional<ExchangeRate> exchangeRateTo = exchangeRateDao.findByPairCode(BASE_CROSS_CURRENCY_CODE, to);
        ExchangeRate exchangeCrossRate = null;

        if (exchangeRateFrom.isPresent() && exchangeRateTo.isPresent()) {
            BigDecimal rateFrom = exchangeRateFrom.get().getRate();
            BigDecimal rateTo = exchangeRateTo.get().getRate();
            BigDecimal crossRate = rateTo.divide(rateFrom, 6, RoundingMode.HALF_UP);

            exchangeCrossRate = buildExchangeRate(exchangeRateFrom.get().getTargetCurrency(),
                    exchangeRateTo.get().getTargetCurrency(), crossRate);
        }

        return Optional.ofNullable(exchangeCrossRate);
    }

    private ExchangeRate buildExchangeRate(Currency baseCurrency, Currency targetCurrency, BigDecimal rate) {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setBaseCurrency(baseCurrency);
        exchangeRate.setTargetCurrency(targetCurrency);
        exchangeRate.setRate(rate);
        return exchangeRate;
    }

    public static CurrencyExchangeService getInstance() {
        return INSTANCE;
    }

    public CurrencyExchangeService() {

    }
}