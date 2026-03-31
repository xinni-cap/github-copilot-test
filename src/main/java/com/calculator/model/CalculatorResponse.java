package com.calculator.model;

/**
 * Immutable record representing the result of a calculator operation.
 * Use {@link #success} or {@link #error} factory methods to construct instances.
 */
public record CalculatorResponse(
        Double firstNumber,
        Double secondNumber,
        String operation,
        String symbol,
        Double result,
        String errorMessage) {

    public static CalculatorResponse success(Double firstNumber, Double secondNumber,
                                             String operation, String symbol, Double result) {
        return new CalculatorResponse(firstNumber, secondNumber, operation, symbol, result, null);
    }

    public static CalculatorResponse error(String errorMessage) {
        return new CalculatorResponse(null, null, null, null, null, errorMessage);
    }

    /** Returns {@code true} when this response represents an error. */
    public boolean isError() {
        return errorMessage != null && !errorMessage.isEmpty();
    }
}
