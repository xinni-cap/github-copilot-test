package com.example.calculator;

/**
 * Core calculator logic supporting the four basic arithmetic operations.
 *
 * <p>This class is the Java equivalent of the arithmetic performed in the
 * Streamlit {@code app.py} calculator.
 */
public class Calculator {

    /**
     * Adds two numbers.
     *
     * @param a the first operand
     * @param b the second operand
     * @return the sum {@code a + b}
     */
    public double add(double a, double b) {
        return a + b;
    }

    /**
     * Subtracts {@code b} from {@code a}.
     *
     * @param a the minuend
     * @param b the subtrahend
     * @return the difference {@code a - b}
     */
    public double subtract(double a, double b) {
        return a - b;
    }

    /**
     * Multiplies two numbers.
     *
     * @param a the first factor
     * @param b the second factor
     * @return the product {@code a * b}
     */
    public double multiply(double a, double b) {
        return a * b;
    }

    /**
     * Divides {@code a} by {@code b}.
     *
     * @param a the dividend
     * @param b the divisor
     * @return the quotient {@code a / b}
     * @throws ArithmeticException if {@code b} is zero
     */
    public double divide(double a, double b) {
        // Use exact bit comparison to catch both +0.0 and -0.0 (IEEE 754:
        // -0.0 == 0.0 is true in Java, so the == operator is safe here).
        if (b == 0.0) {
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        return a / b;
    }
}
