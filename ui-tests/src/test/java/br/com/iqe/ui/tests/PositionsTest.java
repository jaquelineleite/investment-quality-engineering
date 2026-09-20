package br.com.iqe.ui.tests;

import br.com.iqe.ui.core.BaseTest;
import br.com.iqe.ui.pages.LoginPage;
import br.com.iqe.ui.pages.OrderPage;
import br.com.iqe.ui.pages.PositionsPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PositionsTest extends BaseTest {

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
    void shouldStartWithoutOpenPositions() {

        PositionsPage positions =
                new PositionsPage(driver);

        assertEquals(
                "Nenhuma posição aberta",
                positions.getPositions()
        );
    }

    @Test
    void shouldDisplayPositionAfterBuyOrder() {

        new OrderPage(driver)
                .createBuyOrder(
                        "AAPL",
                        3
                );

        PositionsPage positions =
                new PositionsPage(driver);

        assertEquals(
                "AAPL - 3 shares",
                positions.getPositions()
        );
    }
}