package br.com.iqe.integration.crm;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CrmClient {

    private final HttpClient httpClient;
    private final String baseUrl;

    public CrmClient(String baseUrl) {
        this.httpClient = HttpClient.newHttpClient();
        this.baseUrl = baseUrl;
    }

    public CrmResponse syncCustomer(
            String customerId,
            String name,
            String email,
            String correlationId
    ) throws Exception {

        String payload = """
                {
                  "customerId": "%s",
                  "name": "%s",
                  "email": "%s"
                }
                """.formatted(
                customerId,
                name,
                email
        );

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        baseUrl
                                                + "/api/customers"
                                )
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .header(
                                "X-Correlation-Id",
                                correlationId
                        )
                        .POST(
                                HttpRequest.BodyPublishers
                                        .ofString(payload)
                        )
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers
                                .ofString()
                );

        return new CrmResponse(
                response.statusCode(),
                response.body()
        );
    }

    public record CrmResponse(
            int statusCode,
            String body
    ) {
    }
}