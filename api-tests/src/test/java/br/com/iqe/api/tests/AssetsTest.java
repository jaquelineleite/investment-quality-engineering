package br.com.iqe.api.tests;

import br.com.iqe.support.clients.AssetsClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AssetsTest {

    private final AssetsClient assetsClient = new AssetsClient();

    @Test
    void shouldReturnActiveTradableAsset() {

        Response response = assetsClient.getAsset("AAPL");

        assertEquals(
                200,
                response.statusCode(),
                "Expected AAPL asset to be found"
        );

        String symbol = response.jsonPath().getString("symbol");
        String status = response.jsonPath().getString("status");
        Boolean tradable = response.jsonPath().getBoolean("tradable");

        assertEquals("AAPL", symbol);
        assertEquals("active", status);
        assertNotNull(tradable);
        assertTrue(tradable, "AAPL should be tradable");
    }

    @Test
    void shouldReturnNotFoundForInvalidAsset() {

        Response response =
                assetsClient.getAsset("INVALID_SYMBOL_QE_999");

        assertEquals(
                404,
                response.statusCode(),
                "Expected invalid asset to return HTTP 404"
        );
    }
}