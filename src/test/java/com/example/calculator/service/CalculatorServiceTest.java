package com.example.calculator.service;

import com.example.calculator.exception.DivisionByZeroException;
import com.example.calculator.model.CalculationRequest;
import com.example.calculator.model.CalculationResult;
import com.example.calculator.model.Operation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link CalculatorService}.
 */
class CalculatorServiceTest {

    private final CalculatorService service = new CalculatorService();

    @ParameterizedTest(name = "{0} + {1} = {2}")
    @CsvSource({"3.0, 4.0, 7.0", "0.0, 0.0, 0.0", "-5.0, 5.0, 0.0", "1.5, 2.5, 4.0"})
    @DisplayName("Addition produces the correct sum")
    void addition(double a, double b, double expected) {
        CalculationResult result = service.calculate(new CalculationRequest(a, b, Operation.ADD));
        assertThat(result.result()).isEqualTo(expected);
        assertThat(result.operation()).isEqualTo(Operation.ADD);
    }

    @ParameterizedTest(name = "{0} - {1} = {2}")
    @CsvSource({"10.0, 3.0, 7.0", "0.0, 0.0, 0.0", "5.0, 10.0, -5.0"})
    @DisplayName("Subtraction produces the correct difference")
    void subtraction(double a, double b, double expected) {
        CalculationResult result = service.calculate(new CalculationRequest(a, b, Operation.SUBTRACT));
        assertThat(result.result()).isEqualTo(expected);
    }

    @ParameterizedTest(name = "{0} × {1} = {2}")
    @CsvSource({"3.0, 4.0, 12.0", "0.0, 100.0, 0.0", "-2.0, -3.0, 6.0", "2.5, 4.0, 10.0"})
    @DisplayName("Multiplication produces the correct product")
    void multiplication(double a, double b, double expected) {
        CalculationResult result = service.calculate(new CalculationRequest(a, b, Operation.MULTIPLY));
        assertThat(result.result()).isEqualTo(expected);
    }

    @ParameterizedTest(name = "{0} ÷ {1} = {2}")
    @CsvSource({"10.0, 2.0, 5.0", "7.0, 2.0, 3.5", "-9.0, 3.0, -3.0"})
    @DisplayName("Division produces the correct quotient")
    void division(double a, double b, double expected) {
        CalculationResult result = service.calculate(new CalculationRequest(a, b, Operation.DIVIDE));
        assertThat(result.result()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Division by zero throws DivisionByZeroException")
    void divisionByZeroThrows() {
        assertThatThrownBy(() ->
                service.calculate(new CalculationRequest(5.0, 0.0, Operation.DIVIDE)))
                .isInstanceOf(DivisionByZeroException.class)
                .hasMessageContaining("Division by zero");
    }

    @Test
    @DisplayName("Result expression is formatted correctly")
    void expressionFormatting() {
        CalculationResult result = service.calculate(new CalculationRequest(3.0, 4.0, Operation.ADD));
        assertThat(result.expression()).contains("+");
        assertThat(result.expression()).contains("7.0");
    }
}
