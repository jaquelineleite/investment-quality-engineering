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

class OrderIdempotencyTest {

    private final OrdersClient ordersClient =
            new OrdersClient();

    @Test
    void shouldRejectDuplicateClientOrderId() {

        String clientOrderId =
                "iqe-idem-" + UUID.randomUUID();

        Map<String, Object> payload =
                OrderFactory.safeLimitBuyOrder(
                        clientOrderId
                );

        String firstOrderId = null;

        try {

            Response firstResponse =
                    ordersClient.createOrder(payload);

            assertEquals(
                    200,
                    firstResponse.statusCode(),
                    "First order should be accepted"
            );

            firstOrderId =
                    firstResponse.jsonPath()
                            .getString("id");

            assertNotNull(firstOrderId);
            assertFalse(firstOrderId.isBlank());

            assertEquals(
                    clientOrderId,
                    firstResponse.jsonPath()
                            .getString("client_order_id")
            );

            Response duplicateResponse =
                    ordersClient.createOrder(payload);

            assertEquals(
                    422,
                    duplicateResponse.statusCode(),
                    "Duplicate client_order_id should be rejected"
            );

            assertFalse(
                    duplicateResponse.asString().isBlank(),
                    "Duplicate rejection should contain details"
            );

        } finally {

            if (firstOrderId != null
                    && !firstOrderId.isBlank()) {

                ordersClient.cancelOrder(
                        firstOrderId
                );
            }
        }
    }
}