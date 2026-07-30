Feature: Calculator Basic Operations
  As a user of the Calculator API
  I want to perform arithmetic operations
  So that I can get accurate mathematical results

  Background:
    Given the Calculator API is running
    And the API base URL is configured

  @smoke @addition
  Scenario: Add two positive numbers
    Given I have the first number 10
    And I have the second number 5
    When I perform the "ADD" operation
    Then the response status code should be 200
    And the result should be 15.0
    And the expression should be "10.0 + 5.0 = 15.0"

  @addition
  Scenario: Add two decimal numbers
    Given I have the first number 3.5
    And I have the second number 2.7
    When I perform the "ADD" operation
    Then the response status code should be 200
    And the result should be 6.2

  @addition
  Scenario: Add a positive and a negative number
    Given I have the first number 10
    And I have the second number -3
    When I perform the "ADD" operation
    Then the response status code should be 200
    And the result should be 7.0

  @addition
  Scenario: Add two negative numbers
    Given I have the first number -4
    And I have the second number -6
    When I perform the "ADD" operation
    Then the response status code should be 200
    And the result should be -10.0

  @smoke @subtraction
  Scenario: Subtract two positive numbers
    Given I have the first number 10
    And I have the second number 4
    When I perform the "SUBTRACT" operation
    Then the response status code should be 200
    And the result should be 6.0
    And the expression should be "10.0 - 4.0 = 6.0"

  @subtraction
  Scenario: Subtract a larger number from a smaller number
    Given I have the first number 5
    And I have the second number 12
    When I perform the "SUBTRACT" operation
    Then the response status code should be 200
    And the result should be -7.0

  @subtraction
  Scenario: Subtract decimal numbers
    Given I have the first number 9.9
    And I have the second number 4.4
    When I perform the "SUBTRACT" operation
    Then the response status code should be 200
    And the result should be 5.5

  @smoke @multiplication
  Scenario: Multiply two positive numbers
    Given I have the first number 6
    And I have the second number 7
    When I perform the "MULTIPLY" operation
    Then the response status code should be 200
    And the result should be 42.0
    And the expression should be "6.0 × 7.0 = 42.0"

  @multiplication
  Scenario: Multiply a number by zero
    Given I have the first number 99
    And I have the second number 0
    When I perform the "MULTIPLY" operation
    Then the response status code should be 200
    And the result should be 0.0

  @multiplication
  Scenario: Multiply two negative numbers
    Given I have the first number -3
    And I have the second number -4
    When I perform the "MULTIPLY" operation
    Then the response status code should be 200
    And the result should be 12.0

  @multiplication
  Scenario: Multiply a positive and a negative number
    Given I have the first number 5
    And I have the second number -3
    When I perform the "MULTIPLY" operation
    Then the response status code should be 200
    And the result should be -15.0

  @smoke @division
  Scenario: Divide two positive numbers
    Given I have the first number 20
    And I have the second number 4
    When I perform the "DIVIDE" operation
    Then the response status code should be 200
    And the result should be 5.0
    And the expression should be "20.0 ÷ 4.0 = 5.0"

  @division
  Scenario: Divide with a decimal result
    Given I have the first number 10
    And I have the second number 3
    When I perform the "DIVIDE" operation
    Then the response status code should be 200
    And the result should be approximately 3.333

  @division
  Scenario: Divide a negative number by a positive number
    Given I have the first number -15
    And I have the second number 3
    When I perform the "DIVIDE" operation
    Then the response status code should be 200
    And the result should be -5.0

  @batch @smoke
  Scenario Outline: Perform multiple arithmetic operations
    Given I have the first number <num1>
    And I have the second number <num2>
    When I perform the "<operation>" operation
    Then the response status code should be 200
    And the result should be <expected_result>

    Examples:
      | num1 | num2 | operation  | expected_result |
      | 100  | 50   | ADD        | 150.0           |
      | 100  | 50   | SUBTRACT   | 50.0            |
      | 100  | 50   | MULTIPLY   | 5000.0          |
      | 100  | 50   | DIVIDE     | 2.0             |
      | 0    | 0    | ADD        | 0.0             |
      | -10  | -10  | ADD        | -20.0           |
      | 1    | 1000 | MULTIPLY   | 1000.0          |

  @operations
  Scenario: Get list of supported operations
    When I request the list of supported operations
    Then the response status code should be 200
    And the operations list should contain "ADD"
    And the operations list should contain "SUBTRACT"
    And the operations list should contain "MULTIPLY"
    And the operations list should contain "DIVIDE"
