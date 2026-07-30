package com.example.calculator.model;

import jakarta.validation.constraints.NotNull;

/**
 * Immutable record that carries a single calculation request.
 *
 * <p>Uses Java 21 records for concise, boilerplate-free value objects.
 * Bean Validation annotations are applied directly on the record components.
 *
 * @param firstNumber  the left-hand operand
 * @param secondNumber the right-hand operand
 * @param operation    the arithmetic operation to perform
 */
public record CalculationRequest(
        double firstNumber,
        double secondNumber,
        @NotNull(message = "Operation must not be null") Operation operation) {
}
