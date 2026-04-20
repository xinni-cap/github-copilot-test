@calculator @addition
Feature: Addition Operation
  As a user of the Calculator API
  I want to add two numbers together
  So that I can get their sum

  Background:
    Given the Calculator API is available

  Scenario: Add two positive integers
    When I send a calculation request with num1 "10", num2 "5", and operation "Add"
    Then the response status code should be 200
    And the result should be 15.0
    And the response should contain expression "10.0 + 5.0 = 15.0"

  Scenario: Add two negative numbers
    When I send a calculation request with num1 "-8", num2 "-3", and operation "Add"
    Then the response status code should be 200
    And the result should be -11.0

  Scenario: Add a positive and a negative number
    When I send a calculation request with num1 "15", num2 "-7", and operation "Add"
    Then the response status code should be 200
    And the result should be 8.0

  Scenario: Add two decimal numbers
    When I send a calculation request with num1 "3.5", num2 "1.25", and operation "Add"
    Then the response status code should be 200
    And the result should be 4.75

  Scenario: Add a number and zero
    When I send a calculation request with num1 "42", num2 "0", and operation "Add"
    Then the response status code should be 200
    And the result should be 42.0

  Scenario: Add two zeros
    When I send a calculation request with num1 "0", num2 "0", and operation "Add"
    Then the response status code should be 200
    And the result should be 0.0

  Scenario Outline: Add various number pairs
    When I send a calculation request with num1 "<num1>", num2 "<num2>", and operation "Add"
    Then the response status code should be 200
    And the result should be <expected>

    Examples:
      | num1   | num2   | expected |
      | 100    | 200    | 300.0    |
      | 0.1    | 0.2    | 0.3      |
      | 999    | 1      | 1000.0   |
      | -50    | 50     | 0.0      |
      | 1000   | -1000  | 0.0      |
