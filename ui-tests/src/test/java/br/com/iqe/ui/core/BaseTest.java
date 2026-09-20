package br.com.iqe.ui.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeEach
    void setUp() {

        ChromeOptions options = new ChromeOptions();

        boolean headless = Boolean.parseBoolean(
                System.getenv().getOrDefault("HEADLESS", "false")
        );

        if (headless) {
            options.addArguments("--headless=new");
        }

        options.addArguments(
                "--window-size=1920,1080",
                "--disable-dev-shm-usage",
                "--no-sandbox"
        );

        driver = new ChromeDriver(options);

        driver.manage()
                .timeouts()
                .implicitlyWait(Duration.ZERO);

        driver.manage()
                .window()
                .maximize();
    }

    protected String getBaseUrl() {

        return System.getenv()
                .getOrDefault(
                        "UI_BASE_URL",
                        "http://localhost:8081"
                );
    }

    @AfterEach
    void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
}