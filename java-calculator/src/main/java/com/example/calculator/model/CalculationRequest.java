package com.example.calculator.model;

import jakarta.validation.constraints.NotNull;

/**
 * Incoming calculation request.
 *
 * <p>Uses a Java 16+ {@code record} for an immutable, concise data carrier.
 *
 * @param firstNumber  the first operand
 * @param secondNumber the second operand
 * @param operation    the arithmetic operation (ADD, SUBTRACT, MULTIPLY, DIVIDE)
 */
public record CalculationRequest(
        @NotNull(message = "firstNumber must not be null") Double firstNumber,
        @NotNull(message = "secondNumber must not be null") Double secondNumber,
        @NotNull(message = "operation must not be null") String operation) {}
