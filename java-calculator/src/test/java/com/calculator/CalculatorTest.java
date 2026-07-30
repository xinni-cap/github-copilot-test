package com.calculator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Calculator}.
 *
 * <p>Tests are organised by operation using {@link Nested} classes, which
 * keeps the test report readable and mirrors the structure of the application.
 */
@DisplayName("Calculator")
class CalculatorTest {

    // =========================================================================
    // Addition
    // =========================================================================

    @Nested
    @DisplayName("Addition")
    class AdditionTests {

        @Test
        @DisplayName("positive + positive")
        void addPositives() {
            var result = assertSuccess(Calculator.calculate(3.0, 4.0, Operation.ADD));
            assertEquals(7.0, result.result(), 1e-9);
        }

        @Test
        @DisplayName("negative + negative")
        void addNegatives() {
            var result = assertSuccess(Calculator.calculate(-2.5, -1.5, Operation.ADD));
            assertEquals(-4.0, result.result(), 1e-9);
        }

        @Test
        @DisplayName("positive + negative")
        void addMixed() {
            var result = assertSuccess(Calculator.calculate(10.0, -3.0, Operation.ADD));
            assertEquals(7.0, result.result(), 1e-9);
        }

        @Test
        @DisplayName("identity: n + 0 = n")
        void addZero() {
            var result = assertSuccess(Calculator.calculate(42.0, 0.0, Operation.ADD));
            assertEquals(42.0, result.result(), 1e-9);
        }

        @Test
        @DisplayName("expression string is correct")
        void expressionString() {
            var result = assertSuccess(Calculator.calculate(3.0, 4.0, Operation.ADD));
            assertEquals("3 + 4 = 7", result.expression());
        }

        @ParameterizedTest(name = "{0} + {1} = {2}")
        @CsvSource({
            "1.5,   2.5,   4.0",
            "0.1,   0.2,   0.30000000000000004",   // floating-point reality
            "100.0, 200.0, 300.0",
            "-5.0,  5.0,   0.0"
        })
        @DisplayName("parametrized addition cases")
        void parametrizedAddition(double a, double b, double expected) {
            var result = assertSuccess(Calculator.calculate(a, b, Operation.ADD));
            assertEquals(expected, result.result(), 1e-9);
        }
    }

    // =========================================================================
    // Subtraction
    // =========================================================================

    @Nested
    @DisplayName("Subtraction")
    class SubtractionTests {

        @Test
        @DisplayName("positive - positive")
        void subtractPositives() {
            var result = assertSuccess(Calculator.calculate(10.0, 3.0, Operation.SUBTRACT));
            assertEquals(7.0, result.result(), 1e-9);
        }

        @Test
        @DisplayName("negative result")
        void subtractLarger() {
            var result = assertSuccess(Calculator.calculate(3.0, 10.0, Operation.SUBTRACT));
            assertEquals(-7.0, result.result(), 1e-9);
        }

        @Test
        @DisplayName("identity: n - 0 = n")
        void subtractZero() {
            var result = assertSuccess(Calculator.calculate(5.0, 0.0, Operation.SUBTRACT));
            assertEquals(5.0, result.result(), 1e-9);
        }

        @Test
        @DisplayName("n - n = 0")
        void subtractSelf() {
            var result = assertSuccess(Calculator.calculate(7.0, 7.0, Operation.SUBTRACT));
            assertEquals(0.0, result.result(), 1e-9);
        }

        @Test
        @DisplayName("expression string is correct")
        void expressionString() {
            var result = assertSuccess(Calculator.calculate(10.0, 3.0, Operation.SUBTRACT));
            assertEquals("10 - 3 = 7", result.expression());
        }
    }

    // =========================================================================
    // Multiplication
    // =========================================================================

    @Nested
    @DisplayName("Multiplication")
    class MultiplicationTests {

        @Test
        @DisplayName("positive × positive")
        void multiplyPositives() {
            var result = assertSuccess(Calculator.calculate(3.0, 4.0, Operation.MULTIPLY));
            assertEquals(12.0, result.result(), 1e-9);
        }

        @Test
        @DisplayName("negative × negative = positive")
        void multiplyNegatives() {
            var result = assertSuccess(Calculator.calculate(-3.0, -4.0, Operation.MULTIPLY));
            assertEquals(12.0, result.result(), 1e-9);
        }

