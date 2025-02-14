package com.taitly.currencyexchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taitly.currencyexchange.dto.ExchangeRateDto;
import com.taitly.currencyexchange.service.ExchangeRateService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try(PrintWriter printWriter = resp.getWriter()) {
            String pairCode = req.getPathInfo().substring(1);
            ExchangeRateDto exchangeRateDto = exchangeRateService.findByPairCode(pairCode);
            String json = objectMapper.writeValueAsString(exchangeRateDto);
            printWriter.write(json);
        }
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter printWriter = resp.getWriter()){
            String pairCode = req.getPathInfo().substring(1);
            String rate = req.getParameter("rate");
            ExchangeRateDto exchangeRateDto = exchangeRateService.update(pairCode, rate);
            String json = objectMapper.writeValueAsString(exchangeRateDto);
            printWriter.write(json);
        }
    }
}