package com.example.calculator.exception;

/**
 * Thrown when a division-by-zero is attempted, mirroring the error shown in
 * the original Python/Streamlit app.
 */
public class DivisionByZeroException extends RuntimeException {

    public DivisionByZeroException() {
        super("Division by zero is not allowed.");
    }
}
