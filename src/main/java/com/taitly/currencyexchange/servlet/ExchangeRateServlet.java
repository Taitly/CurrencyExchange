package com.taitly.currencyexchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taitly.currencyexchange.dto.ExchangeRateRequestDto;
import com.taitly.currencyexchange.dto.ExchangeRateResponseDto;
import com.taitly.currencyexchange.exception.InvalidDataException;
import com.taitly.currencyexchange.service.ExchangeRateService;
import com.taitly.currencyexchange.validation.ExchangeRateValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pairCode = req.getPathInfo().substring(1);
        String baseCurrencyCode = pairCode.substring(0, 3);
        String targetCurrencyCode = pairCode.substring(3, 6);

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