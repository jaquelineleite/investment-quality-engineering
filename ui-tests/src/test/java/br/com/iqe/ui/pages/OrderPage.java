package br.com.iqe.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OrderPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By symbol =
            By.cssSelector("[data-testid='asset-symbol']");

    private final By quantity =
            By.cssSelector("[data-testid='order-quantity']");

    private final By buyButton =
            By.cssSelector("[data-testid='buy-button']");

    private final By orderResult =
            By.cssSelector("[data-testid='order-result']");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(5)
        );
    }

    public OrderPage createBuyOrder(
            String assetSymbol,
            int orderQuantity
    ) {

        driver.findElement(symbol).clear();
        driver.findElement(symbol).sendKeys(assetSymbol);

        driver.findElement(quantity).clear();
        driver.findElement(quantity)
                .sendKeys(String.valueOf(orderQuantity));

        driver.findElement(buyButton).click();

        return this;
    }

    public String getOrderResult() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        orderResult
                )
        ).getText();
    }
}