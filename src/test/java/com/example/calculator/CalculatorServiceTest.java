package com.example.calculator;

import com.example.calculator.model.CalculatorRequest;
import com.example.calculator.model.CalculatorResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class CalculatorServiceTest {

    private CalculatorService service;

    @BeforeEach
    void setUp() {
        service = new CalculatorService();
    }

    @Test
    void add_returnsCorrectSum() {
        CalculatorResult result = service.calculate(new CalculatorRequest(3.0, 4.0, "Add"));
        assertThat(result.getResult()).isEqualTo(7.0);
        assertThat(result.getSymbol()).isEqualTo("+");
        assertThat(result.hasError()).isFalse();
    }

    @Test
    void subtract_returnsCorrectDifference() {
        CalculatorResult result = service.calculate(new CalculatorRequest(10.0, 3.5, "Subtract"));
        assertThat(result.getResult()).isCloseTo(6.5, within(1e-9));
        assertThat(result.getSymbol()).isEqualTo("-");
    }

    @Test
    void multiply_returnsCorrectProduct() {
        CalculatorResult result = service.calculate(new CalculatorRequest(6.0, 7.0, "Multiply"));
        assertThat(result.getResult()).isEqualTo(42.0);
        assertThat(result.getSymbol()).isEqualTo("×");
    }

    @Test
    void divide_returnsCorrectQuotient() {
        CalculatorResult result = service.calculate(new CalculatorRequest(10.0, 4.0, "Divide"));
        assertThat(result.getResult()).isCloseTo(2.5, within(1e-9));
        assertThat(result.getSymbol()).isEqualTo("÷");
        assertThat(result.hasError()).isFalse();
    }

    @Test
    void divide_byZero_returnsError() {
        CalculatorResult result = service.calculate(new CalculatorRequest(5.0, 0.0, "Divide"));
        assertThat(result.hasError()).isTrue();
        assertThat(result.getErrorMessage()).contains("zero");
        assertThat(result.getResult()).isNull();
    }

    @Test
    void unknownOperation_returnsError() {
        CalculatorResult result = service.calculate(new CalculatorRequest(1.0, 1.0, "Modulo"));
        assertThat(result.hasError()).isTrue();
        assertThat(result.getErrorMessage()).contains("Unknown operation");
    }

    @Test
    void add_negativeNumbers() {
        CalculatorResult result = service.calculate(new CalculatorRequest(-3.0, -7.0, "Add"));
        assertThat(result.getResult()).isEqualTo(-10.0);
    }

    @Test
    void multiply_byZero_returnsZero() {
        CalculatorResult result = service.calculate(new CalculatorRequest(999.0, 0.0, "Multiply"));
        assertThat(result.getResult()).isEqualTo(0.0);
        assertThat(result.hasError()).isFalse();
    }
}
