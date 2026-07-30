package com.calculator.api.test.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads and exposes test configuration properties.
 * Properties are read from {@code test-config.properties} on the classpath,
 * with environment-variable overrides.
 */
public class TestConfig {

    private static final TestConfig INSTANCE = new TestConfig();

    private final Properties properties = new Properties();

    private TestConfig() {
        try (InputStream stream = getClass().getClassLoader()
                .getResourceAsStream("test-config.properties")) {
            if (stream != null) {
                properties.load(stream);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load test-config.properties", e);
        }
    }

    public static TestConfig getInstance() {
        return INSTANCE;
    }

    /**
     * Returns the base URL of the Calculator API under test.
     * Override via environment variable {@code CALCULATOR_API_BASE_URL}.
     */
    public String getBaseUrl() {
        String env = System.getenv("CALCULATOR_API_BASE_URL");
        if (env != null && !env.isBlank()) {
            return env;
        }
        return properties.getProperty("api.base.url", "http://localhost:8080");
    }

    /**
     * Returns the API version path segment (e.g. {@code /api/v1}).
     */
    public String getApiVersion() {
        return properties.getProperty("api.version", "/api/v1");
    }

    /**
     * Returns the connection timeout in milliseconds.
     */
    public int getConnectionTimeout() {
        return Integer.parseInt(properties.getProperty("api.timeout.connection", "5000"));
    }

    /**
     * Returns the read timeout in milliseconds.
     */
    public int getReadTimeout() {
        return Integer.parseInt(properties.getProperty("api.timeout.read", "10000"));
    }
}
