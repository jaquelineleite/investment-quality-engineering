package br.com.iqe.integration.messaging;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.GetResponse;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RabbitMqIntegrationTest
        extends RabbitMqTestSupport {


    @Test
    void shouldPublishAndConsumeOrderEvent()
            throws Exception {

        String queue =
                "iqe.order."
                        + UUID.randomUUID();

        try (
                Connection connection =
                        openConnection();

                Channel channel =
                        connection.createChannel()
        ) {

            channel.queueDeclare(
                    queue,
                    false,
                    true,
                    true,
                    null
            );

            String message =
                    """
                    {
                      "eventType": "ORDER_CREATED",
                      "orderId": "ORD-001",
                      "symbol": "AAPL"
                    }
                    """;

            channel.basicPublish(
                    "",
                    queue,
                    null,
                    message.getBytes(
                            StandardCharsets.UTF_8
                    )
            );

            GetResponse response =
                    waitForMessage(
                            channel,
                            queue,
                            3000
                    );

            assertNotNull(
                    response,
                    "Published message should be consumed."
            );

            String received =
                    new String(
                            response.getBody(),
                            StandardCharsets.UTF_8
                    );

            assertTrue(
                    received.contains(
                            "\"eventType\": \"ORDER_CREATED\""
                    )
            );

            assertTrue(
                    received.contains(
                            "\"orderId\": \"ORD-001\""
                    )
            );
        }
    }


    @Test
    void shouldPreserveCorrelationIdAndMetadata()
            throws Exception {

        String queue =
                "iqe.metadata."
                        + UUID.randomUUID();

        String correlationId =
                UUID.randomUUID()
                        .toString();

        try (
                Connection connection =
                        openConnection();

                Channel channel =
                        connection.createChannel()
        ) {

            channel.queueDeclare(
                    queue,
                    false,
                    true,
                    true,
                    null
            );

            Map<String, Object> headers =
                    new HashMap<>();

            headers.put(
                    "eventType",
                    "ORDER_FILLED"
            );

            headers.put(
                    "source",
                    "investment-quality-engineering"
            );

            AMQP.BasicProperties properties =
                    new AMQP.BasicProperties
                            .Builder()
                            .correlationId(
                                    correlationId
                            )
                            .contentType(
                                    "application/json"
                            )
                            .headers(headers)
                            .build();

            channel.basicPublish(
                    "",
                    queue,
                    properties,
                    """
                    {"orderId":"ORD-001"}
                    """
                            .getBytes(
                                    StandardCharsets.UTF_8
                            )
            );

            GetResponse response =
                    waitForMessage(
                            channel,
                            queue,
                            3000
                    );

            assertNotNull(response);

            assertEquals(
                    correlationId,
                    response
                            .getProps()
                            .getCorrelationId()
            );

            assertEquals(
                    "application/json",
                    response
                            .getProps()
                            .getContentType()
            );

            assertEquals(
                    "ORDER_FILLED",
                    response
                            .getProps()
                            .getHeaders()
                            .get("eventType")
                            .toString()
            );

            assertEquals(
                    "investment-quality-engineering",
                    response
                            .getProps()
                            .getHeaders()
                            .get("source")
                            .toString()
            );
        }
    }


    @Test
    void rejectedMessageShouldGoToDeadLetterQueue()
            throws Exception {

        String suffix =
                UUID.randomUUID()
                        .toString();

        String sourceQueue =
                "iqe.source."
                        + suffix;

        String deadLetterQueue =
                "iqe.dlq."
                        + suffix;

        String deadLetterExchange =
                "iqe.dlx."
                        + suffix;

        String deadLetterRoutingKey =
                "order.invalid";

        try (
                Connection connection =
                        openConnection();

                Channel channel =
                        connection.createChannel()
        ) {

            channel.exchangeDeclare(
                    deadLetterExchange,
                    "direct",
                    false,
                    true,
                    null
            );

            channel.queueDeclare(
                    deadLetterQueue,
                    false,
                    true,
                    true,
                    null
            );

            channel.queueBind(
                    deadLetterQueue,
                    deadLetterExchange,
                    deadLetterRoutingKey
            );

            Map<String, Object> arguments =
                    new HashMap<>();

            arguments.put(
                    "x-dead-letter-exchange",
                    deadLetterExchange
            );

            arguments.put(
                    "x-dead-letter-routing-key",
                    deadLetterRoutingKey
            );

            channel.queueDeclare(
                    sourceQueue,
                    false,
                    true,
                    true,
                    arguments
            );

            String invalidEvent =
                    """
                    {
                      "eventType": "ORDER_CREATED",
                      "orderId": "",
                      "reason": "INVALID_ORDER"
                    }
                    """;

            channel.basicPublish(
                    "",
                    sourceQueue,
                    null,
                    invalidEvent.getBytes(
                            StandardCharsets.UTF_8
                    )
            );

            GetResponse original =
                    channel.basicGet(
                            sourceQueue,
                            false
                    );

            assertNotNull(
                    original,
                    "Message should exist in source queue."
            );

            channel.basicReject(
                    original
                            .getEnvelope()
                            .getDeliveryTag(),
                    false
            );

            GetResponse deadLetter =
                    waitForMessage(
                            channel,
                            deadLetterQueue,
                            5000
                    );

            assertNotNull(
                    deadLetter,
                    "Rejected message should reach DLQ."
            );

            String received =
                    new String(
                            deadLetter.getBody(),
                            StandardCharsets.UTF_8
                    );

            assertTrue(
                    received.contains(
                            "\"reason\": \"INVALID_ORDER\""
                    )
            );
        }
    }
}