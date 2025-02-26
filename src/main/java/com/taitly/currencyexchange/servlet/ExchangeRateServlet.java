package com.taitly.currencyexchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taitly.currencyexchange.dto.ExchangeRateDto;
import com.taitly.currencyexchange.exception.DataNotFoundException;
import com.taitly.currencyexchange.exception.DatabaseException;
import com.taitly.currencyexchange.exception.InvalidDataException;
import com.taitly.currencyexchange.service.ExchangeRateService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter printWriter = resp.getWriter();

        try {
            String pairCode = req.getPathInfo().substring(1);
            ExchangeRateDto exchangeRateDto = exchangeRateService.findByPairCode(pairCode);
            resp.setStatus(HttpServletResponse.SC_OK);
            String json = objectMapper.writeValueAsString(exchangeRateDto);
            printWriter.write(json);
        } catch (InvalidDataException e) {
            writeErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), printWriter);
        } catch (DataNotFoundException e) {
            writeErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage(), printWriter);
        } catch (DatabaseException e) {
            writeErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), printWriter);
        } finally {
            printWriter.close();
        }
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter printWriter = resp.getWriter();

        try {
            String pairCode = req.getPathInfo().substring(1);
            String rate = req.getParameter("rate");
            ExchangeRateDto exchangeRateDto = exchangeRateService.update(pairCode, rate);
            resp.setStatus(HttpServletResponse.SC_OK);
            String json = objectMapper.writeValueAsString(exchangeRateDto);
            printWriter.write(json);
        } catch (InvalidDataException e) {
            writeErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), printWriter);
        } catch (DataNotFoundException e) {
            writeErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage(), printWriter);
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