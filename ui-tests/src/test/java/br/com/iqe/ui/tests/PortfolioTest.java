package br.com.iqe.ui.tests;

import br.com.iqe.ui.core.BaseTest;
import br.com.iqe.ui.pages.LoginPage;
import br.com.iqe.ui.pages.PortfolioPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PortfolioTest extends BaseTest {

    @Test
    void shouldDisplayPortfolioInformation() {

        new LoginPage(driver)
                .open(getBaseUrl())
                .login(
                        "qa.user",
                        "Quality123!"
                )
                .isDashboardVisible();

        PortfolioPage portfolio =
                new PortfolioPage(driver);

        assertEquals(
                "$100,000.00",
                portfolio.getAvailableBalance()
        );

        assertEquals(
                "ACTIVE",
                portfolio.getPortfolioStatus()
        );
    }
}