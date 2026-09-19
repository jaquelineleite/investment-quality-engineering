package br.com.iqe.support.clients;

import br.com.iqe.support.config.EnvironmentConfig;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AssetsClient {

    public Response getAsset(String symbol) {

        EnvironmentConfig.validateSafeEnvironment();

        return given()
                .baseUri(EnvironmentConfig.alpacaBaseUrl())
                .header(
                        "APCA-API-KEY-ID",
                        EnvironmentConfig.alpacaApiKey()
                )
                .header(
                        "APCA-API-SECRET-KEY",
                        EnvironmentConfig.alpacaSecretKey()
                )
                .pathParam("symbol", symbol)
                .when()
                .get("/v2/assets/{symbol}");
    }
}