package br.com.iqe.integration;

import br.com.iqe.integration.crm.CrmClient;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class CustomerCrmSyncTest {

    private HttpServer server;

    @AfterEach
    void stopServer() {

        if (server != null) {
            server.stop(0);
        }
    }


    @Test
    void shouldSynchronizeCustomerWithCrm()
            throws Exception {

        AtomicReference<String> receivedBody =
                new AtomicReference<>();

        AtomicReference<String> receivedCorrelationId =
                new AtomicReference<>();

        server = createServer(
                exchange -> {

                    receivedBody.set(
                            readBody(exchange)
                    );

                    receivedCorrelationId.set(
                            exchange
                                    .getRequestHeaders()
                                    .getFirst(
                                            "X-Correlation-Id"
                                    )
                    );

                    respond(
                            exchange,
                            201,
                            """
                            {
                              "crmCustomerId": "CRM-001",
                              "status": "SYNCED"
                            }
                            """
                    );
                }
        );

        String correlationId =
                UUID.randomUUID()
                        .toString();

        CrmClient client =
                new CrmClient(
                        baseUrl()
                );

        CrmClient.CrmResponse response =
                client.syncCustomer(
                        "CUS-001",
                        "QA Customer",
                        "qa.customer@example.com",
                        correlationId
                );

        assertEquals(
                201,
                response.statusCode()
        );

        assertTrue(
                response.body()
                        .contains(
                                "\"status\": \"SYNCED\""
                        )
        );

        assertTrue(
                receivedBody
                        .get()
                        .contains(
                                "\"customerId\": \"CUS-001\""
                        )
        );

        assertTrue(
                receivedBody
                        .get()
                        .contains(
                                "\"email\": \"qa.customer@example.com\""
                        )
        );

        assertEquals(
                correlationId,
                receivedCorrelationId.get()
        );
    }


    @Test
    void shouldReturnValidationErrorFromCrm()
            throws Exception {

        server = createServer(
                exchange ->
                        respond(
                                exchange,
                                400,
                                """
                                {
                                  "error": "INVALID_CUSTOMER",
                                  "field": "email"
                                }
                                """
                        )
        );

        CrmClient client =
                new CrmClient(
                        baseUrl()
                );

        CrmClient.CrmResponse response =
                client.syncCustomer(
                        "CUS-002",
                        "Invalid Customer",
                        "",
                        UUID.randomUUID()
                                .toString()
                );

        assertEquals(
                400,
                response.statusCode()
        );

        assertTrue(
                response.body()
                        .contains(
                                "INVALID_CUSTOMER"
                        )
        );
    }


    @Test
    void shouldSurfaceCrmUnavailableResponse()
            throws Exception {

        server = createServer(
                exchange ->
                        respond(
                                exchange,
                                503,
                                """
                                {
                                  "error": "CRM_UNAVAILABLE"
                                }
                                """
                        )
        );

        CrmClient client =
                new CrmClient(
                        baseUrl()
                );

        CrmClient.CrmResponse response =
                client.syncCustomer(
                        "CUS-003",
                        "Unavailable Test",
                        "unavailable@example.com",
                        UUID.randomUUID()
                                .toString()
                );

        assertEquals(
                503,
                response.statusCode()
        );

        assertTrue(
                response.body()
                        .contains(
                                "CRM_UNAVAILABLE"
                        )
        );
    }


    private HttpServer createServer(
            ExchangeHandler handler
    ) throws IOException {

        HttpServer httpServer =
                HttpServer.create(
                        new InetSocketAddress(
                                "127.0.0.1",
                                0
                        ),
                        0
                );

        httpServer.createContext(
                "/api/customers",
                exchange -> {

                    assertEquals(
                            "POST",
                            exchange
                                    .getRequestMethod()
                    );

                    assertTrue(
                            exchange
                                    .getRequestHeaders()
                                    .getFirst(
                                            "Content-Type"
                                    )
                                    .contains(
                                            "application/json"
                                    )
                    );

                    handler.handle(exchange);
                }
        );

        httpServer.start();

        return httpServer;
    }


    private String baseUrl() {

        return "http://127.0.0.1:"
                + server
                .getAddress()
                .getPort();
    }


    private String readBody(
            HttpExchange exchange
    ) throws IOException {

        return new String(
                exchange
                        .getRequestBody()
                        .readAllBytes(),
                StandardCharsets.UTF_8
        );
    }


    private void respond(
            HttpExchange exchange,
            int statusCode,
            String response
    ) throws IOException {

        byte[] bytes =
                response.getBytes(
                        StandardCharsets.UTF_8
                );

        exchange
                .getResponseHeaders()
                .add(
                        "Content-Type",
                        "application/json"
                );

        exchange.sendResponseHeaders(
                statusCode,
                bytes.length
        );

        exchange
                .getResponseBody()
                .write(bytes);

        exchange.close();
    }


    @FunctionalInterface
    interface ExchangeHandler {

        void handle(
                HttpExchange exchange
        ) throws IOException;
    }
}