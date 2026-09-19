package br.com.iqe.api.contract;

import br.com.iqe.support.clients.AccountClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountContractTest {

    private final AccountClient accountClient = new AccountClient();

    @Test
    void shouldMatchAccountContract() {

        Response response = accountClient.getAccount();

        assertEquals(
                200,
                response.statusCode(),
                "Expected account endpoint to return HTTP 200"
        );

        response.then()
                .assertThat()
                .body(
                        matchesJsonSchemaInClasspath(
                                "schemas/account-schema.json"
                        )
                );
    }
}
