package br.com.iqe.integration.database;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseIntegrityTest
        extends DatabaseTestSupport {


    @Test
    void accountShouldBeActiveAndBalanceNonNegative()
            throws SQLException {

        String sql = """
                SELECT status,
                       available_balance
                FROM accounts
                WHERE id = ?
                """;

        try (
                Connection connection =
                        openConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    "ACC-001"
            );

            try (
                    ResultSet result =
                            statement.executeQuery()
            ) {

                assertTrue(
                        result.next(),
                        "Account ACC-001 should exist."
                );

                assertEquals(
                        "ACTIVE",
                        result.getString("status")
                );

                BigDecimal balance =
                        result.getBigDecimal(
                                "available_balance"
                        );

                assertTrue(
                        balance.compareTo(
                                BigDecimal.ZERO
                        ) >= 0,
                        "Account balance must not be negative."
                );
            }
        }
    }


    @Test
    void ordersShouldReferenceExistingAccounts()
            throws SQLException {

        String sql = """
                SELECT COUNT(*) AS orphan_orders
                FROM orders o
                LEFT JOIN accounts a
                       ON a.id = o.account_id
                WHERE a.id IS NULL
                """;

        try (
                Connection connection =
                        openConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet result =
                        statement.executeQuery()
        ) {

            assertTrue(result.next());

            assertEquals(
                    0,
                    result.getInt(
                            "orphan_orders"
                    ),
                    "Orders without an existing account were found."
            );
        }
    }


    @Test
    void clientOrderIdShouldBeUnique()
            throws SQLException {

        String sql = """
                INSERT INTO orders (
                    id,
                    client_order_id,
                    account_id,
                    symbol,
                    side,
                    quantity,
                    status
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        openConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    "ORD-DUPLICATE-001"
            );

            statement.setString(
                    2,
                    "CLIENT-ORDER-001"
            );

            statement.setString(
                    3,
                    "ACC-001"
            );

            statement.setString(
                    4,
                    "AAPL"
            );

            statement.setString(
                    5,
                    "BUY"
            );

            statement.setBigDecimal(
                    6,
                    BigDecimal.ONE
            );

            statement.setString(
                    7,
                    "NEW"
            );

            SQLException exception =
                    assertThrows(
                            SQLException.class,
                            statement::executeUpdate
                    );

            assertEquals(
                    "23505",
                    exception.getSQLState(),
                    "Expected unique constraint violation."
            );
        }
    }


    @Test
    void orderQuantityShouldBeGreaterThanZero()
            throws SQLException {

        String sql = """
                INSERT INTO orders (
                    id,
                    client_order_id,
                    account_id,
                    symbol,
                    side,
                    quantity,
                    status
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        openConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    "ORD-QTY-INVALID"
            );

            statement.setString(
                    2,
                    "CLIENT-QTY-INVALID"
            );

            statement.setString(
                    3,
                    "ACC-001"
            );

            statement.setString(
                    4,
                    "AAPL"
            );

            statement.setString(
                    5,
                    "BUY"
            );

            statement.setBigDecimal(
                    6,
                    BigDecimal.ZERO
            );

            statement.setString(
                    7,
                    "NEW"
            );

            SQLException exception =
                    assertThrows(
                            SQLException.class,
                            statement::executeUpdate
                    );

            assertEquals(
                    "23514",
                    exception.getSQLState(),
                    "Expected CHECK constraint violation."
            );
        }
    }


    @Test
    void orderSideShouldAcceptOnlyBuyOrSell()
            throws SQLException {

        String sql = """
                INSERT INTO orders (
                    id,
                    client_order_id,
                    account_id,
                    symbol,
                    side,
                    quantity,
                    status
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        openConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    "ORD-SIDE-INVALID"
            );

            statement.setString(
                    2,
                    "CLIENT-SIDE-INVALID"
            );

            statement.setString(
                    3,
                    "ACC-001"
            );

            statement.setString(
                    4,
                    "AAPL"
            );

            statement.setString(
                    5,
                    "HOLD"
            );

            statement.setBigDecimal(
                    6,
                    BigDecimal.ONE
            );

            statement.setString(
                    7,
                    "NEW"
            );

            SQLException exception =
                    assertThrows(
                            SQLException.class,
                            statement::executeUpdate
                    );

            assertEquals(
                    "23514",
                    exception.getSQLState(),
                    "Expected CHECK constraint violation."
            );
        }
    }


    @Test
    void positionShouldHaveValidQuantityAndPrice()
            throws SQLException {

        String sql = """
                SELECT quantity,
                       average_price
                FROM positions
                WHERE id = ?
                """;

        try (
                Connection connection =
                        openConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    "POS-001"
            );

            try (
                    ResultSet result =
                            statement.executeQuery()
            ) {

                assertTrue(
                        result.next(),
                        "Position POS-001 should exist."
                );

                BigDecimal quantity =
                        result.getBigDecimal(
                                "quantity"
                        );

                BigDecimal averagePrice =
                        result.getBigDecimal(
                                "average_price"
                        );

                assertTrue(
                        quantity.compareTo(
                                BigDecimal.ZERO
                        ) > 0
                );

                assertTrue(
                        averagePrice.compareTo(
                                BigDecimal.ZERO
                        ) >= 0
                );
            }
        }
    }
}