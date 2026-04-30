@calculator @workflow @integration
Feature: Calculator Business Workflow
  As a user of the Calculator API
  I want to perform a sequence of arithmetic operations
  So that I can complete complex calculations step by step

  Background:
    Given the Calculator API is available

  Scenario: Complete arithmetic workflow - all four operations
    When I send a calculation request with num1 "100", num2 "50", and operation "Add"
    Then the response status code should be 200
    And the result should be 150.0

    When I send a calculation request with num1 "150", num2 "30", and operation "Subtract"
    Then the response status code should be 200
    And the result should be 120.0

    When I send a calculation request with num1 "120", num2 "3", and operation "Multiply"
    Then the response status code should be 200
    And the result should be 360.0

    When I send a calculation request with num1 "360", num2 "12", and operation "Divide"
    Then the response status code should be 200
    And the result should be 30.0

  Scenario: Tax calculation workflow
    # Base price
    When I send a calculation request with num1 "199.99", num2 "0.08", and operation "Multiply"
    Then the response status code should be 200
    And the result should be approximately 15.999

    # Price + tax
    When I send a calculation request with num1 "199.99", num2 "15.9992", and operation "Add"
    Then the response status code should be 200
    And the result should be approximately 215.989

  Scenario: Split bill calculation workflow
    # Total bill
    When I send a calculation request with num1 "85.50", num2 "14.50", and operation "Add"
    Then the response status code should be 200
    And the result should be 100.0

    # Split equally between 4 people
    When I send a calculation request with num1 "100", num2 "4", and operation "Divide"
    Then the response status code should be 200
    And the result should be 25.0

  Scenario: Interest calculation workflow
    # Principal * rate
    When I send a calculation request with num1 "1000", num2 "0.05", and operation "Multiply"
    Then the response status code should be 200
    And the result should be 50.0

    # Interest for 3 years
    When I send a calculation request with num1 "50", num2 "3", and operation "Multiply"
    Then the response status code should be 200
    And the result should be 150.0

    # Total amount
    When I send a calculation request with num1 "1000", num2 "150", and operation "Add"
    Then the response status code should be 200
    And the result should be 1150.0

  Scenario: Error recovery in workflow
    # First operation succeeds
    When I send a calculation request with num1 "20", num2 "5", and operation "Add"
    Then the response status code should be 200
    And the result should be 25.0

    # Second operation fails (division by zero)
    When I send a calculation request with num1 "25", num2 "0", and operation "Divide"
    Then the response status code should be 400
    And the error message should be "Division by zero is not allowed."

    # Third operation succeeds after the error
    When I send a calculation request with num1 "25", num2 "5", and operation "Divide"
    Then the response status code should be 200
    And the result should be 5.0
