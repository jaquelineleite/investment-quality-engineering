package br.com.iqe.api.tests;

import br.com.iqe.support.clients.OrdersClient;
import br.com.iqe.support.factories.OrderFactory;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderLookupByClientIdTest {

    private final OrdersClient ordersClient =
            new OrdersClient();

    @Test
    void shouldRetrieveOrderByClientOrderId() {

        String clientOrderId =
                "iqe-lookup-" + UUID.randomUUID();

        Map<String, Object> payload =
                OrderFactory.safeLimitBuyOrder(
                        clientOrderId
                );

        String orderId = null;

        try {

            Response createResponse =
                    ordersClient.createOrder(payload);

            assertEquals(
                    200,
                    createResponse.statusCode()
            );

            orderId =
                    createResponse.jsonPath()
                            .getString("id");

            assertNotNull(orderId);
            assertFalse(orderId.isBlank());

            Response lookupResponse =
                    ordersClient.getOrderByClientOrderId(
                            clientOrderId
                    );

            assertEquals(
                    200,
                    lookupResponse.statusCode()
            );

            assertEquals(
                    orderId,
                    lookupResponse.jsonPath()
                            .getString("id")
            );

            assertEquals(
                    clientOrderId,
                    lookupResponse.jsonPath()
                            .getString("client_order_id")
            );

        } finally {

            if (orderId != null
                    && !orderId.isBlank()) {

                ordersClient.cancelOrder(orderId);
            }
        }
    }
}