package br.com.iqe.support.clients;

import br.com.iqe.support.config.EnvironmentConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrdersClient {

    private RequestSpecification request() {

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
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }

    public Response createOrder(Map<String, Object> payload) {
        return request()
                .body(payload)
                .when()
                .post("/v2/orders");
    }

    public Response getOrderById(String orderId) {
        return request()
                .pathParam("orderId", orderId)
                .when()
                .get("/v2/orders/{orderId}");
    }

    public Response getOrderByClientOrderId(String clientOrderId) {
        return request()
                .queryParam(
                        "client_order_id",
                        clientOrderId
                )
                .when()
                .get("/v2/orders:by_client_order_id");
    }

    public Response cancelOrder(String orderId) {
        return request()
                .pathParam("orderId", orderId)
                .when()
                .delete("/v2/orders/{orderId}");
    }
}