        @Test
        @DisplayName("positive × negative = negative")
        void multiplyMixed() {
            var result = assertSuccess(Calculator.calculate(3.0, -4.0, Operation.MULTIPLY));
            assertEquals(-12.0, result.result(), 1e-9);
        }

        @Test
        @DisplayName("absorbing: n × 0 = 0")
        void multiplyByZero() {
            var result = assertSuccess(Calculator.calculate(99.0, 0.0, Operation.MULTIPLY));
            assertEquals(0.0, result.result(), 1e-9);
        }

        @Test
        @DisplayName("identity: n × 1 = n")
        void multiplyByOne() {
            var result = assertSuccess(Calculator.calculate(7.5, 1.0, Operation.MULTIPLY));
            assertEquals(7.5, result.result(), 1e-9);
        }

        @Test
        @DisplayName("expression string is correct")
        void expressionString() {
            var result = assertSuccess(Calculator.calculate(3.0, 4.0, Operation.MULTIPLY));
            assertEquals("3 × 4 = 12", result.expression());
        }
    }

    // =========================================================================
    // Division
    // =========================================================================

    @Nested
    @DisplayName("Division")
    class DivisionTests {

        @Test
        @DisplayName("positive ÷ positive")
        void dividePositives() {
            var result = assertSuccess(Calculator.calculate(12.0, 4.0, Operation.DIVIDE));
            assertEquals(3.0, result.result(), 1e-9);
        }

        @Test
        @DisplayName("non-integer result")
        void divideNonInteger() {
            var result = assertSuccess(Calculator.calculate(1.0, 3.0, Operation.DIVIDE));
            assertEquals(1.0 / 3.0, result.result(), 1e-9);
        }

        @Test
        @DisplayName("negative ÷ positive = negative")
        void divideNegativeByPositive() {
            var result = assertSuccess(Calculator.calculate(-9.0, 3.0, Operation.DIVIDE));
            assertEquals(-3.0, result.result(), 1e-9);
        }

        @Test
        @DisplayName("identity: n ÷ 1 = n")
        void divideByOne() {
            var result = assertSuccess(Calculator.calculate(42.0, 1.0, Operation.DIVIDE));
            assertEquals(42.0, result.result(), 1e-9);
        }

        @Test
        @DisplayName("division by zero returns DivisionByZero result")
        void divisionByZero() {
            var result = Calculator.calculate(5.0, 0.0, Operation.DIVIDE);
            assertInstanceOf(CalculationResult.DivisionByZero.class, result,
                    "Expected DivisionByZero but got: " + result);
        }

        @Test
        @DisplayName("DivisionByZero carries the dividend")
        void divisionByZeroCarriesDividend() {
            var result = (CalculationResult.DivisionByZero)
                    Calculator.calculate(5.0, 0.0, Operation.DIVIDE);
            assertEquals(5.0, result.dividend(), 1e-9);
        }

        @Test
        @DisplayName("DivisionByZero has non-blank error message")
        void divisionByZeroErrorMessage() {
            var result = (CalculationResult.DivisionByZero)
                    Calculator.calculate(1.0, 0.0, Operation.DIVIDE);
            assertFalse(result.errorMessage().isBlank());
        }

        @Test
        @DisplayName("expression string uses division symbol ÷")
        void expressionString() {
            var result = assertSuccess(Calculator.calculate(12.0, 4.0, Operation.DIVIDE));
            assertEquals("12 ÷ 4 = 3", result.expression());
        }
    }

    // =========================================================================
    // String-based overload
    // =========================================================================

    @Nested
    @DisplayName("String operation overload")
    class StringOverloadTests {

        @ParameterizedTest(name = "operation \"{0}\" is recognised")
        @ValueSource(strings = {"Add", "add", "ADD", "Subtract", "MULTIPLY", "Divide"})
        @DisplayName("case-insensitive operation names")
        void caseInsensitive(String opName) {
            assertDoesNotThrow(() -> Calculator.calculate(1.0, 1.0, opName));
        }

