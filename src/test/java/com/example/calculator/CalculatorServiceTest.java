package com.example.calculator;

import com.example.calculator.model.CalculatorRequest;
import com.example.calculator.model.CalculatorResult;
import com.example.calculator.service.CalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorServiceTest {

    private CalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        calculatorService = new CalculatorService();
    }

    @Test
    void addition_returnsCorrectResult() {
        CalculatorRequest request = new CalculatorRequest(3.5, 1.5, "Add");
        CalculatorResult result = calculatorService.calculate(request);

        assertEquals(5.0, result.getResult(), 1e-9);
        assertEquals(3.5, result.getNum1(), 1e-9);
        assertEquals(1.5, result.getNum2(), 1e-9);
        assertEquals("Add", result.getOperation());
        assertEquals("+", result.getSymbol());
    }

    @Test
    void subtraction_returnsCorrectResult() {
        CalculatorRequest request = new CalculatorRequest(10.0, 4.0, "Subtract");
        CalculatorResult result = calculatorService.calculate(request);

        assertEquals(6.0, result.getResult(), 1e-9);
        assertEquals("Subtract", result.getOperation());
        assertEquals("-", result.getSymbol());
    }

    @Test
    void multiplication_returnsCorrectResult() {
        CalculatorRequest request = new CalculatorRequest(2.5, 4.0, "Multiply");
        CalculatorResult result = calculatorService.calculate(request);

        assertEquals(10.0, result.getResult(), 1e-9);
        assertEquals("Multiply", result.getOperation());
        assertEquals("×", result.getSymbol());
    }

    @Test
    void division_returnsCorrectResult() {
        CalculatorRequest request = new CalculatorRequest(9.0, 3.0, "Divide");
        CalculatorResult result = calculatorService.calculate(request);

        assertEquals(3.0, result.getResult(), 1e-9);
        assertEquals("Divide", result.getOperation());
        assertEquals("÷", result.getSymbol());
    }

    @Test
    void divisionByZero_throwsIllegalArgumentException() {
        CalculatorRequest request = new CalculatorRequest(5.0, 0.0, "Divide");

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> calculatorService.calculate(request));
        assertEquals("Cannot divide by zero", exception.getMessage());
    }

    @Test
    void addition_withNegativeNumbers_returnsCorrectResult() {
        CalculatorRequest request = new CalculatorRequest(-3.0, -2.0, "Add");
        CalculatorResult result = calculatorService.calculate(request);

        assertEquals(-5.0, result.getResult(), 1e-9);
    }

    @Test
    void multiplication_withZero_returnsZero() {
        CalculatorRequest request = new CalculatorRequest(99.99, 0.0, "Multiply");
        CalculatorResult result = calculatorService.calculate(request);

        assertEquals(0.0, result.getResult(), 1e-9);
    }

    @Test
    void unknownOperation_throwsIllegalArgumentException() {
        CalculatorRequest request = new CalculatorRequest(1.0, 2.0, "Modulo");

        assertThrows(IllegalArgumentException.class, () -> calculatorService.calculate(request));
    }

    @Test
    void result_getExpression_formatsCorrectly() {
        CalculatorRequest request = new CalculatorRequest(1.5, 2.5, "Add");
        CalculatorResult result = calculatorService.calculate(request);

        String expression = result.getExpression();
        assertNotNull(expression);
        assertTrue(expression.contains("+"));
        assertTrue(expression.contains("="));
    }
}
