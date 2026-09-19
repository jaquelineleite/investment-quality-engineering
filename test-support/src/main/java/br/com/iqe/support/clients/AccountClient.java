package br.com.iqe.support.clients;

import br.com.iqe.support.config.EnvironmentConfig;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AccountClient {

    public Response getAccount() {

        EnvironmentConfig.validateSafeEnvironment();

        return getAccountWithCredentials(
                EnvironmentConfig.alpacaApiKey(),
                EnvironmentConfig.alpacaSecretKey()
        );
    }

    public Response getAccountWithCredentials(
            String apiKey,
            String secretKey
    ) {

        EnvironmentConfig.validateSafeEnvironment();

        return given()
                .baseUri(
                        EnvironmentConfig.alpacaBaseUrl()
                )
                .header(
                        "APCA-API-KEY-ID",
                        apiKey
                )
                .header(
                        "APCA-API-SECRET-KEY",
                        secretKey
                )
                .when()
                .get("/v2/account");
    }
}