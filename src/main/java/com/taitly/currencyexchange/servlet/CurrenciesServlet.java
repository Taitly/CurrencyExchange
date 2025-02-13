package com.taitly.currencyexchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taitly.currencyexchange.dto.CurrencyDto;
import com.taitly.currencyexchange.entity.Currency;
import com.taitly.currencyexchange.mapper.CurrencyMapper;
import com.taitly.currencyexchange.service.CurrencyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private final CurrencyService currencyService = CurrencyService.getInstance();

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CurrencyMapper currencyMapper =  CurrencyMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter printWriter = resp.getWriter()) {
            List<CurrencyDto> currencies = currencyService.findAll();
            String json = objectMapper.writeValueAsString(currencies);
            printWriter.write(json);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter printWriter = resp.getWriter()) {
            String code = req.getParameter("code");
            String name = req.getParameter("name");
            String sign = req.getParameter("sign");
            Currency currencyToCreate = new Currency(null, code, name, sign);

            CurrencyDto currency = currencyService.create(currencyMapper.toDto(currencyToCreate));
            printWriter.write(objectMapper.writeValueAsString(currency));
        }
    }
}