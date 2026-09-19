package br.com.iqe.support.factories;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public final class OrderFactory {

    private OrderFactory() {
    }

    public static Map<String, Object> safeLimitBuyOrder() {
        return safeLimitBuyOrder(
                "iqe-" + UUID.randomUUID()
        );
    }

    public static Map<String, Object> safeLimitBuyOrder(
            String clientOrderId
    ) {

        Map<String, Object> order =
                new LinkedHashMap<>();

        order.put("symbol", "AAPL");
        order.put("qty", "1");
        order.put("side", "buy");
        order.put("type", "limit");
        order.put("time_in_force", "gtc");
        order.put("limit_price", "1.00");
        order.put(
                "client_order_id",
                clientOrderId
        );

        return order;
    }
}