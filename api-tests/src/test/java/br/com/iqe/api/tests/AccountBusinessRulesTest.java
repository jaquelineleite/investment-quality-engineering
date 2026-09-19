package br.com.iqe.api.tests;

import br.com.iqe.support.clients.AccountClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountBusinessRulesTest {

    private final AccountClient accountClient = new AccountClient();

    @Test
    void shouldReturnConsistentFinancialAccountData() {

        Response response = accountClient.getAccount();

        assertEquals(
                200,
                response.statusCode(),
                "Expected account endpoint to return HTTP 200"
        );

        String currency = response.jsonPath().getString("currency");
        String status = response.jsonPath().getString("status");
        String cashValue = response.jsonPath().getString("cash");
        String buyingPowerValue =
                response.jsonPath().getString("buying_power");
        String portfolioValue =
                response.jsonPath().getString("portfolio_value");

        assertEquals(
                "USD",
                currency,
                "Paper account currency should be USD"
        );

        assertNotNull(status, "Account status should not be null");
        assertFalse(status.isBlank(), "Account status should not be blank");

        BigDecimal cash = new BigDecimal(cashValue);
        BigDecimal buyingPower = new BigDecimal(buyingPowerValue);
        BigDecimal portfolio = new BigDecimal(portfolioValue);

        assertNotNull(cash);

        assertTrue(
                buyingPower.compareTo(BigDecimal.ZERO) >= 0,
                "Buying power should not be negative"
        );

        assertTrue(
                portfolio.compareTo(BigDecimal.ZERO) >= 0,
                "Portfolio value should not be negative"
        );
    }
}
