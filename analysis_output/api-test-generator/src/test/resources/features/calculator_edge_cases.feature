Feature: Calculator Edge Cases and Error Handling
  As a user of the Calculator API
  I want the API to handle edge cases and invalid inputs gracefully
  So that I receive meaningful error messages

  Background:
    Given the Calculator API is running
    And the API base URL is configured

  @error @division
  Scenario: Divide by zero returns an error
    Given I have the first number 10
    And I have the second number 0
    When I perform the "DIVIDE" operation
    Then the response status code should be 400
    And the error message should contain "Division by zero is not allowed"

  @error @validation
  Scenario: Missing operation field returns validation error
    Given I have a calculation request without an operation
    When I submit the calculation request
    Then the response status code should be 400
    And the error message should contain "operation"

  @error @validation
  Scenario: Missing firstNumber field returns validation error
    Given I have a calculation request without a first number
    When I submit the calculation request
    Then the response status code should be 400
    And the error message should contain "firstNumber"

  @error @validation
  Scenario: Invalid operation value returns error
    Given I have the first number 10
    And I have the second number 5
    And the operation is "MODULO"
    When I submit the calculation request
    Then the response status code should be 400
    And the error message should contain "Unsupported operation"

  @error @validation
  Scenario: Non-numeric value for number field returns error
    Given I send a raw request body:
      """
      {
        "firstNumber": "not-a-number",
        "secondNumber": 5,
        "operation": "ADD"
      }
      """
    When I submit the raw calculation request
    Then the response status code should be 400

  @boundary
  Scenario: Calculate with very large numbers
    Given I have the first number 999999999.99
    And I have the second number 999999999.99
    When I perform the "ADD" operation
    Then the response status code should be 200
    And the result should be 1999999999.98

  @boundary
  Scenario: Calculate with very small decimal numbers
    Given I have the first number 0.000001
    And I have the second number 0.000001
    When I perform the "ADD" operation
    Then the response status code should be 200
    And the result should be 0.000002

  @boundary
  Scenario: Divide resulting in a very small decimal
    Given I have the first number 1
    And I have the second number 1000000
    When I perform the "DIVIDE" operation
    Then the response status code should be 200
    And the result should be approximately 0.000001

  @boundary
  Scenario: Add zero to a number
    Given I have the first number 42
    And I have the second number 0
    When I perform the "ADD" operation
    Then the response status code should be 200
    And the result should be 42.0

  @boundary
  Scenario: Multiply by one
    Given I have the first number 12345.678
    And I have the second number 1
    When I perform the "MULTIPLY" operation
    Then the response status code should be 200
    And the result should be 12345.678

  @response-structure
  Scenario: Successful calculation response has required fields
    Given I have the first number 5
    And I have the second number 3
    When I perform the "ADD" operation
    Then the response status code should be 200
    And the response body should contain field "result"
    And the response body should contain field "firstNumber"
    And the response body should contain field "secondNumber"
    And the response body should contain field "operation"
    And the response body should contain field "expression"

  @performance
  Scenario: API responds within acceptable time limit
    Given I have the first number 100
    And I have the second number 50
    When I perform the "MULTIPLY" operation
    Then the response status code should be 200
    And the response time should be less than 2000 milliseconds

  @content-type
  Scenario: API returns JSON content type
    Given I have the first number 1
    And I have the second number 1
    When I perform the "ADD" operation
    Then the response status code should be 200
    And the response content type should be "application/json"

  @history @workflow
  Scenario: Perform a series of calculations simulating a workflow
    Given the Calculator API is running
    When I perform the "ADD" operation with 100 and 50
    Then the result should be 150.0
    When I perform the "MULTIPLY" operation with 150.0 and 2
    Then the result should be 300.0
    When I perform the "SUBTRACT" operation with 300.0 and 100
    Then the result should be 200.0
    When I perform the "DIVIDE" operation with 200.0 and 4
    Then the result should be 50.0
