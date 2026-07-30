package com.calculator.api.tests.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Centralized configuration for the API test suite.
 * <p>
 * Properties are loaded from {@code src/test/resources/application.properties}
 * and can be overridden via system properties or environment variables.
 * </p>
 */
public class TestConfig {

    private static final TestConfig INSTANCE = new TestConfig();
    private final Properties properties = new Properties();

    private TestConfig() {
        loadProperties();
    }

    public static TestConfig getInstance() {
        return INSTANCE;
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            // Fall back to defaults; properties file is optional
        }
    }

    /**
     * Returns the base URL of the Calculator API under test.
     * Priority: system property {@code api.base.url} > env var {@code API_BASE_URL} > default.
     */
    public String getBaseUrl() {
        String sysProp = System.getProperty("api.base.url");
        if (sysProp != null && !sysProp.isBlank()) {
            return sysProp;
        }
        String envVar = System.getenv("API_BASE_URL");
        if (envVar != null && !envVar.isBlank()) {
            return envVar;
        }
        return properties.getProperty("api.base.url", "http://localhost:8080");
    }

    /**
     * Returns the path prefix for all calculator API endpoints.
     */
    public String getApiPath() {
        return properties.getProperty("api.path", "/api/v1");
    }

    /**
     * Returns the maximum allowed response time in milliseconds.
     */
    public int getResponseTimeoutMs() {
        String value = properties.getProperty("api.response.timeout.ms", "2000");
        return Integer.parseInt(value);
    }

    /**
     * Returns whether request/response logging is enabled.
     */
    public boolean isLoggingEnabled() {
        String value = properties.getProperty("api.logging.enabled", "true");
        return Boolean.parseBoolean(value);
    }
}
