package br.com.iqe.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By username =
            By.cssSelector("[data-testid='username']");

    private final By password =
            By.cssSelector("[data-testid='password']");

    private final By loginButton =
            By.cssSelector("[data-testid='login-button']");

    private final By loginError =
            By.cssSelector("[data-testid='login-error']");

    private final By dashboardTitle =
            By.cssSelector("[data-testid='dashboard-title']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(5)
        );
    }

    public LoginPage open(String baseUrl) {

        driver.get(baseUrl);

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        username
                )
        );

        return this;
    }

    public LoginPage login(
            String user,
            String pass
    ) {

        driver.findElement(username)
                .sendKeys(user);

        driver.findElement(password)
                .sendKeys(pass);

        driver.findElement(loginButton)
                .click();

        return this;
    }

    public boolean isDashboardVisible() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        dashboardTitle
                )
        ).isDisplayed();
    }

    public String getLoginError() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        loginError
                )
        ).getText();
    }
}