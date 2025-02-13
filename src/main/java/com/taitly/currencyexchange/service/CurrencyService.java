package com.taitly.currencyexchange.service;

import com.taitly.currencyexchange.dao.CurrencyDao;
import com.taitly.currencyexchange.dto.CurrencyDto;
import com.taitly.currencyexchange.entity.Currency;
import com.taitly.currencyexchange.mapper.CurrencyMapper;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class CurrencyService {
    private static final CurrencyService INSTANCE = new CurrencyService();
    private final CurrencyDao currencyDao = CurrencyDao.getInstance();
    private final CurrencyMapper currencyMapper =  CurrencyMapper.getInstance();

    public List<CurrencyDto> findAll() {
        return currencyDao.findAll().stream().map(currencyMapper :: toDto).toList();
    }

    public CurrencyDto findByCode(String code) {
        Optional<Currency> currencyOptional = currencyDao.findByCode(code);
        Currency currency = currencyOptional.orElseThrow(() -> new NoSuchElementException("Currency not found"));

        return currencyMapper.toDto(currency);
    }

    public CurrencyDto create(CurrencyDto currencyDto) {
        Currency currency = currencyMapper.toEntity(currencyDto);
        Currency createdCurrency = currencyDao.create(currency);

        return currencyMapper.toDto(createdCurrency);
    }

    public static CurrencyService getInstance() {
        return INSTANCE;
    }

    private CurrencyService() {

    }
}