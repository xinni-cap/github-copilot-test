@calculator @multiplication
Feature: Multiplication Operation
  As a user of the Calculator API
  I want to multiply two numbers together
  So that I can get their product

  Background:
    Given the Calculator API is available

  Scenario: Multiply two positive integers
    When I send a calculation request with num1 "6", num2 "7", and operation "Multiply"
    Then the response status code should be 200
    And the result should be 42.0
    And the response should contain expression "6.0 × 7.0 = 42.0"

  Scenario: Multiply a positive and a negative number
    When I send a calculation request with num1 "5", num2 "-3", and operation "Multiply"
    Then the response status code should be 200
    And the result should be -15.0

  Scenario: Multiply two negative numbers
    When I send a calculation request with num1 "-4", num2 "-6", and operation "Multiply"
    Then the response status code should be 200
    And the result should be 24.0

  Scenario: Multiply by zero
    When I send a calculation request with num1 "9999", num2 "0", and operation "Multiply"
    Then the response status code should be 200
    And the result should be 0.0

  Scenario: Multiply by one
    When I send a calculation request with num1 "42", num2 "1", and operation "Multiply"
    Then the response status code should be 200
    And the result should be 42.0

  Scenario: Multiply decimal numbers
    When I send a calculation request with num1 "2.5", num2 "4.0", and operation "Multiply"
    Then the response status code should be 200
    And the result should be 10.0

  Scenario Outline: Multiply various number pairs
    When I send a calculation request with num1 "<num1>", num2 "<num2>", and operation "Multiply"
    Then the response status code should be 200
    And the result should be <expected>

    Examples:
      | num1  | num2  | expected |
      | 10    | 10    | 100.0    |
      | 0.5   | 0.5   | 0.25     |
      | -2    | -2    | 4.0      |
      | 100   | 0.01  | 1.0      |
