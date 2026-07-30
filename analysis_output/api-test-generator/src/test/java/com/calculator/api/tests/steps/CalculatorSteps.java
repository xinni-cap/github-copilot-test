package com.calculator.api.tests.steps;

import com.calculator.api.tests.client.ApiClient;
import com.calculator.api.tests.context.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Step definitions for all calculator-related Cucumber scenarios.
 * <p>
 * Covers normal operations, edge cases, error handling, and multi-step
 * workflow scenarios.  Shared state is exchanged through {@link TestContext}
 * which PicoContainer injects into every step class.
 * </p>
 */
public class CalculatorSteps {

    private static final Logger LOG = LoggerFactory.getLogger(CalculatorSteps.class);

    private final TestContext context;
    private final ApiClient apiClient;

    /** PicoContainer-injected constructor. */
    public CalculatorSteps(TestContext context, ApiClient apiClient) {
        this.context = context;
        this.apiClient = apiClient;
    }

    // ---- Given steps ---------------------------------------------------------

    @Given("the Calculator API is running")
    public void theCalculatorApiIsRunning() {
        LOG.info("Verifying Calculator API availability");
        // Connectivity will be validated naturally on the first real request;
        // a GET /operations serves as a lightweight health-check.
    }

    @Given("the API base URL is configured")
    public void theApiBaseUrlIsConfigured() {
        LOG.info("API base URL ready");
    }

    @Given("I have the first number {double}")
    public void iHaveTheFirstNumber(double number) {
        context.setRequestField("firstNumber", number);
    }

    @Given("I have the second number {double}")
    public void iHaveTheSecondNumber(double number) {
        context.setRequestField("secondNumber", number);
    }

    @And("the operation is {string}")
    public void theOperationIs(String operation) {
        context.setRequestField("operation", operation);
    }

    @Given("I have a calculation request without an operation")
    public void iHaveACalculationRequestWithoutAnOperation() {
        context.setRequestField("firstNumber", 10.0);
        context.setRequestField("secondNumber", 5.0);
        // "operation" intentionally omitted
    }

    @Given("I have a calculation request without a first number")
    public void iHaveACalculationRequestWithoutAFirstNumber() {
        context.setRequestField("secondNumber", 5.0);
        context.setRequestField("operation", "ADD");
        // "firstNumber" intentionally omitted
    }

    @Given("I send a raw request body:")
    public void iSendARawRequestBody(String rawJson) {
        context.setRawRequestBody(rawJson);
    }

    // ---- When steps ----------------------------------------------------------

    @When("I perform the {string} operation")
    public void iPerformTheOperation(String operation) {
        context.setRequestField("operation", operation);
        LOG.info("Performing {} operation with request: {}", operation, context.getCalculationRequest());
        context.setLastResponse(apiClient.calculate(context.getCalculationRequest()));
    }

    @When("I perform the {string} operation with {double} and {double}")
    public void iPerformTheOperationWithAndNumbers(String operation, double firstNumber, double secondNumber) {
        context.getCalculationRequest().clear();
        context.setRequestField("firstNumber", firstNumber);
        context.setRequestField("secondNumber", secondNumber);
        context.setRequestField("operation", operation);
        context.setLastResponse(apiClient.calculate(context.getCalculationRequest()));
        Double result = context.getLastResponse().jsonPath().getDouble("result");
        context.setChainedResult(result);
    }

    @When("I submit the calculation request")
    public void iSubmitTheCalculationRequest() {
        LOG.info("Submitting calculation request: {}", context.getCalculationRequest());
        context.setLastResponse(apiClient.calculate(context.getCalculationRequest()));
    }

    @When("I submit the raw calculation request")
    public void iSubmitTheRawCalculationRequest() {
        LOG.info("Submitting raw calculation request");
        context.setLastResponse(apiClient.calculateRaw(context.getRawRequestBody()));
    }

    @When("I request the list of supported operations")
    public void iRequestTheListOfSupportedOperations() {
        context.setLastResponse(apiClient.getOperations());
    }

    // ---- Then steps ----------------------------------------------------------

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatus) {
        int actualStatus = context.getLastResponse().getStatusCode();
        assertThat(actualStatus)
                .as("Expected HTTP status %d but got %d. Response: %s",
                        expectedStatus, actualStatus, context.getLastResponse().asString())
                .isEqualTo(expectedStatus);
    }

    @Then("the result should be {double}")
    public void theResultShouldBe(double expected) {
        Double actual = context.getLastResponse().jsonPath().getDouble("result");
        assertThat(actual)
                .as("Expected result to be %s but was %s", expected, actual)
                .isEqualTo(expected, within(1e-6));
        context.setChainedResult(actual);
    }

    @Then("the result should be approximately {double}")
    public void theResultShouldBeApproximately(double expected) {
        Double actual = context.getLastResponse().jsonPath().getDouble("result");
        assertThat(actual)
                .as("Expected result to be approximately %s but was %s", expected, actual)
                .isCloseTo(expected, within(0.001));
        context.setChainedResult(actual);
    }

    @Then("the expression should be {string}")
    public void theExpressionShouldBe(String expected) {
        String actual = context.getLastResponse().jsonPath().getString("expression");
        assertThat(actual)
                .as("Expected expression '%s' but got '%s'", expected, actual)
                .isEqualTo(expected);
    }

    @Then("the error message should contain {string}")
    public void theErrorMessageShouldContain(String fragment) {
        String body = context.getLastResponse().asString();
        assertThat(body)
                .as("Expected error body to contain '%s' but was: %s", fragment, body)
                .containsIgnoringCase(fragment);
    }

    @Then("the operations list should contain {string}")
    public void theOperationsListShouldContain(String operation) {
        String body = context.getLastResponse().asString();
        assertThat(body)
                .as("Expected operations list to contain '%s'", operation)
                .contains(operation);
    }

    @Then("the response body should contain field {string}")
    public void theResponseBodyShouldContainField(String fieldName) {
        Object value = context.getLastResponse().jsonPath().get(fieldName);
        assertThat(value)
                .as("Expected response body to contain field '%s'", fieldName)
                .isNotNull();
    }

    @Then("the response time should be less than {int} milliseconds")
    public void theResponseTimeShouldBeLessThan(int maxMs) {
        long responseTimeMs = context.getLastResponse().getTime();
        assertThat(responseTimeMs)
                .as("Expected response time < %d ms but was %d ms", maxMs, responseTimeMs)
                .isLessThan(maxMs);
    }

    @Then("the response content type should be {string}")
    public void theResponseContentTypeShouldBe(String expectedContentType) {
        String actual = context.getLastResponse().getContentType();
        assertThat(actual)
                .as("Expected content type '%s' but got '%s'", expectedContentType, actual)
                .contains(expectedContentType);
    }
}
