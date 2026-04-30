package com.example.calculator.service;

import com.example.calculator.model.CalculationRequest;
import com.example.calculator.model.CalculationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link CalculatorService}.
 *
 * <p>Uses JUnit 5 + AssertJ (both on the Spring Boot test classpath).
 */
class CalculatorServiceTest {

    private CalculatorService service;

    @BeforeEach
    void setUp() {
        service = new CalculatorService();
    }

    // -----------------------------------------------------------------------
    // Parameterized happy-path tests
    // -----------------------------------------------------------------------

    @ParameterizedTest(name = "{0} {2} {1} = {3}")
    @CsvSource({
        "3.0,  4.0, ADD,      7.0",
        "10.0, 3.0, SUBTRACT, 7.0",
        "3.0,  4.0, MULTIPLY, 12.0",
        "10.0, 4.0, DIVIDE,   2.5",
        "0.0,  5.0, ADD,      5.0",
        "-3.0, 3.0, ADD,      0.0",
    })
    void calculate_happyPath(double a, double b, String op, double expected) {
        CalculationRequest req = new CalculationRequest(a, b, op);
        CalculationResponse res = service.calculate(req);

        assertThat(res.result()).isEqualTo(expected);
        assertThat(res.firstNumber()).isEqualTo(a);
        assertThat(res.secondNumber()).isEqualTo(b);
        assertThat(res.operation()).isEqualTo(op);
        assertThat(res.expression()).isNotBlank();
    }

    // -----------------------------------------------------------------------
    // Division by zero
    // -----------------------------------------------------------------------

    @Test
    void calculate_divideByZero_throwsArithmeticException() {
        CalculationRequest req = new CalculationRequest(5.0, 0.0, "DIVIDE");
        assertThatThrownBy(() -> service.calculate(req))
                .isInstanceOf(ArithmeticException.class)
                .hasMessageContaining("zero");
    }

    // -----------------------------------------------------------------------
    // Unknown operation
    // -----------------------------------------------------------------------

    @Test
    void calculate_unknownOperation_throwsIllegalArgumentException() {
        CalculationRequest req = new CalculationRequest(1.0, 2.0, "POWER");
        assertThatThrownBy(() -> service.calculate(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("POWER");
    }

    // -----------------------------------------------------------------------
    // Expression format
    // -----------------------------------------------------------------------

    @Test
    void calculate_add_expressionContainsSymbol() {
        CalculationRequest req = new CalculationRequest(2.0, 3.0, "ADD");
        CalculationResponse res = service.calculate(req);
        assertThat(res.expression()).contains("+");
    }

    @Test
    void calculate_subtract_expressionContainsSymbol() {
        CalculationRequest req = new CalculationRequest(5.0, 2.0, "SUBTRACT");
        CalculationResponse res = service.calculate(req);
        assertThat(res.expression()).contains("-");
    }

    @Test
    void calculate_multiply_expressionContainsSymbol() {
        CalculationRequest req = new CalculationRequest(3.0, 4.0, "MULTIPLY");
        CalculationResponse res = service.calculate(req);
        assertThat(res.expression()).contains("×");
    }

    @Test
    void calculate_divide_expressionContainsSymbol() {
        CalculationRequest req = new CalculationRequest(10.0, 2.0, "DIVIDE");
        CalculationResponse res = service.calculate(req);
        assertThat(res.expression()).contains("÷");
    }

    // -----------------------------------------------------------------------
    // Operation aliases
    // -----------------------------------------------------------------------

    @Test
    void calculate_operationAlias_plus() {
        CalculationRequest req = new CalculationRequest(1.0, 1.0, "+");
        CalculationResponse res = service.calculate(req);
        assertThat(res.result()).isEqualTo(2.0);
    }
}
