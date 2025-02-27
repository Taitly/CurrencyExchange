package com.taitly.currencyexchange.service;

import com.taitly.currencyexchange.dao.CurrencyDao;
import com.taitly.currencyexchange.dto.CurrencyRequestDto;
import com.taitly.currencyexchange.dto.CurrencyResponseDto;
import com.taitly.currencyexchange.entity.Currency;
import com.taitly.currencyexchange.exception.DataNotFoundException;
import com.taitly.currencyexchange.mapper.CurrencyMapper;
import com.taitly.currencyexchange.validation.CurrencyValidator;

import java.util.List;

public class CurrencyService {
    private static final CurrencyService INSTANCE = new CurrencyService();
    private final CurrencyDao currencyDao = CurrencyDao.getInstance();
    private final CurrencyMapper currencyMapper =  CurrencyMapper.getInstance();
    private final CurrencyValidator currencyValidator = new CurrencyValidator();

    public List<CurrencyResponseDto> findAll() {
        return currencyDao.findAll().stream().map(currencyMapper :: toDto).toList();
    }

    public CurrencyResponseDto findByCode(CurrencyRequestDto currencyRequestDto) {
        currencyValidator.checkCode(currencyRequestDto.getCode());

        Currency currency = currencyDao.findByCode(currencyRequestDto.getCode())
                .orElseThrow(() -> new DataNotFoundException("Currency not found in the database."));

        return currencyMapper.toDto(currency);
    }

    public CurrencyResponseDto create(CurrencyRequestDto currencyRequestDto) {
        currencyValidator.checkCode(currencyRequestDto.getCode());
        currencyValidator.checkName(currencyRequestDto.getName());
        currencyValidator.checkSign(currencyRequestDto.getSign());

        Currency currency = currencyMapper.toEntity(currencyRequestDto);
        Currency createdCurrency = currencyDao.create(currency);

        return currencyMapper.toDto(createdCurrency);
    }

    public static CurrencyService getInstance() {
        return INSTANCE;
    }

    private CurrencyService() {

    }
}