package com.taitly.currencyexchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taitly.currencyexchange.dto.CurrencyDto;
import com.taitly.currencyexchange.service.CurrencyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    CurrencyService currencyService = CurrencyService.getInstance();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try(PrintWriter printWriter = resp.getWriter()) {
            String code = req.getPathInfo().substring(1);
            CurrencyDto currencyDto = currencyService.findByCode(code);
            String json = objectMapper.writeValueAsString(currencyDto);
            printWriter.write(json);
        }
    }
}