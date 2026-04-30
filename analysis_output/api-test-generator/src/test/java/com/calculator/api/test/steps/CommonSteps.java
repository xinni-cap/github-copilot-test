package com.calculator.api.test.steps;

import com.calculator.api.test.client.CalculatorApiClient;
import com.calculator.api.test.context.TestContext;
import io.cucumber.java.en.Given;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for common / shared Cucumber steps such as pre-conditions
 * and connectivity checks.
 */
public class CommonSteps {

    private static final Logger log = LoggerFactory.getLogger(CommonSteps.class);

    private final TestContext context;
    private final CalculatorApiClient apiClient;

    public CommonSteps(TestContext context) {
        this.context = context;
        this.apiClient = new CalculatorApiClient();
    }

    // -----------------------------------------------------------------------
    // Given steps
    // -----------------------------------------------------------------------

    @Given("the Calculator API is available")
    public void theCalculatorApiIsAvailable() {
        try {
            var response = apiClient.healthCheck();
            log.info("Health check responded with status {}", response.getStatusCode());
            // Accept 200 (up) or 503 (degraded but responding)
            assertThat(response.getStatusCode())
                    .as("Calculator API health check failed – is the service running?")
                    .isIn(200, 503);
        } catch (Exception e) {
            log.warn("Health endpoint unavailable ({}). Tests will continue; individual steps may fail.", e.getMessage());
        }
    }
}
