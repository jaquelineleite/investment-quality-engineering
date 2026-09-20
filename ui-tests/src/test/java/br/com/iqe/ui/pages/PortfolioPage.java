package br.com.iqe.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PortfolioPage {

    private final WebDriver driver;

    private final By availableBalance =
            By.cssSelector("[data-testid='available-balance']");

    private final By portfolioStatus =
            By.cssSelector("[data-testid='portfolio-status']");

    public PortfolioPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getAvailableBalance() {
        return driver
                .findElement(availableBalance)
                .getText();
    }

    public String getPortfolioStatus() {
        return driver
                .findElement(portfolioStatus)
                .getText();
    }
}