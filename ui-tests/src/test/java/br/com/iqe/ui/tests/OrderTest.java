package br.com.iqe.ui.tests;

import br.com.iqe.ui.core.BaseTest;
import br.com.iqe.ui.pages.LoginPage;
import br.com.iqe.ui.pages.OrderPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderTest extends BaseTest {

    @BeforeEach
    void login() {

        new LoginPage(driver)
                .open(getBaseUrl())
                .login(
                        "qa.user",
                        "Quality123!"
                )
                .isDashboardVisible();
    }

    @Test
    void shouldCreateValidBuyOrder() {

        OrderPage order =
                new OrderPage(driver);

        order.createBuyOrder(
                "AAPL",
                2
        );

        assertEquals(
                "Ordem criada: BUY 2 AAPL",
                order.getOrderResult()
        );
    }

    @Test
    void shouldRejectInvalidOrder() {

        OrderPage order =
                new OrderPage(driver);

        order.createBuyOrder(
                "INVALID",
                0
        );

        assertEquals(
                "Dados da ordem inválidos",
                order.getOrderResult()
        );
    }
}