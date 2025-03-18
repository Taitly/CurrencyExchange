package com.taitly.currencyexchange.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import static jakarta.servlet.http.HttpServletResponse.*;

import com.taitly.currencyexchange.exception.DataAlreadyExistsException;
import com.taitly.currencyexchange.exception.DataNotFoundException;
import com.taitly.currencyexchange.exception.DatabaseException;
import com.taitly.currencyexchange.exception.InvalidDataException;

@WebFilter(value = {
        "/currencies", "/currency/*", "/exchangeRates", "/exchangeRate/*", "/exchange"
})
public class ErrorFilter extends HttpFilter {
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            super.doFilter(req, res, chain);
        } catch (InvalidDataException e) {
            writeError(res, SC_BAD_REQUEST, e);
        } catch (DataAlreadyExistsException e) {
            writeError(res, SC_CONFLICT, e);
        } catch (DataNotFoundException e) {
            writeError(res, SC_NOT_FOUND, e);
        } catch (DatabaseException e) {
            writeError(res, SC_INTERNAL_SERVER_ERROR, e);
        }
    }

    private void writeError(HttpServletResponse res, int errorCode, RuntimeException e) throws IOException {
        Map<String, String> error = Map.of("message", e.getMessage());
        String json = objectMapper.writeValueAsString(error);
        res.setStatus(errorCode);
        res.getWriter().write(json);
    }
}