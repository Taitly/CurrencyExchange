package com.taitly.currencyexchange.service;

import com.taitly.currencyexchange.dao.CurrencyDao;
import com.taitly.currencyexchange.dao.ExchangeRateDao;
import com.taitly.currencyexchange.dto.ExchangeRateDto;
import com.taitly.currencyexchange.entity.Currency;
import com.taitly.currencyexchange.entity.ExchangeRate;
import com.taitly.currencyexchange.exception.DataAlreadyExistsException;
import com.taitly.currencyexchange.exception.DatabaseException;
import com.taitly.currencyexchange.mapper.ExchangeRateMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExchangeRateService {
    private static final ExchangeRateService INSTANCE = new ExchangeRateService();
    private final ExchangeRateDao exchangeRateDao = ExchangeRateDao.getInstance();
    private final ExchangeRateMapper exchangeRateMapper = ExchangeRateMapper.getInstance();
    private final CurrencyDao currencyDao = CurrencyDao.getInstance();

    public List<ExchangeRateDto> findAll() {
     return exchangeRateDao.findAll().stream().map(exchangeRateMapper :: toDto).toList();
    }

    public ExchangeRateDto findByPairCode(String pairCode) {
        String baseCurrencyCode = pairCode.substring(0, 3);
        String targetCurrencyCode = pairCode.substring(3, 6);

        Optional<ExchangeRate> exchangeRateOptional = exchangeRateDao.findByPairCode(baseCurrencyCode, targetCurrencyCode);
        ExchangeRate exchangeRate = exchangeRateOptional.orElseThrow(() -> new RuntimeException("ExchangeRate not found"));

        return exchangeRateMapper.toDto(exchangeRate);
    }

    public ExchangeRateDto create(String baseCurrencyCode, String targetCurrencyCode, String rateValue) {
        Currency baseCurrency = currencyDao.findByCode(baseCurrencyCode)
                .orElseThrow(() -> new RuntimeException("Currency with code %s not found in database".formatted(baseCurrencyCode)));
        Currency targetCurrency = currencyDao.findByCode(targetCurrencyCode)
                .orElseThrow(() -> new RuntimeException("Currency with code %s not found in database".formatted(targetCurrencyCode)));

        Optional<ExchangeRate> existingRate = exchangeRateDao.findByPairCode(baseCurrencyCode, targetCurrencyCode);

        if(existingRate.isPresent()) {
            throw new DataAlreadyExistsException("Exchange rate for a pair of codes %s/%s already exists.".formatted(baseCurrencyCode, targetCurrencyCode));
        }

        BigDecimal rate = new BigDecimal(rateValue);

        ExchangeRate exchangeRate = new ExchangeRate(null, baseCurrency, targetCurrency, rate);
        ExchangeRate createdExchangeRate = exchangeRateDao.create(exchangeRate);

        return exchangeRateMapper.toDto(createdExchangeRate);
    }

    public ExchangeRateDto update(String pairCode, String rateValue) {
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