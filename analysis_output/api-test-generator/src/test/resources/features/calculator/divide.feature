@calculator @division
Feature: Division Operation
  As a user of the Calculator API
  I want to divide one number by another
  So that I can get their quotient

  Background:
    Given the Calculator API is available

  Scenario: Divide two positive integers
    When I send a calculation request with num1 "10", num2 "2", and operation "Divide"
    Then the response status code should be 200
    And the result should be 5.0
    And the response should contain expression "10.0 ÷ 2.0 = 5.0"

  Scenario: Divide with a decimal result
    When I send a calculation request with num1 "10", num2 "3", and operation "Divide"
    Then the response status code should be 200
    And the result should be approximately 3.333

  Scenario: Divide a negative number by a positive number
    When I send a calculation request with num1 "-20", num2 "4", and operation "Divide"
    Then the response status code should be 200
    And the result should be -5.0

  Scenario: Divide a positive number by a negative number
    When I send a calculation request with num1 "20", num2 "-5", and operation "Divide"
    Then the response status code should be 200
    And the result should be -4.0

  Scenario: Divide two negative numbers
    When I send a calculation request with num1 "-15", num2 "-3", and operation "Divide"
    Then the response status code should be 200
    And the result should be 5.0

  Scenario: Divide zero by a number
    When I send a calculation request with num1 "0", num2 "7", and operation "Divide"
    Then the response status code should be 200
    And the result should be 0.0

  Scenario: Divide by one
    When I send a calculation request with num1 "55", num2 "1", and operation "Divide"
    Then the response status code should be 200
    And the result should be 55.0

  Scenario: Divide two decimal numbers
    When I send a calculation request with num1 "7.5", num2 "2.5", and operation "Divide"
    Then the response status code should be 200
    And the result should be 3.0

  Scenario Outline: Divide various number pairs
    When I send a calculation request with num1 "<num1>", num2 "<num2>", and operation "Divide"
    Then the response status code should be 200
    And the result should be <expected>

    Examples:
      | num1  | num2  | expected |
      | 100   | 4     | 25.0     |
      | 9     | 3     | 3.0      |
      | 1     | 2     | 0.5      |
      | -100  | 10    | -10.0    |
