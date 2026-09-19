package br.com.iqe.api.contract;

import br.com.iqe.support.clients.OrdersClient;
import br.com.iqe.support.factories.OrderFactory;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderContractTest {

    private final OrdersClient ordersClient = new OrdersClient();

    @Test
    void shouldMatchOrderContract() {

        Map<String, Object> payload =
                OrderFactory.safeLimitBuyOrder();

        String orderId = null;

        try {
            Response response =
                    ordersClient.createOrder(payload);

            assertEquals(
                    200,
                    response.statusCode(),
                    "Expected order creation to return HTTP 200"
            );

            orderId =
                    response.jsonPath().getString("id");

            assertNotNull(orderId);
            assertFalse(orderId.isBlank());

            response.then()
                    .assertThat()
                    .body(
                            matchesJsonSchemaInClasspath(
                                    "schemas/order-schema.json"
                            )
                    );

        } finally {

            if (orderId != null && !orderId.isBlank()) {
                ordersClient.cancelOrder(orderId);
            }
        }
    }
}