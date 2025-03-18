package com.taitly.currencyexchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taitly.currencyexchange.dto.CurrencyRequestDto;
import com.taitly.currencyexchange.dto.CurrencyResponseDto;
import com.taitly.currencyexchange.service.CurrencyService;
import com.taitly.currencyexchange.validation.CurrencyValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private final CurrencyService currencyService = CurrencyService.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CurrencyValidator currencyValidator = new CurrencyValidator();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getPathInfo().substring(1);

        currencyValidator.checkCode(code);

        CurrencyRequestDto currencyRequestDto = new CurrencyRequestDto(code, null, null);

        CurrencyResponseDto currencyResponseDto = currencyService.findByCode(currencyRequestDto);

        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), currencyResponseDto);
    }
}