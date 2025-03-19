package com.taitly.currencyexchange.service;

import com.taitly.currencyexchange.dao.CurrencyDao;
import com.taitly.currencyexchange.dao.ExchangeRateDao;
import com.taitly.currencyexchange.dto.ExchangeRateRequestDto;
import com.taitly.currencyexchange.dto.ExchangeRateResponseDto;
import com.taitly.currencyexchange.entity.Currency;
import com.taitly.currencyexchange.entity.ExchangeRate;
import com.taitly.currencyexchange.exception.DataNotFoundException;
import com.taitly.currencyexchange.mapper.ExchangeRateMapper;

import java.math.BigDecimal;
import java.util.List;

public class ExchangeRateService {
    private static final ExchangeRateService INSTANCE = new ExchangeRateService();
    private final ExchangeRateDao exchangeRateDao = ExchangeRateDao.getInstance();
    private final ExchangeRateMapper exchangeRateMapper = ExchangeRateMapper.INSTANCE;
    private final CurrencyDao currencyDao = CurrencyDao.getInstance();

    public List<ExchangeRateResponseDto> findAll() {
        return exchangeRateDao.findAll().stream().map(exchangeRateMapper::toDto).toList();
    }

    public ExchangeRateResponseDto findByPairCode(ExchangeRateRequestDto exchangeRateRequestDto) {
        String baseCurrencyCode = exchangeRateRequestDto.baseCurrencyCode();
        String targetCurrencyCode = exchangeRateRequestDto.targetCurrencyCode();

        ExchangeRate exchangeRate = exchangeRateDao.findByPairCode(baseCurrencyCode, targetCurrencyCode)
                .orElseThrow(() -> new DataNotFoundException("No exchange rate was found for a pair of codes: %s/%s .".formatted(baseCurrencyCode, targetCurrencyCode)));

        return exchangeRateMapper.toDto(exchangeRate);
    }

    public ExchangeRateResponseDto create(ExchangeRateRequestDto exchangeRateRequestDto) {
        String baseCurrencyCode = exchangeRateRequestDto.baseCurrencyCode();
        String targetCurrencyCode = exchangeRateRequestDto.targetCurrencyCode();
        BigDecimal rate = exchangeRateRequestDto.rate();


        Currency baseCurrency = currencyDao.findByCode(baseCurrencyCode)
                .orElseThrow(() -> new DataNotFoundException("Currency with code %s not found in database".formatted(baseCurrencyCode)));
        Currency targetCurrency = currencyDao.findByCode(targetCurrencyCode)
                .orElseThrow(() -> new DataNotFoundException("Currency with code %s not found in database".formatted(targetCurrencyCode)));


        ExchangeRate exchangeRate = new ExchangeRate(null, baseCurrency, targetCurrency, rate);
        ExchangeRate createdExchangeRate = exchangeRateDao.create(exchangeRate);

        return exchangeRateMapper.toDto(createdExchangeRate);
    }

    public ExchangeRateResponseDto update(ExchangeRateRequestDto exchangeRateRequestDto) {
        String baseCurrencyCode = exchangeRateRequestDto.baseCurrencyCode();
        String targetCurrencyCode = exchangeRateRequestDto.targetCurrencyCode();
        BigDecimal rate = exchangeRateRequestDto.rate();

        ExchangeRate exchangeRate = exchangeRateDao.findByPairCode(baseCurrencyCode, targetCurrencyCode)
                .orElseThrow(() -> new DataNotFoundException("No exchange rate was found for a pair of codes: %s/%s .".formatted(baseCurrencyCode, targetCurrencyCode)));

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