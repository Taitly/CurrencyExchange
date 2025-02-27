package com.taitly.currencyexchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taitly.currencyexchange.dto.ExchangeRateRequestDto;
import com.taitly.currencyexchange.dto.ExchangeRateResponseDto;
import com.taitly.currencyexchange.service.ExchangeRateService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ExchangeRateResponseDto> exchangeRates = exchangeRateService.findAll();

        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), exchangeRates);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rate = req.getParameter("rate");

        BigDecimal rateValue = new BigDecimal(rate);

        ExchangeRateRequestDto exchangeRateRequestDto = new ExchangeRateRequestDto(baseCurrencyCode, targetCurrencyCode, rateValue);

        ExchangeRateResponseDto exchangeRateResponseDto = exchangeRateService.create(exchangeRateRequestDto);

        resp.setStatus(HttpServletResponse.SC_CREATED);
        objectMapper.writeValue(resp.getWriter(), exchangeRateResponseDto);
    }
}