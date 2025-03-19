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
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private final CurrencyService currencyService = CurrencyService.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CurrencyValidator currencyValidator = new CurrencyValidator();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<CurrencyResponseDto> currencies = currencyService.findAll();

        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), currencies);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        String name = req.getParameter("name");
        String sign = req.getParameter("sign");

        currencyValidator.checkCode(code);
        currencyValidator.checkName(name);
        currencyValidator.checkSign(sign);

        CurrencyRequestDto currencyRequestDto = new CurrencyRequestDto(code, name, sign);
        CurrencyResponseDto currencyResponseDto = currencyService.create(currencyRequestDto);

        resp.setStatus(HttpServletResponse.SC_CREATED);
        objectMapper.writeValue(resp.getWriter(), currencyResponseDto);
    }
}