package com.calculator;

import java.util.Scanner;

/**
 * Interactive console front-end for the calculator.
 *
 * <p>This mirrors the Streamlit Python calculator's workflow:
 * <ol>
 *   <li>Prompt for two numbers.</li>
 *   <li>Prompt for an operation (Add / Subtract / Multiply / Divide).</li>
 *   <li>Display the result or an error message.</li>
 *   <li>Ask whether to perform another calculation.</li>
 * </ol>
 *
 * <p>All business logic lives in {@link Calculator}; this class is purely
 * responsible for I/O.
 */
public final class Main {

    private Main() {}

    // -------------------------------------------------------------------------
    // Entry point
    // -------------------------------------------------------------------------

    public static void main(String[] args) {
        var ui = new ConsoleUI(new Scanner(System.in));
        ui.run();
    }

    // -------------------------------------------------------------------------
    // Console UI – kept in a separate (package-private) class so that tests
    // can supply a fake Scanner without touching System.in.
    // -------------------------------------------------------------------------

    static final class ConsoleUI {

        private final Scanner scanner;

        ConsoleUI(Scanner scanner) {
            this.scanner = scanner;
        }

        void run() {
            printBanner();

            boolean again = true;
            while (again) {
                performCalculation();
                again = askYesNo("\nPerform another calculation? [y/N] ");
            }

            System.out.println("\nGoodbye! 👋");
        }

        // ---- individual steps -----------------------------------------------

        private void printBanner() {
            var banner = """
                    ╔══════════════════════════════╗
                    ║     🧮  Simple Calculator    ║
                    ║   Perform quick arithmetic   ║
                    ╚══════════════════════════════╝
                    """;
            System.out.print(banner);
        }

        private void performCalculation() {
            double firstNumber  = readDouble("First number  : ");
            double secondNumber = readDouble("Second number : ");
            Operation operation = readOperation();

            var result = Calculator.calculate(firstNumber, secondNumber, operation);

            // Pattern matching in switch (Java 21 – finalized)
            switch (result) {
                case CalculationResult.Success s -> {
                    System.out.println("\n✅  Result: " + s.expression());
                    System.out.println(s.details());
                }
                case CalculationResult.DivisionByZero ignored ->
                        System.out.println("\n❌  Error: Division by zero is not allowed.");
            }
        }

        // ---- helpers --------------------------------------------------------

        private double readDouble(String prompt) {
            while (true) {
                System.out.print(prompt);
                var line = scanner.nextLine().strip();
                try {
                    return Double.parseDouble(line);
                } catch (NumberFormatException e) {
                    System.out.println("   ⚠  Please enter a valid number (e.g. 3.14).");
                }
            }
        }

        private Operation readOperation() {
            var menu = """
                    Operation:
                      1. Add
                      2. Subtract
                      3. Multiply
                      4. Divide
                    Choose [1-4] or type the name: """;

            while (true) {
                System.out.print(menu);
                var input = scanner.nextLine().strip();

                // Accept numeric shortcut 1-4
                Operation op = switch (input) {
                    case "1" -> Operation.ADD;
                    case "2" -> Operation.SUBTRACT;
                    case "3" -> Operation.MULTIPLY;
                    case "4" -> Operation.DIVIDE;
                    default  -> null;
                };

                if (op != null) {
                    return op;
                }

                // Fall back to name / label parsing
                try {
                    return Operation.parse(input);
                } catch (IllegalArgumentException e) {
                    System.out.println("   ⚠  " + e.getMessage());
                }
            }
        }

        private boolean askYesNo(String prompt) {
            System.out.print(prompt);
            var answer = scanner.nextLine().strip().toLowerCase();
            return answer.equals("y") || answer.equals("yes");
        }
    }
}
