package br.com.iqe.api.tests;

import br.com.iqe.support.clients.OrdersClient;
import br.com.iqe.support.factories.OrderFactory;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderLifecycleTest {

    private final OrdersClient ordersClient = new OrdersClient();

    @Test
    void shouldCreateReadAndCancelPaperLimitOrder() {

        Map<String, Object> payload =
                OrderFactory.safeLimitBuyOrder();

        Response createResponse =
                ordersClient.createOrder(payload);

        assertEquals(
                200,
                createResponse.statusCode(),
                "Expected order creation to return HTTP 200"
        );

        String orderId =
                createResponse.jsonPath().getString("id");

        String symbol =
                createResponse.jsonPath().getString("symbol");

        String side =
                createResponse.jsonPath().getString("side");

        String type =
                createResponse.jsonPath().getString("type");

        assertNotNull(orderId);
        assertFalse(orderId.isBlank());

        assertEquals("AAPL", symbol);
        assertEquals("buy", side);
        assertEquals("limit", type);

        Response getResponse =
                ordersClient.getOrderById(orderId);

        assertEquals(
                200,
                getResponse.statusCode(),
                "Expected created order to be retrievable"
        );

        assertEquals(
                orderId,
                getResponse.jsonPath().getString("id")
        );

        Response cancelResponse =
                ordersClient.cancelOrder(orderId);

        assertEquals(
                204,
                cancelResponse.statusCode(),
                "Expected open order cancellation to return HTTP 204"
        );
    }
}