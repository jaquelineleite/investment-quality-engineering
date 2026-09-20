package br.com.iqe.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AssetsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By symbol =
            By.cssSelector("[data-testid='asset-symbol']");

    private final By searchButton =
            By.cssSelector("[data-testid='search-asset-button']");

    private final By result =
            By.cssSelector("[data-testid='asset-result']");

    public AssetsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(5)
        );
    }

    public AssetsPage search(String assetSymbol) {

        driver.findElement(symbol).clear();
        driver.findElement(symbol).sendKeys(assetSymbol);

        driver.findElement(searchButton).click();

        return this;
    }

    public String getResult() {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        result
                )
        ).getText();
    }
}