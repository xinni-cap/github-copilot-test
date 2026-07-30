package com.example.calculator.model;

/**
 * Supported arithmetic operations.
 *
 * <p>Uses a Java enum with switch expressions in the service layer for
 * exhaustive, compile-time-checked dispatch.
 */
public enum Operation {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE;

    /** Parse operation from a human-readable string (case-insensitive). */
    public static Operation fromString(String value) {
        return switch (value.toUpperCase()) {
            case "ADD", "+" -> ADD;
            case "SUBTRACT", "-" -> SUBTRACT;
            case "MULTIPLY", "*", "X" -> MULTIPLY;
            case "DIVIDE", "/" -> DIVIDE;
            default -> throw new IllegalArgumentException(
                    "Unknown operation: '%s'. Supported values: ADD, SUBTRACT, MULTIPLY, DIVIDE"
                            .formatted(value));
        };
    }
}
