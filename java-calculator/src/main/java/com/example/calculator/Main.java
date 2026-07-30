package com.example.calculator;

import java.util.Scanner;

/**
 * Command-line entry point for the Java Calculator application.
 *
 * <p>Mirrors the interactive experience of the Streamlit {@code app.py}
 * calculator using Java 21 features such as enhanced switch expressions
 * and text blocks.
 */
public class Main {

    public static void main(String[] args) {
        var calculator = new Calculator();

        System.out.println("""
                ╔══════════════════════════════╗
                ║       Java Calculator         ║
                ║  Simple arithmetic operations ║
                ╚══════════════════════════════╝
                """);

        try (var scanner = new Scanner(System.in)) {
            System.out.print("First number : ");
            double a = scanner.nextDouble();

            System.out.print("Second number: ");
            double b = scanner.nextDouble();

            System.out.println("Operation (add / subtract / multiply / divide): ");
            String op = scanner.next().trim().toLowerCase();

            OperationResult opResult = evaluate(calculator, op, a, b);

            System.out.printf("%nResult: %.6f %s %.6f = %.6f%n",
                    a, opResult.symbol(), b, opResult.value());
        } catch (ArithmeticException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    /** Pairs a computed value with its display symbol. */
    record OperationResult(double value, String symbol) {}

    /**
     * Evaluates the named operation on {@code a} and {@code b}, returning
     * both the numeric result and the display symbol in one place.
     *
     * @throws IllegalArgumentException if {@code op} is not recognised
     * @throws ArithmeticException      if {@code op} is "divide" and {@code b} is zero
     */
    private static OperationResult evaluate(Calculator calc, String op, double a, double b) {
        return switch (op) {
            case "add"      -> new OperationResult(calc.add(a, b),      "+");
            case "subtract" -> new OperationResult(calc.subtract(a, b), "-");
            case "multiply" -> new OperationResult(calc.multiply(a, b), "×");
            case "divide"   -> new OperationResult(calc.divide(a, b),   "÷");
            default         -> throw new IllegalArgumentException(
                    "Unknown operation: " + op +
                    ". Use add, subtract, multiply, or divide.");
        };
    }
}
