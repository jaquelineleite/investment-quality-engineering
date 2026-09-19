package br.com.iqe.api.auth;

import br.com.iqe.support.clients.AccountClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AuthenticationTest {

    private final AccountClient accountClient = new AccountClient();

    @Test
    void shouldAuthenticateAndReturnPaperAccount() {

        Response response = accountClient.getAccount();

        assertEquals(
                200,
                response.statusCode(),
                "Expected successful authentication"
        );

        String accountId = response.jsonPath().getString("id");
        String status = response.jsonPath().getString("status");
        String currency = response.jsonPath().getString("currency");

        assertNotNull(accountId, "Account id should not be null");
        assertFalse(accountId.isBlank(), "Account id should not be blank");

        assertNotNull(status, "Account status should not be null");
        assertFalse(status.isBlank(), "Account status should not be blank");

        assertNotNull(currency, "Currency should not be null");
        assertFalse(currency.isBlank(), "Currency should not be blank");
    }
}