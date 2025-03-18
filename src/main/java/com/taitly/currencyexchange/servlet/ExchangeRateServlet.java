package com.taitly.currencyexchange.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.taitly.currencyexchange.dto.ExchangeRateRequestDto;
import com.taitly.currencyexchange.dto.ExchangeRateResponseDto;
import com.taitly.currencyexchange.exception.InvalidDataException;
import com.taitly.currencyexchange.service.ExchangeRateService;
import com.taitly.currencyexchange.validation.ExchangeRateValidator;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ExchangeRateValidator exchangeRateValidator = new ExchangeRateValidator();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pairCode = req.getPathInfo().substring(1);
        String baseCurrencyCode = pairCode.substring(0, 3);
        String targetCurrencyCode = pairCode.substring(3, 6);

        exchangeRateValidator.checkPairCode(baseCurrencyCode, targetCurrencyCode);

        ExchangeRateRequestDto exchangeRateRequestDto = new ExchangeRateRequestDto(baseCurrencyCode, targetCurrencyCode, null);
        ExchangeRateResponseDto exchangeRateResponseDto = exchangeRateService.findByPairCode(exchangeRateRequestDto);

        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), exchangeRateResponseDto);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pairCode = req.getPathInfo().substring(1);
        String requestBody = readRequestBody(req);
        String rate = parseRateFromRequest(requestBody);
        String baseCurrencyCode = pairCode.substring(0, 3);
        String targetCurrencyCode = pairCode.substring(3, 6);

        exchangeRateValidator.checkRate(rate);
        exchangeRateValidator.checkPairCode(baseCurrencyCode, targetCurrencyCode);

        BigDecimal rateValue = new BigDecimal(rate);

        ExchangeRateRequestDto exchangeRateRequestDto = new ExchangeRateRequestDto(baseCurrencyCode, targetCurrencyCode, rateValue);
        ExchangeRateResponseDto exchangeRateResponseDto = exchangeRateService.update(exchangeRateRequestDto);

        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), exchangeRateResponseDto);
    }

    private String readRequestBody(HttpServletRequest req) throws IOException {
        StringBuilder body = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
        }
        return body.toString();
    }

    private String parseRateFromRequest(String requestBody) {
        String[] params = requestBody.split("&");
        for (String param : params) {
            if (param.startsWith("rate=")) {
                String rate = param.substring(5);
                return rate.replace(',', '.');
            }
        }
        throw new InvalidDataException("Missing required parameter: rate");
    }
}