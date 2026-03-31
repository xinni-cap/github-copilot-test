package com.calculator;

/**
 * Sealed hierarchy that represents either a successful calculation or a
 * well-typed error.
 *
 * <p>Using a sealed interface means the compiler enforces exhaustive handling
 * in every {@code switch} expression that switches over a
 * {@code CalculationResult}.
 *
 * <h2>Permitted subtypes</h2>
 * <ul>
 *   <li>{@link Success}       – holds the numeric result and full expression.</li>
 *   <li>{@link DivisionByZero} – signals an attempt to divide by zero.</li>
 * </ul>
 */
public sealed interface CalculationResult
        permits CalculationResult.Success, CalculationResult.DivisionByZero {

    /**
     * A successful arithmetic calculation.
     *
     * @param firstNumber  the left-hand operand
     * @param secondNumber the right-hand operand
     * @param operation    the operation that was performed
     * @param result       the numeric result
     */
    record Success(
            double firstNumber,
            double secondNumber,
            Operation operation,
            double result) implements CalculationResult {

        /**
         * Returns a human-readable expression string, e.g.
         * {@code "3.0 + 4.0 = 7.0"}.
         */
        public String expression() {
            return "%s %s %s = %s".formatted(
                    formatNumber(firstNumber),
                    operation.symbol(),
                    formatNumber(secondNumber),
                    formatNumber(result));
        }

        /**
         * Returns a detailed summary suitable for display in a "computation
         * details" section.
         */
        public String details() {
            return """
                    Computation details:
                      first_number  : %s
                      second_number : %s
                      operation     : %s
                      result        : %s"""
                    .formatted(
                            firstNumber,
                            secondNumber,
                            operation.label(),
                            result);
        }

        private static String formatNumber(double value) {
            // Show as integer when there is no fractional part
            return (value == Math.floor(value) && !Double.isInfinite(value))
                    ? String.valueOf((long) value)
                    : String.valueOf(value);
        }
    }

    /**
     * Signals that division by zero was attempted.
     *
     * @param dividend the number that was to be divided
     */
    record DivisionByZero(double dividend) implements CalculationResult {

        /** A user-friendly error message. */
        public String errorMessage() {
            return "Division by zero is not allowed.";
        }
    }
}
