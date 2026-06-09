package com.example.calculator.api.steps;

import com.example.calculator.api.support.TestContext;
import com.example.calculator.api.support.TestDataBuilder;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;

public class CalculatorApiSteps {
    private final TestContext testContext = new TestContext();

    @Given("the calculator API is available")
    public void theCalculatorApiIsAvailable() {
        testContext.ensureApiIsAvailable();
    }

    @Given("a calculation request with first number {bigdecimal}, second number {bigdecimal}, and operation {string}")
    public void aCalculationRequest(BigDecimal firstNumber, BigDecimal secondNumber, String operation) {
        testContext.setRequest(TestDataBuilder.calculationRequest(firstNumber, secondNumber, operation));
    }

    @When("the client submits the calculation request")
    public void theClientSubmitsTheCalculationRequest() {
        testContext.setResponse(testContext.apiClient().calculate(testContext.request()));
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int statusCode) {
        Assertions.assertEquals(statusCode, testContext.response().statusCode());
    }

    @Then("the response should contain the computed result {bigdecimal}")
    public void theResponseShouldContainTheComputedResult(BigDecimal expectedResult) {
        BigDecimal actualResult = testContext.response().jsonPath().getObject("result", BigDecimal.class);
        Assertions.assertEquals(0, expectedResult.compareTo(actualResult.stripTrailingZeros()));
    }

    @Then("the response should echo the operation {string}")
    public void theResponseShouldEchoTheOperation(String operation) {
        Assertions.assertEquals(operation, testContext.response().jsonPath().getString("operation"));
    }

    @Then("the response should contain the error message {string}")
    public void theResponseShouldContainTheErrorMessage(String message) {
        Assertions.assertEquals(message, testContext.response().jsonPath().getString("error"));
    }
}
