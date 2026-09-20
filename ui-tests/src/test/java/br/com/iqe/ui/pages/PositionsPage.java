package br.com.iqe.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PositionsPage {

    private final WebDriver driver;

    private final By positions =
            By.cssSelector("[data-testid='positions-result']");

    public PositionsPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getPositions() {
        return driver
                .findElement(positions)
                .getText();
    }
}