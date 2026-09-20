package br.com.iqe.ui.tests;

import br.com.iqe.ui.core.BaseTest;
import br.com.iqe.ui.pages.AssetsPage;
import br.com.iqe.ui.pages.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AssetsTest extends BaseTest {

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
    void shouldFindTradableAsset() {

        AssetsPage assets =
                new AssetsPage(driver);

        assets.search("AAPL");

        assertEquals(
                "AAPL - Apple Inc. - Tradable",
                assets.getResult()
        );
    }

    @Test
    void shouldReturnNotFoundForUnknownAsset() {

        AssetsPage assets =
                new AssetsPage(driver);

        assets.search("INVALID");

        assertEquals(
                "Ativo não encontrado",
                assets.getResult()
        );
    }
}