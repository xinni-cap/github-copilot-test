package com.example.calculator.model;

/**
 * Immutable record that represents the result of a single calculation.
 *
 * <p>Mirrors the "computation details" expanded panel from the original Python
 * Streamlit app.
 *
 * @param firstNumber  the left-hand operand used in the calculation
 * @param secondNumber the right-hand operand used in the calculation
 * @param operation    the operation that was applied
 * @param result       the numeric result
 * @param expression   a human-readable string such as {@code "3.0 + 4.0 = 7.0"}
 */
public record CalculationResult(
        double firstNumber,
        double secondNumber,
        Operation operation,
        double result,
        String expression) {

    /**
     * Convenience factory that builds a {@code CalculationResult} and
     * pre-formats the {@code expression} field.
     */
    public static CalculationResult of(double a, double b, Operation op, double result) {
        String expression = "%s %s %s = %s".formatted(a, op.symbol(), b, result);
        return new CalculationResult(a, b, op, result, expression);
    }
}
