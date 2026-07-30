package com.example.calculator.model;

/**
 * The four arithmetic operations supported by the calculator.
 *
 * <p>Each constant carries a human-readable {@code symbol} used when
 * formatting results, mirroring the symbols shown in the original Python app.
 */
public enum Operation {

    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("×"),
    DIVIDE("÷");

    private final String symbol;

    Operation(String symbol) {
        this.symbol = symbol;
    }

    /** Returns the display symbol for this operation (e.g. {@code "+"}, {@code "÷"}). */
    public String symbol() {
        return symbol;
    }
}
