package com.taitly.currencyexchange.dao;

import com.taitly.currencyexchange.entity.Currency;
import com.taitly.currencyexchange.entity.ExchangeRate;
import com.taitly.currencyexchange.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class ExchangeRateDao {
    private static final ExchangeRateDao INSTANCE = new ExchangeRateDao();

    private static final String FIND_ALL = """
        SELECT  ExchangeRates.id,
                ExchangeRates.BaseCurrencyId,
                BaseCurrency.code AS BaseCurrencyCode,
                BaseCurrency.FullName AS BaseCurrencyFullName,
                BaseCurrency.Sign AS BaseCurrencySign,
                ExchangeRates.TargetCurrencyId,
                TargetCurrency.code AS TargetCurrencyCode,
                TargetCurrency.FullName AS TargetCurrencyFullName,
                TargetCurrency.Sign AS TargetCurrencySign,
                ExchangeRates.Rate
          FROM  ExchangeRates
          JOIN  main.Currencies BaseCurrency ON ExchangeRates.BaseCurrencyId = BaseCurrency.ID
          JOIN  main.Currencies TargetCurrency ON ExchangeRates.TargetCurrencyId = TargetCurrency.ID
        """;

    private static final String FIND_BY_PAIR_CODE = """
        SELECT  ExchangeRates.id,
                ExchangeRates.BaseCurrencyId,
                BaseCurrency.code AS BaseCurrencyCode,
                BaseCurrency.FullName AS BaseCurrencyFullName,
                BaseCurrency.Sign AS BaseCurrencySign,
                ExchangeRates.TargetCurrencyId,
                TargetCurrency.code AS TargetCurrencyCode,
                TargetCurrency.FullName AS TargetCurrencyFullName,
                TargetCurrency.Sign AS TargetCurrencySign,
                ExchangeRates.Rate
          FROM  ExchangeRates
          JOIN  main.Currencies BaseCurrency ON ExchangeRates.BaseCurrencyId = BaseCurrency.ID
          JOIN  main.Currencies TargetCurrency ON ExchangeRates.TargetCurrencyId = TargetCurrency.ID
         WHERE  BaseCurrencyCode = ? and TargetCurrencyCode = ?
        """;

    private static final String SAVE_EXCHANGE_RATE = """
        INSERT INTO  ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate)
             VALUES  (?, ?, ?)
        """;

    private static final String UPDATE_EXCHANGE_RATE = """
        UPDATE  ExchangeRates
           SET  Rate = ?
         WHERE  BaseCurrencyId = (SELECT ID FROM Currencies WHERE Code = ?)
           AND  TargetCurrencyId = (SELECT ID FROM Currencies WHERE Code = ?)
        """;

    public List<ExchangeRate> findAll() {
        try (Connection connection = ConnectionManager.get();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            List<ExchangeRate> exchangeRates = new ArrayList<>();
            while (resultSet.next()) {
                exchangeRates.add(buildExchangeRate(resultSet));
            }
            return exchangeRates;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<ExchangeRate> findByPairCode(String baseCurrencyCode, String targetCurrencyCode) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_PAIR_CODE)){

            preparedStatement.setString(1, baseCurrencyCode);
            preparedStatement.setString(2, targetCurrencyCode);

            ResultSet resultSet = preparedStatement.executeQuery();
            ExchangeRate exchangeRate = null;
            while (resultSet.next()) {
                exchangeRate = buildExchangeRate(resultSet);
            }
            return Optional.ofNullable(exchangeRate);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ExchangeRate create(ExchangeRate exchangeRate) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_EXCHANGE_RATE, RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, exchangeRate.getBaseCurrency().getId());
            preparedStatement.setLong(2, exchangeRate.getTargetCurrency().getId());
            preparedStatement.setBigDecimal(3, exchangeRate.getRate());
            preparedStatement.executeUpdate();

            ResultSet generateKeys = preparedStatement.getGeneratedKeys();
            if (generateKeys.next()) {
                Long id = generateKeys.getLong(1);
                return new ExchangeRate(
                        id,
                        exchangeRate.getBaseCurrency(),
                        exchangeRate.getTargetCurrency(),
                        exchangeRate.getRate()
                );
            }
            throw new SQLException("No ID obtained");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ExchangeRate update(ExchangeRate exchangeRate) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_EXCHANGE_RATE)){

            preparedStatement.setBigDecimal(1, exchangeRate.getRate());
            preparedStatement.setString(2, exchangeRate.getBaseCurrency().getCode());
            preparedStatement.setString(2, exchangeRate.getTargetCurrency().getCode());
            preparedStatement.executeUpdate();

            return exchangeRate;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ExchangeRate buildExchangeRate(ResultSet resultSet) throws SQLException {
        return new ExchangeRate(
                resultSet.getLong("id"),
                new Currency(
                        resultSet.getLong("BaseCurrencyId"),
                        resultSet.getString("BaseCurrencyCode"),
                        resultSet.getString("BaseCurrencyFullName"),
                        resultSet.getString("BaseCurrencySign")
                ),
                new Currency(
                        resultSet.getLong("TargetCurrencyId"),
                        resultSet.getString("TargetCurrencyCode"),
                        resultSet.getString("TargetCurrencyFullName"),
                        resultSet.getString("TargetCurrencySign")
                ),
                resultSet.getBigDecimal("Rate")
        );
    }

    public static ExchangeRateDao getInstance() {
        return INSTANCE;
    }

    private ExchangeRateDao() {
    }
}
