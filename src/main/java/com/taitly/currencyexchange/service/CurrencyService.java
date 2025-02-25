package com.taitly.currencyexchange.service;

import com.taitly.currencyexchange.dao.CurrencyDao;
import com.taitly.currencyexchange.dto.CurrencyDto;
import com.taitly.currencyexchange.entity.Currency;
import com.taitly.currencyexchange.exception.DataAlreadyExistsException;
import com.taitly.currencyexchange.exception.DataNotFoundException;
import com.taitly.currencyexchange.mapper.CurrencyMapper;
import com.taitly.currencyexchange.validation.CurrencyValidator;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

public class CurrencyService {
    private static final CurrencyService INSTANCE = new CurrencyService();
    private final CurrencyDao currencyDao = CurrencyDao.getInstance();
    private final CurrencyMapper currencyMapper =  CurrencyMapper.getInstance();
    private final CurrencyValidator currencyValidator = new CurrencyValidator();

    public List<CurrencyDto> findAll() {
        return currencyDao.findAll().stream().map(currencyMapper :: toDto).toList();
    }

    public CurrencyDto findByCode(String code) {
        currencyValidator.checkCode(code);

        Currency currency = currencyDao.findByCode(code)
                .orElseThrow(() -> new DataNotFoundException("Currency not found in the database."));

        return currencyMapper.toDto(currency);
    }

    public CurrencyDto create(CurrencyDto currencyDto) {
        currencyValidator.checkCode(currencyDto.getCode());
        currencyValidator.checkName(currencyDto.getName());
        currencyValidator.checkSign(currencyDto.getSign());

        Optional<Currency> existingCurrency = currencyDao.findByCode(currencyDto.getCode());
        if(existingCurrency.isPresent()) {
            throw new DataAlreadyExistsException("Currency with code %s already exists in the database.".formatted(currencyDto.getCode()));
        }

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