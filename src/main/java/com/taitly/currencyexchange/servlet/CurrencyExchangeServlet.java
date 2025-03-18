package com.taitly.currencyexchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taitly.currencyexchange.dto.CurrencyExchangeDto;
import com.taitly.currencyexchange.service.CurrencyExchangeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchange")
public class CurrencyExchangeServlet extends HttpServlet {
    CurrencyExchangeService currencyExchangeService = CurrencyExchangeService.getInstance();
    ObjectMapper objectMapper = new ObjectMapper();
    private final CurrencyExchangeService currencyExchangeService = CurrencyExchangeService.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fromCode = req.getParameter("from");
        String toCode = req.getParameter("to");
        String amount = req.getParameter("amount");

        CurrencyExchangeDto currencyExchangeDto = currencyExchangeService.exchange(fromCode, toCode, amount);

        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), currencyExchangeDto);
    }
}