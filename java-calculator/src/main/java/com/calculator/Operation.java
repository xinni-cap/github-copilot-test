package com.calculator;

/**
 * The four arithmetic operations supported by the calculator.
 *
 * <p>Using an {@code enum} here keeps the set of valid operations
 * closed and lets the compiler warn us if we miss a case in a
 * {@code switch} expression.
 */
public enum Operation {

    ADD("Add", "+"),
    SUBTRACT("Subtract", "-"),
    MULTIPLY("Multiply", "×"),
    DIVIDE("Divide", "÷");

    private final String label;
    private final String symbol;

    Operation(String label, String symbol) {
        this.label = label;
        this.symbol = symbol;
    }

    /** Human-readable name shown in menus. */
    public String label() {
        return label;
    }

    /** Mathematical symbol used in result output. */
    public String symbol() {
        return symbol;
    }

    /**
     * Parses a user-supplied string (case-insensitive) to an {@link Operation}.
     *
     * @param input the raw user input
     * @return the matching {@code Operation}
     * @throws IllegalArgumentException if no operation matches
     */
    public static Operation parse(String input) {
        String normalised = input.strip().toUpperCase();
        for (Operation op : values()) {
            if (op.name().equals(normalised) || op.label().equalsIgnoreCase(input.strip())) {
                return op;
            }
        }
        throw new IllegalArgumentException(
                "Unknown operation: '%s'. Valid options: add, subtract, multiply, divide."
                        .formatted(input));
    }
}