        @Test
        @DisplayName("unknown operation string throws IllegalArgumentException")
        void unknownOperationThrows() {
            assertThrows(IllegalArgumentException.class,
                    () -> Calculator.calculate(1.0, 1.0, "modulo"));
        }

        @Test
        @DisplayName("numeric shortcut strings are NOT accepted by Operation.parse")
        void numericShortcutsNotAccepted() {
            // The console UI maps "1"-"4" to enum values; the parser itself does not
            assertThrows(IllegalArgumentException.class,
                    () -> Calculator.calculate(1.0, 1.0, "1"));
        }
    }

    // =========================================================================
    // Operation enum
    // =========================================================================

    @Nested
    @DisplayName("Operation enum")
    class OperationEnumTests {

        @Test
        @DisplayName("ADD has label 'Add' and symbol '+'")
        void addMetadata() {
            assertEquals("Add", Operation.ADD.label());
            assertEquals("+",   Operation.ADD.symbol());
        }

        @Test
        @DisplayName("SUBTRACT has label 'Subtract' and symbol '-'")
        void subtractMetadata() {
            assertEquals("Subtract", Operation.SUBTRACT.label());
            assertEquals("-",        Operation.SUBTRACT.symbol());
        }

        @Test
        @DisplayName("MULTIPLY has label 'Multiply' and symbol '×'")
        void multiplyMetadata() {
            assertEquals("Multiply", Operation.MULTIPLY.label());
            assertEquals("×",        Operation.MULTIPLY.symbol());
        }

        @Test
        @DisplayName("DIVIDE has label 'Divide' and symbol '÷'")
        void divideMetadata() {
            assertEquals("Divide", Operation.DIVIDE.label());
            assertEquals("÷",      Operation.DIVIDE.symbol());
        }

        @Test
        @DisplayName("parse is case-insensitive for labels")
        void parseCaseInsensitive() {
            assertEquals(Operation.ADD,      Operation.parse("add"));
            assertEquals(Operation.SUBTRACT, Operation.parse("SUBTRACT"));
            assertEquals(Operation.MULTIPLY, Operation.parse("Multiply"));
            assertEquals(Operation.DIVIDE,   Operation.parse("divide"));
        }

        @Test
        @DisplayName("parse throws for empty input")
        void parseEmptyThrows() {
            assertThrows(IllegalArgumentException.class, () -> Operation.parse(""));
        }
    }

    // =========================================================================
    // CalculationResult records
    // =========================================================================

    @Nested
    @DisplayName("CalculationResult records")
    class CalculationResultTests {

        @Test
        @DisplayName("Success record stores all components")
        void successStoresComponents() {
            var s = new CalculationResult.Success(2.0, 3.0, Operation.ADD, 5.0);
            assertEquals(2.0,          s.firstNumber(),  1e-9);
            assertEquals(3.0,          s.secondNumber(), 1e-9);
            assertEquals(Operation.ADD, s.operation());
            assertEquals(5.0,          s.result(),       1e-9);
        }

        @Test
        @DisplayName("Success details() contains all field names")
        void successDetailsContainsLabels() {
            var s = new CalculationResult.Success(2.0, 3.0, Operation.ADD, 5.0);
            var details = s.details();
            assertTrue(details.contains("first_number"));
            assertTrue(details.contains("second_number"));
            assertTrue(details.contains("operation"));
            assertTrue(details.contains("result"));
        }

        @Test
        @DisplayName("Success record equality (value-based)")
        void successEquality() {
            var a = new CalculationResult.Success(1.0, 2.0, Operation.ADD, 3.0);
            var b = new CalculationResult.Success(1.0, 2.0, Operation.ADD, 3.0);
            assertEquals(a, b);
        }

        @Test
        @DisplayName("DivisionByZero record equality (value-based)")
        void divByZeroEquality() {
            var a = new CalculationResult.DivisionByZero(5.0);
            var b = new CalculationResult.DivisionByZero(5.0);
            assertEquals(a, b);
        }
    }

    // =========================================================================
    // Helper
    // =========================================================================

    /** Assert that the result is a {@link CalculationResult.Success} and return it. */
    private static CalculationResult.Success assertSuccess(CalculationResult result) {
        assertInstanceOf(CalculationResult.Success.class, result,
                "Expected Success but got: " + result);
        return (CalculationResult.Success) result;
    }
}
