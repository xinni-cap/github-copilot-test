package com.calculator.api.test.steps;

import com.calculator.api.test.builders.CalculationRequestBuilder;
import com.calculator.api.test.client.CalculatorApiClient;
import com.calculator.api.test.context.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Cucumber step definitions for all Calculator API scenarios.
 *
 * <p>PicoContainer injects a shared {@link TestContext} instance so that state
 * (e.g. the last HTTP response) can be passed between {@code When} and {@code Then}
 * steps without static fields.
 */
public class CalculationSteps {

    private static final Logger log = LoggerFactory.getLogger(CalculationSteps.class);

    private final TestContext context;
    private final CalculatorApiClient apiClient;

    public CalculationSteps(TestContext context) {
        this.context = context;
        this.apiClient = new CalculatorApiClient();
    }

    // -----------------------------------------------------------------------
    // When steps – sending requests
    // -----------------------------------------------------------------------

    @When("I send a calculation request with num1 {string}, num2 {string}, and operation {string}")
    public void iSendACalculationRequest(String num1, String num2, String operation) {
        String body = new CalculationRequestBuilder()
                .withNum1(Double.parseDouble(num1))
                .withNum2(Double.parseDouble(num2))
                .withOperation(operation)
                .build();

        log.debug("Sending calculation request: {}", body);
        context.setLastRequestBody(body);

        Response response = apiClient.calculate(body);
        context.setLastResponse(response);
    }

    @When("I send a calculation request without {string}")
    public void iSendACalculationRequestWithout(String missingField) {
        CalculationRequestBuilder builder = new CalculationRequestBuilder();

        if (!"num1".equals(missingField)) {
            builder.withNum1(10.0);
        }
        if (!"num2".equals(missingField)) {
            builder.withNum2(5.0);
        }
        if (!"operation".equals(missingField)) {
            builder.withOperation("Add");
        }

        String body = builder.build();
        log.debug("Sending incomplete request (missing {}): {}", missingField, body);
        context.setLastRequestBody(body);

        Response response = apiClient.calculate(body);
        context.setLastResponse(response);
    }

    @When("I send a raw calculation request with body:")
    public void iSendARawCalculationRequestWithBody(String rawBody) {
        log.debug("Sending raw request body: {}", rawBody);
        context.setLastRequestBody(rawBody);

        Response response = apiClient.calculate(rawBody);
        context.setLastResponse(response);
    }

    @When("I send an empty calculation request")
    public void iSendAnEmptyCalculationRequest() {
        log.debug("Sending empty calculation request");
        Response response = apiClient.calculateEmpty();
        context.setLastResponse(response);
    }

    // -----------------------------------------------------------------------
    // Then / And steps – asserting responses
    // -----------------------------------------------------------------------

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
        int actual = context.getLastStatusCode();
        assertThat(actual)
                .as("Expected HTTP status %d but got %d. Body: %s",
                        expectedStatusCode, actual, context.getLastResponse().asString())
                .isEqualTo(expectedStatusCode);
    }

    @And("the result should be {double}")
    public void theResultShouldBe(double expected) {
        double actual = extractResult();
        assertThat(actual)
                .as("Calculation result mismatch")
                .isCloseTo(expected, within(1e-9));
    }

    @And("the result should be approximately {double}")
    public void theResultShouldBeApproximately(double expected) {
        double actual = extractResult();
        assertThat(actual)
                .as("Calculation result (approx) mismatch")
                .isCloseTo(expected, within(0.01));
    }

    @And("the response should contain expression {string}")
    public void theResponseShouldContainExpression(String expectedExpression) {
        String body = context.getLastResponse().asString();
        assertThat(body)
                .as("Response body should contain expression '%s'", expectedExpression)
                .contains(expectedExpression);
    }

    @And("the error message should be {string}")
    public void theErrorMessageShouldBe(String expectedMessage) {
        String body = context.getLastResponse().asString();
        assertThat(body)
                .as("Expected error message '%s' not found in response body: %s",
                        expectedMessage, body)
                .contains(expectedMessage);
    }

    @And("the error message should contain {string}")
    public void theErrorMessageShouldContain(String expectedFragment) {
        String body = context.getLastResponse().asString();
        assertThat(body)
                .as("Expected error message fragment '%s' not found in response body: %s",
                        expectedFragment, body)
                .containsIgnoringCase(expectedFragment);
    }

    @And("the response should contain error code {string}")
    public void theResponseShouldContainErrorCode(String expectedCode) {
        String body = context.getLastResponse().asString();
        assertThat(body)
                .as("Expected error code '%s' not found in response body: %s",
                        expectedCode, body)
                .contains(expectedCode);
    }

    // -----------------------------------------------------------------------
    // Internal helpers
    // -----------------------------------------------------------------------

    private double extractResult() {
        Response response = context.getLastResponse();
        Double result = response.jsonPath().get("result");
        assertThat(result)
                .as("Response body did not contain a 'result' field. Body: %s", response.asString())
                .isNotNull();
        return result;
    }
}
