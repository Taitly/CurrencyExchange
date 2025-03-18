package com.taitly.currencyexchange.util;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import com.taitly.currencyexchange.exception.DatabaseException;

public class ConnectionPoolManager {
    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig("hikari.properties");
        dataSource = new HikariDataSource(config);
    }

    public static Connection get() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new DatabaseException("Connection error, database unavailable.");
        }
    }

    private ConnectionPoolManager() {
    }
}