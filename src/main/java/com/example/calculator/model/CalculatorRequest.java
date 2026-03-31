package com.example.calculator.model;

import jakarta.validation.constraints.NotNull;

/**
 * Represents an incoming calculator request containing two operands
 * and the arithmetic operation to perform.
 */
public class CalculatorRequest {

    @NotNull(message = "First number is required")
    private Double num1;

    @NotNull(message = "Second number is required")
    private Double num2;

    @NotNull(message = "Operation is required")
    private String operation;

    public CalculatorRequest() {}

    public CalculatorRequest(Double num1, Double num2, String operation) {
        this.num1 = num1;
        this.num2 = num2;
        this.operation = operation;
    }

    public Double getNum1() { return num1; }
    public void setNum1(Double num1) { this.num1 = num1; }

    public Double getNum2() { return num2; }
    public void setNum2(Double num2) { this.num2 = num2; }

    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }
}
