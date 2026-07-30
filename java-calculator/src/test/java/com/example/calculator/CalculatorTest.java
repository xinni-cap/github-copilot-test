package com.example.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Calculator} – mirrors the arithmetic logic validated
 * by the Python Streamlit calculator.
 */
@DisplayName("Calculator")
class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    // ── Add ──────────────────────────────────────────────────────────────────

    @ParameterizedTest(name = "{0} + {1} = {2}")
    @CsvSource({
        "1.0,   2.0,   3.0",
        "-1.0,  1.0,   0.0",
        "0.0,   0.0,   0.0",
        "2.5,   2.5,   5.0",
        "-3.0, -4.0,  -7.0"
    })
    @DisplayName("add: basic cases")
    void add(double a, double b, double expected) {
        assertEquals(expected, calculator.add(a, b), 1e-9);
    }

    // ── Subtract ─────────────────────────────────────────────────────────────

    @ParameterizedTest(name = "{0} - {1} = {2}")
    @CsvSource({
        "5.0,  3.0,  2.0",
        "0.0,  0.0,  0.0",
        "1.0,  2.0, -1.0",
        "-1.0, -1.0, 0.0"
    })
    @DisplayName("subtract: basic cases")
    void subtract(double a, double b, double expected) {
        assertEquals(expected, calculator.subtract(a, b), 1e-9);
    }

    // ── Multiply ─────────────────────────────────────────────────────────────

    @ParameterizedTest(name = "{0} × {1} = {2}")
    @CsvSource({
        "3.0,  4.0,  12.0",
        "0.0,  99.0,  0.0",
        "-2.0, 3.0,  -6.0",
        "-2.0,-3.0,   6.0"
    })
    @DisplayName("multiply: basic cases")
    void multiply(double a, double b, double expected) {
        assertEquals(expected, calculator.multiply(a, b), 1e-9);
    }

    // ── Divide ───────────────────────────────────────────────────────────────

    @ParameterizedTest(name = "{0} ÷ {1} = {2}")
    @CsvSource({
        "10.0,  2.0,  5.0",
        "7.0,   2.0,  3.5",
        "-6.0,  3.0, -2.0",
        "0.0,   5.0,  0.0"
    })
    @DisplayName("divide: basic cases")
    void divide(double a, double b, double expected) {
        assertEquals(expected, calculator.divide(a, b), 1e-9);
    }

    @Test
    @DisplayName("divide: throws ArithmeticException on division by zero")
    void divideByZeroThrows() {
        ArithmeticException ex = assertThrows(
                ArithmeticException.class,
                () -> calculator.divide(5.0, 0.0)
        );
        assertTrue(ex.getMessage().contains("zero"),
                "Exception message should mention 'zero'");
    }
}
