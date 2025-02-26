package com.taitly.currencyexchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taitly.currencyexchange.dto.CurrencyExchangeDto;
import com.taitly.currencyexchange.exception.DataNotFoundException;
import com.taitly.currencyexchange.exception.DatabaseException;
import com.taitly.currencyexchange.exception.InvalidDataException;
import com.taitly.currencyexchange.service.CurrencyExchangeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/exchange")
public class CurrenciesExchangeServlet extends HttpServlet {
    CurrencyExchangeService currencyExchangeService = CurrencyExchangeService.getInstance();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter printWriter = resp.getWriter();
        String fromCode = req.getParameter("from");
        String toCode = req.getParameter("to");
        String amount = req.getParameter("amount");

        CurrencyExchangeDto currencyExchangeDto = currencyExchangeService.exchange(fromCode, toCode, amount);
        String json = objectMapper.writeValueAsString(currencyExchangeDto);
        printWriter.write(json);
    }
}