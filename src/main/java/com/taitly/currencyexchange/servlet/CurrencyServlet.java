package com.taitly.currencyexchange.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taitly.currencyexchange.dto.CurrencyDto;
import com.taitly.currencyexchange.exception.DataNotFoundException;
import com.taitly.currencyexchange.exception.DatabaseException;
import com.taitly.currencyexchange.exception.InvalidDataException;
import com.taitly.currencyexchange.service.CurrencyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    CurrencyService currencyService = CurrencyService.getInstance();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter printWriter = resp.getWriter();

        try {
            String code = req.getPathInfo().substring(1);
            CurrencyDto currencyDto = currencyService.findByCode(code);
            resp.setStatus(HttpServletResponse.SC_OK);
            String json = objectMapper.writeValueAsString(currencyDto);
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