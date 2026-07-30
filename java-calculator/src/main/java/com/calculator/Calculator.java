package com.calculator;

/**
 * Core calculator logic.
 *
 * <p>This class is intentionally stateless – every method is {@code static}.
 * The {@link #calculate(double, double, Operation)} method uses a Java 21
 * switch expression to dispatch to the correct arithmetic, returning a
 * {@link CalculationResult} (either {@link CalculationResult.Success} or
 * {@link CalculationResult.DivisionByZero}) without ever throwing an
 * exception for normal "bad input" conditions.
 */
public final class Calculator {

    // Utility class – prevent instantiation
    private Calculator() {}

    /**
     * Performs the requested arithmetic operation on the two operands.
     *
     * @param firstNumber  left-hand operand
     * @param secondNumber right-hand operand
     * @param operation    the operation to perform
     * @return a {@link CalculationResult} describing the outcome
     */
    public static CalculationResult calculate(
            double firstNumber, double secondNumber, Operation operation) {

        // Guard: check for division-by-zero before performing any arithmetic
        if (operation == Operation.DIVIDE && secondNumber == 0.0) {
            return new CalculationResult.DivisionByZero(firstNumber);
        }

        // Switch expression over an enum – exhaustive without a default arm
        double result = switch (operation) {
            case ADD      -> firstNumber + secondNumber;
            case SUBTRACT -> firstNumber - secondNumber;
            case MULTIPLY -> firstNumber * secondNumber;
            case DIVIDE   -> firstNumber / secondNumber;
        };

        return new CalculationResult.Success(firstNumber, secondNumber, operation, result);
    }

    /**
     * Convenience overload that accepts the operation as a string.
     *
     * @param firstNumber  left-hand operand
     * @param secondNumber right-hand operand
     * @param operationStr operation label or name (case-insensitive)
     * @return a {@link CalculationResult} describing the outcome
     * @throws IllegalArgumentException if {@code operationStr} is not recognised
     */
    public static CalculationResult calculate(
            double firstNumber, double secondNumber, String operationStr) {
        return calculate(firstNumber, secondNumber, Operation.parse(operationStr));
    }
}
