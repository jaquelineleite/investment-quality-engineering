package br.com.iqe.api.tests;

import br.com.iqe.support.clients.OrdersClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class OrderBusinessRulesTest {

    private final OrdersClient ordersClient = new OrdersClient();

    @Test
    void shouldRejectLimitOrderWithoutLimitPrice() {

        Map<String, Object> payload = new LinkedHashMap<>();

        payload.put("symbol", "AAPL");
        payload.put("qty", "1");
        payload.put("side", "buy");
        payload.put("type", "limit");
        payload.put("time_in_force", "gtc");

        Response response =
                ordersClient.createOrder(payload);

        assertEquals(
                422,
                response.statusCode(),
                "Limit order without limit_price should be rejected"
        );

        assertFalse(
                response.asString().isBlank(),
                "Error response should contain details"
        );
    }

    @Test
    void shouldRejectOrderWithInvalidSide() {

        Map<String, Object> payload = new LinkedHashMap<>();

        payload.put("symbol", "AAPL");
        payload.put("qty", "1");
        payload.put("side", "hold");
        payload.put("type", "market");
        payload.put("time_in_force", "day");

        Response response =
                ordersClient.createOrder(payload);

        assertEquals(
                422,
                response.statusCode(),
                "Order with invalid side should be rejected"
        );

        assertFalse(
                response.asString().isBlank(),
                "Error response should contain details"
        );
    }
}