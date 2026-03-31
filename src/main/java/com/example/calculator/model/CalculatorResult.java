package com.example.calculator.model;

/**
 * Holds the outcome of a calculation, including the formatted expression
 * and any error message produced during computation.
 */
public class CalculatorResult {

    private Double num1;
    private Double num2;
    private String operation;
    private String symbol;
    private Double result;
    private String errorMessage;

    public CalculatorResult() {}

    // --- Getters & Setters ---

    public Double getNum1() { return num1; }
    public void setNum1(Double num1) { this.num1 = num1; }

    public Double getNum2() { return num2; }
    public void setNum2(Double num2) { this.num2 = num2; }

    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public Double getResult() { return result; }
    public void setResult(Double result) { this.result = result; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public boolean hasError() { return errorMessage != null && !errorMessage.isBlank(); }
}
