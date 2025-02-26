package com.taitly.currencyexchange.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(value = {
        "/currencies", "/currency/*", "/exchangeRates", "/exchangeRate/*", "/exchange"
})
public class ContentTypeFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        super.doFilter(req, res, chain);
    }
}