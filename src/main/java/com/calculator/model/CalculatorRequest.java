package com.calculator.model;

import jakarta.validation.constraints.NotNull;

/**
 * Immutable record representing a calculator operation request.
 */
public record CalculatorRequest(
        @NotNull Double firstNumber,
        @NotNull Double secondNumber,
        @NotNull String operation) {
}
