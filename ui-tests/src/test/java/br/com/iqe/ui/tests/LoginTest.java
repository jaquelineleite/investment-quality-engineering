package br.com.iqe.ui.tests;

import br.com.iqe.ui.core.BaseTest;
import br.com.iqe.ui.pages.LoginPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginTest extends BaseTest {

    @Test
    void shouldLoginWithValidCredentials() {

        LoginPage loginPage =
                new LoginPage(driver);

        loginPage
                .open(getBaseUrl())
                .login(
                        "qa.user",
                        "Quality123!"
                );

        assertTrue(
                loginPage.isDashboardVisible(),
                "Dashboard should be visible after valid login."
        );
    }

    @Test
    void shouldRejectInvalidCredentials() {

        LoginPage loginPage =
                new LoginPage(driver);

        loginPage
                .open(getBaseUrl())
                .login(
                        "invalid.user",
                        "invalid.password"
                );

        assertEquals(
                "Usuário ou senha inválidos",
                loginPage.getLoginError()
        );
    }
}