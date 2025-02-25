package com.taitly.currencyexchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taitly.currencyexchange.dto.CurrencyDto;
import com.taitly.currencyexchange.entity.Currency;
import com.taitly.currencyexchange.exception.DataAlreadyExistsException;
import com.taitly.currencyexchange.exception.DatabaseException;
import com.taitly.currencyexchange.exception.InvalidDataException;
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
import java.util.Map;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private final CurrencyService currencyService = CurrencyService.getInstance();

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CurrencyMapper currencyMapper = CurrencyMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter printWriter = resp.getWriter();

        try {
            List<CurrencyDto> currencies = currencyService.findAll();
            resp.setStatus(HttpServletResponse.SC_OK);
            String json = objectMapper.writeValueAsString(currencies);
            printWriter.write(json);
        } catch (DatabaseException e) {
            writeErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), printWriter);
        } finally {
            printWriter.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter printWriter = resp.getWriter();

        try {
            String code = req.getParameter("code");
            String name = req.getParameter("name");
            String sign = req.getParameter("sign");
            Currency currencyToCreate = new Currency(null, code, name, sign);

            CurrencyDto currency = currencyService.create(currencyMapper.toDto(currencyToCreate));
            resp.setStatus(HttpServletResponse.SC_CREATED);
            String json = objectMapper.writeValueAsString(currency);
            printWriter.write(json);
        } catch (InvalidDataException e) {
            writeErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), printWriter);
        } catch (DataAlreadyExistsException e) {
            writeErrorResponse(resp, HttpServletResponse.SC_CONFLICT, e.getMessage(), printWriter);
        } catch (DatabaseException e) {
            writeErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), printWriter);
        } finally {
            printWriter.close();
        }
    }

    private void writeErrorResponse(HttpServletResponse resp, int errorCode, String errorMessage, PrintWriter printWriter) throws IOException {
        resp.setStatus(errorCode);
        Map<String, String> errorResponse = Map.of("message", errorMessage);
        String json = objectMapper.writeValueAsString(errorResponse);
        printWriter.write(json);
    }
}