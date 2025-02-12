package com.taitly.currencyexchange.dao;

import com.taitly.currencyexchange.entity.Currency;
import com.taitly.currencyexchange.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class CurrencyDao {
    private static final CurrencyDao INSTANCE = new CurrencyDao();

    private static final String FIND_ALL = """
            SELECT  id,
                    Code,
                    FullName,
                    Sign
              FROM  Currencies
            """;
    private static final String FIND_BY_CODE = """
            SELECT  id,
                    Code,
                    FullName,
                    Sign
              FROM  Currencies
             WHERE  Code = ?
            """;

    private static final String SAVE_CURRENCY = """
            INSERT INTO Currencies (Code, FullName, Sign)
                 VALUES (?, ?, ?)
            """;

    public List<Currency> findAll() {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Currency> currencies = new ArrayList<>();
            while (resultSet.next()) {
                currencies.add(buildCurrency(resultSet));
            }
            return currencies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Currency> findByCode(String code) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODE)) {
            preparedStatement.setObject(1, code);

            ResultSet resultSet = preparedStatement.executeQuery();
            Currency currency = null;
            while (resultSet.next()) {
                currency = buildCurrency(resultSet);
            }
            return Optional.ofNullable(currency);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Currency save(Currency currency) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_CURRENCY, RETURN_GENERATED_KEYS)) {

            preparedStatement.setObject(1, currency.getCode());
            preparedStatement.setObject(2, currency.getName());
            preparedStatement.setObject(3, currency.getSign());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();

            currency.setId(generatedKeys.getLong("id"));

            return currency;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Currency buildCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getLong("id"),
                resultSet.getString("Code"),
                resultSet.getString("FullName"),
                resultSet.getString("Sign")
        );
    }

    public static CurrencyDao getInstance() {
        return INSTANCE;
    }

    private CurrencyDao() {
    }
}