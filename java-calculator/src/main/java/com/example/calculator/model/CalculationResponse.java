package com.example.calculator.model;

/**
 * Outgoing calculation response.
 *
 * <p>Uses a Java 16+ {@code record} for an immutable, concise data carrier.
 *
 * @param firstNumber  the first operand echoed back
 * @param secondNumber the second operand echoed back
 * @param operation    the operation performed
 * @param result       the computed result
 * @param expression   a human-readable expression string, e.g. "3.0 + 4.0 = 7.0"
 */
public record CalculationResponse(
        double firstNumber,
        double secondNumber,
        String operation,
        double result,
        String expression) {}
