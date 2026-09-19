package br.com.iqe.support.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.nio.file.Files;
import java.nio.file.Path;

public final class EnvironmentConfig {

    private static final Dotenv DOTENV = loadDotenv();

    private EnvironmentConfig() {
    }

    private static Dotenv loadDotenv() {

        Path currentPath =
                Path.of(System.getProperty("user.dir"))
                        .toAbsolutePath();

        while (currentPath != null) {

            if (Files.exists(currentPath.resolve(".env"))
                    || Files.exists(currentPath.resolve(".env.example"))) {

                return Dotenv.configure()
                        .directory(currentPath.toString())
                        .ignoreIfMissing()
                        .load();
            }

            currentPath = currentPath.getParent();
        }

        return Dotenv.configure()
                .ignoreIfMissing()
                .load();
    }

    public static String getRequired(String key) {

        String value = DOTENV.get(key);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Required environment variable is missing: " + key
            );
        }

        return value;
    }

    public static String get(String key, String defaultValue) {
        return DOTENV.get(key, defaultValue);
    }

    public static String testEnvironment() {
        return get("TEST_ENV", "paper");
    }

    public static String alpacaBaseUrl() {
        return get(
                "ALPACA_BASE_URL",
                "https://paper-api.alpaca.markets"
        );
    }

    public static String alpacaApiKey() {
        return getRequired("ALPACA_API_KEY");
    }

    public static String alpacaSecretKey() {
        return getRequired("ALPACA_SECRET_KEY");
    }

    public static void validateSafeEnvironment() {

        if (!"paper".equalsIgnoreCase(testEnvironment())) {
            throw new IllegalStateException(
                    "Execution blocked: TEST_ENV must be 'paper'."
            );
        }

        if (!alpacaBaseUrl().contains("paper-api.alpaca.markets")) {
            throw new IllegalStateException(
                    "Execution blocked: only Alpaca Paper Trading is allowed."
            );
        }
    }
}