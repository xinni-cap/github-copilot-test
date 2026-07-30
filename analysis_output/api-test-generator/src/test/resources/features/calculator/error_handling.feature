@calculator @error-handling @negative-tests
Feature: Error Handling
  As a user of the Calculator API
  I want to receive meaningful error messages
  So that I can understand why my request failed

  Background:
    Given the Calculator API is available

  Scenario: Divide by zero returns error
    When I send a calculation request with num1 "10", num2 "0", and operation "Divide"
    Then the response status code should be 400
    And the error message should be "Division by zero is not allowed."
    And the response should contain error code "DIVISION_BY_ZERO"

  Scenario: Missing num1 field returns validation error
    When I send a calculation request without "num1"
    Then the response status code should be 400
    And the error message should contain "num1"

  Scenario: Missing num2 field returns validation error
    When I send a calculation request without "num2"
    Then the response status code should be 400
    And the error message should contain "num2"

  Scenario: Missing operation field returns validation error
    When I send a calculation request without "operation"
    Then the response status code should be 400
    And the error message should contain "operation"

  Scenario: Invalid operation returns error
    When I send a calculation request with num1 "10", num2 "5", and operation "Modulo"
    Then the response status code should be 400
    And the error message should contain "Invalid operation"

  Scenario: Non-numeric num1 value returns validation error
    When I send a raw calculation request with body:
      """
      {
        "num1": "not-a-number",
        "num2": 5.0,
        "operation": "Add"
      }
      """
    Then the response status code should be 400

  Scenario: Non-numeric num2 value returns validation error
    When I send a raw calculation request with body:
      """
      {
        "num1": 10.0,
        "num2": "not-a-number",
        "operation": "Add"
      }
      """
    Then the response status code should be 400

  Scenario: Empty request body returns error
    When I send an empty calculation request
    Then the response status code should be 400

  Scenario: Request with null operation returns error
    When I send a raw calculation request with body:
      """
      {
        "num1": 10.0,
        "num2": 5.0,
        "operation": null
      }
      """
    Then the response status code should be 400
