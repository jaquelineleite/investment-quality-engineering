package br.com.iqe.api.auth;

import br.com.iqe.support.clients.AccountClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InvalidAuthenticationTest {

    private final AccountClient accountClient =
            new AccountClient();

    @Test
    void shouldRejectInvalidCredentials() {

        Response response =
                accountClient.getAccountWithCredentials(
                        "INVALID_API_KEY",
                        "INVALID_SECRET_KEY"
                );

        assertTrue(
                response.statusCode() == 401
                        || response.statusCode() == 403,
                "Invalid credentials should return 401 or 403"
        );

        assertFalse(
                response.asString().isBlank(),
                "Authentication error should contain details"
        );
    }
}