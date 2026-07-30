package com.calculator.api.tests.steps;

import com.calculator.api.tests.context.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cucumber hooks that run before and after each scenario.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Reset the shared {@link TestContext} before each scenario so state
 *       does not leak between tests.</li>
 *   <li>Log scenario start and result for easier debugging.</li>
 *   <li>Attach response body to the Cucumber report on failure.</li>
 * </ul>
 * </p>
 */
public class CommonSteps {

    private static final Logger LOG = LoggerFactory.getLogger(CommonSteps.class);

    private final TestContext context;

    public CommonSteps(TestContext context) {
        this.context = context;
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        LOG.info("▶ Starting scenario: [{}] {}", scenario.getId(), scenario.getName());
        context.reset();
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed() && context.getLastResponse() != null) {
            String responseBody = context.getLastResponse().asString();
            LOG.error("Scenario FAILED. Last response body:\n{}", responseBody);
            scenario.attach(responseBody.getBytes(), "application/json", "Last API Response");
        }

        if (scenario.isFailed()) {
            LOG.error("✖ Scenario FAILED: {}", scenario.getName());
        } else {
            LOG.info("✔ Scenario PASSED: {}", scenario.getName());
        }
    }
}
