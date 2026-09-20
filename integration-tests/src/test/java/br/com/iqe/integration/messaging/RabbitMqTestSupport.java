package br.com.iqe.integration.messaging;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

public abstract class RabbitMqTestSupport {

    protected Connection openConnection()
            throws Exception {

        ConnectionFactory factory =
                new ConnectionFactory();

        factory.setHost(
                System.getenv()
                        .getOrDefault(
                                "RABBITMQ_HOST",
                                "localhost"
                        )
        );

        factory.setPort(
                Integer.parseInt(
                        System.getenv()
                                .getOrDefault(
                                        "RABBITMQ_PORT",
                                        "5672"
                                )
                )
        );

        factory.setUsername(
                System.getenv()
                        .getOrDefault(
                                "RABBITMQ_USER",
                                "iqe_user"
                        )
        );

        factory.setPassword(
                System.getenv()
                        .getOrDefault(
                                "RABBITMQ_PASSWORD",
                                "iqe_password"
                        )
        );

        return factory.newConnection();
    }

    protected GetResponse waitForMessage(
            com.rabbitmq.client.Channel channel,
            String queue,
            long timeoutMillis
    ) throws Exception {

        long deadline =
                System.currentTimeMillis()
                        + timeoutMillis;

        while (
                System.currentTimeMillis()
                        < deadline
        ) {

            GetResponse response =
                    channel.basicGet(
                            queue,
                            true
                    );

            if (response != null) {
                return response;
            }

            Thread.sleep(100);
        }

        return null;
    }
}