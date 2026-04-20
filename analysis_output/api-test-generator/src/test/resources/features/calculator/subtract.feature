@calculator @subtraction
Feature: Subtraction Operation
  As a user of the Calculator API
  I want to subtract one number from another
  So that I can get their difference

  Background:
    Given the Calculator API is available

  Scenario: Subtract two positive integers
    When I send a calculation request with num1 "10", num2 "3", and operation "Subtract"
    Then the response status code should be 200
    And the result should be 7.0
    And the response should contain expression "10.0 - 3.0 = 7.0"

  Scenario: Subtract a larger number from a smaller number
    When I send a calculation request with num1 "5", num2 "10", and operation "Subtract"
    Then the response status code should be 200
    And the result should be -5.0

  Scenario: Subtract a negative number
    When I send a calculation request with num1 "10", num2 "-4", and operation "Subtract"
    Then the response status code should be 200
    And the result should be 14.0

  Scenario: Subtract two decimal numbers
    When I send a calculation request with num1 "9.75", num2 "3.25", and operation "Subtract"
    Then the response status code should be 200
    And the result should be 6.5

  Scenario: Subtract zero from a number
    When I send a calculation request with num1 "100", num2 "0", and operation "Subtract"
    Then the response status code should be 200
    And the result should be 100.0

  Scenario: Subtract a number from itself
    When I send a calculation request with num1 "77", num2 "77", and operation "Subtract"
    Then the response status code should be 200
    And the result should be 0.0

  Scenario Outline: Subtract various number pairs
    When I send a calculation request with num1 "<num1>", num2 "<num2>", and operation "Subtract"
    Then the response status code should be 200
    And the result should be <expected>

    Examples:
      | num1  | num2  | expected |
      | 500   | 300   | 200.0    |
      | 1.5   | 0.5   | 1.0      |
      | -10   | -5    | -5.0     |
      | 0     | 50    | -50.0    |
