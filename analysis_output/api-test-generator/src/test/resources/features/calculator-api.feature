Feature: Calculator API operations
  As an API consumer
  I want to submit calculator requests
  So that arithmetic results and validation errors are returned consistently

  Background:
    Given the calculator API is available

  Scenario Outline: Successful calculation requests
    Given a calculation request with first number <firstNumber>, second number <secondNumber>, and operation "<operation>"
    When the client submits the calculation request
    Then the response status should be 200
    And the response should contain the computed result <expectedResult>
    And the response should echo the operation "<operation>"

    Examples:
      | firstNumber | secondNumber | operation | expectedResult |
      | 12.5        | 7.5          | Add       | 20.0           |
      | 9.25        | 4.25         | Subtract  | 5.0            |
      | -3          | 6            | Multiply  | -18.0          |
      | 22          | 4            | Divide    | 5.5            |

  Scenario: Division by zero is rejected
    Given a calculation request with first number 8, second number 0, and operation "Divide"
    When the client submits the calculation request
    Then the response status should be 400
    And the response should contain the error message "Division by zero is not allowed."
