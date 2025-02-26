package com.taitly.currencyexchange.service;

import com.taitly.currencyexchange.dao.CurrencyDao;
import com.taitly.currencyexchange.dao.ExchangeRateDao;
import com.taitly.currencyexchange.dto.ExchangeRateDto;
import com.taitly.currencyexchange.entity.Currency;
import com.taitly.currencyexchange.entity.ExchangeRate;
import com.taitly.currencyexchange.exception.DataAlreadyExistsException;
import com.taitly.currencyexchange.exception.DataNotFoundException;
import com.taitly.currencyexchange.exception.DatabaseException;
import com.taitly.currencyexchange.mapper.ExchangeRateMapper;
import com.taitly.currencyexchange.validation.CurrencyValidator;
import com.taitly.currencyexchange.validation.ExchangeRateValidator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExchangeRateService {
    private static final ExchangeRateService INSTANCE = new ExchangeRateService();
    private final ExchangeRateDao exchangeRateDao = ExchangeRateDao.getInstance();
    private final ExchangeRateMapper exchangeRateMapper = ExchangeRateMapper.getInstance();
    private final CurrencyDao currencyDao = CurrencyDao.getInstance();
    private final ExchangeRateValidator exchangeRateValidator = new ExchangeRateValidator();
    private final CurrencyValidator currencyValidator = new CurrencyValidator();

    public List<ExchangeRateDto> findAll() {
     return exchangeRateDao.findAll().stream().map(exchangeRateMapper :: toDto).toList();
    }

    public ExchangeRateDto findByPairCode(String pairCode) {
        exchangeRateValidator.checkPairCode(pairCode);

        String baseCurrencyCode = pairCode.substring(0, 3);
        String targetCurrencyCode = pairCode.substring(3, 6);

        ExchangeRate exchangeRate = exchangeRateDao.findByPairCode(baseCurrencyCode, targetCurrencyCode)
                .orElseThrow(() -> new DataNotFoundException("No exchange rate was found for a pair of codes: %s .".formatted(pairCode)));

        return exchangeRateMapper.toDto(exchangeRate);
    }

    public ExchangeRateDto create(String baseCurrencyCode, String targetCurrencyCode, String rateValue) {
        currencyValidator.checkCode(baseCurrencyCode);
        currencyValidator.checkCode(targetCurrencyCode);
        exchangeRateValidator.checkRate(rateValue);

        Currency baseCurrency = currencyDao.findByCode(baseCurrencyCode)
                .orElseThrow(() -> new DataNotFoundException("Currency with code %s not found in database".formatted(baseCurrencyCode)));
        Currency targetCurrency = currencyDao.findByCode(targetCurrencyCode)
                .orElseThrow(() -> new DataNotFoundException("Currency with code %s not found in database".formatted(targetCurrencyCode)));

        BigDecimal rate = new BigDecimal(rateValue);

        ExchangeRate exchangeRate = new ExchangeRate(null, baseCurrency, targetCurrency, rate);
        ExchangeRate createdExchangeRate = exchangeRateDao.create(exchangeRate);

        return exchangeRateMapper.toDto(createdExchangeRate);
    }

    public ExchangeRateDto update(String pairCode, String rateValue) {
        exchangeRateValidator.checkPairCode(pairCode);
        exchangeRateValidator.checkRate(rateValue);

        BigDecimal rate = new BigDecimal(rateValue);

        ExchangeRateDto exchangeRateDto = findByPairCode(pairCode);
        ExchangeRate exchangeRate = exchangeRateMapper.toEntity(exchangeRateDto);
        exchangeRate.setRate(rate);

        ExchangeRate updatedExchangeRate = exchangeRateDao.update(exchangeRate);

        return exchangeRateMapper.toDto(updatedExchangeRate);
    }

    public static ExchangeRateService getInstance() {
        return INSTANCE;
    }

    private ExchangeRateService() {

    }
}