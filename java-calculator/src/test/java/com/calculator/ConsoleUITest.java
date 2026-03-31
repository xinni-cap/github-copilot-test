package com.calculator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration-style tests for the {@link Main.ConsoleUI}.
 *
 * <p>Rather than mocking {@code System.in}/{@code System.out} globally, each
 * test creates a {@link Main.ConsoleUI} with a pre-loaded {@link Scanner} and
 * captures the output via a {@link ByteArrayOutputStream}.
 */
@DisplayName("ConsoleUI")
class ConsoleUITest {

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /**
     * Runs the console UI with the given lines of simulated user input and
     * returns everything that was printed to {@code System.out}.
     */
    private String runWithInput(String... lines) {
        var input = String.join(System.lineSeparator(), lines) + System.lineSeparator();
        var scanner = new Scanner(input);

        var baos   = new ByteArrayOutputStream();
        var oldOut = System.out;
        System.setOut(new PrintStream(baos));
        try {
            var ui = new Main.ConsoleUI(scanner);
            ui.run();
        } finally {
            System.setOut(oldOut);
        }
        return baos.toString();
    }

    // -------------------------------------------------------------------------
    // Tests
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("addition is displayed correctly")
    void addition() {
        // Input: 3, 4, operation "1" (Add), then "n" (no repeat)
        var output = runWithInput("3", "4", "1", "n");
        assertTrue(output.contains("3 + 4 = 7"),
                "Output should contain '3 + 4 = 7' but was:\n" + output);
    }

    @Test
    @DisplayName("subtraction is displayed correctly")
    void subtraction() {
        var output = runWithInput("10", "3", "2", "n");
        assertTrue(output.contains("10 - 3 = 7"),
                "Output should contain '10 - 3 = 7' but was:\n" + output);
    }

    @Test
    @DisplayName("multiplication is displayed correctly")
    void multiplication() {
        var output = runWithInput("6", "7", "3", "n");
        assertTrue(output.contains("6 × 7 = 42"),
                "Output should contain '6 × 7 = 42' but was:\n" + output);
    }

    @Test
    @DisplayName("division is displayed correctly")
    void division() {
        var output = runWithInput("12", "4", "4", "n");
        assertTrue(output.contains("12 ÷ 4 = 3"),
                "Output should contain '12 ÷ 4 = 3' but was:\n" + output);
    }

    @Test
    @DisplayName("division by zero shows error message")
    void divisionByZero() {
        var output = runWithInput("5", "0", "4", "n");
        assertTrue(output.contains("Division by zero is not allowed."),
                "Output should contain division-by-zero error but was:\n" + output);
    }

    @Test
    @DisplayName("operation can be entered by name (case-insensitive)")
    void operationByName() {
        var output = runWithInput("2", "3", "multiply", "n");
        assertTrue(output.contains("2 × 3 = 6"),
                "Output should contain '2 × 3 = 6' but was:\n" + output);
    }

    @Test
    @DisplayName("banner is printed on startup")
    void bannerPrinted() {
        var output = runWithInput("1", "1", "1", "n");
        assertTrue(output.contains("Simple Calculator"),
                "Banner should contain 'Simple Calculator' but was:\n" + output);
    }

    @Test
    @DisplayName("'y' answer loops for a second calculation")
    void loopsOnYes() {
        // First: 1+1, then continue; Second: 2+2, then quit
        var output = runWithInput("1", "1", "1", "y", "2", "2", "1", "n");
        assertTrue(output.contains("1 + 1 = 2"), "Should contain first result");
        assertTrue(output.contains("2 + 2 = 4"), "Should contain second result");
    }

    @Test
    @DisplayName("computation details are printed after success")
    void computationDetails() {
        var output = runWithInput("5", "3", "1", "n");
        assertTrue(output.contains("first_number"),
                "Computation details should include 'first_number'");
        assertTrue(output.contains("second_number"),
                "Computation details should include 'second_number'");
        assertTrue(output.contains("operation"),
                "Computation details should include 'operation'");
        assertTrue(output.contains("result"),
                "Computation details should include 'result'");
    }

    @Test
    @DisplayName("invalid number input is re-prompted until valid")
    void invalidNumberRetried() {
        // "abc" is rejected, then "5" is accepted; "2", operation "1" (Add), no repeat
        var output = runWithInput("abc", "5", "2", "1", "n");
        assertTrue(output.contains("5 + 2 = 7"),
                "Should eventually calculate after bad input; output was:\n" + output);
    }

    @Test
    @DisplayName("invalid operation input is re-prompted until valid")
    void invalidOperationRetried() {
        // "xyz" is rejected, then "1" (Add) is accepted
        var output = runWithInput("3", "4", "xyz", "1", "n");
        assertTrue(output.contains("3 + 4 = 7"),
                "Should eventually calculate after bad operation; output was:\n" + output);
    }
}
