package com.calculator;

import com.calculator.model.CalculatorRequest;
import com.calculator.model.CalculatorResponse;
import com.calculator.service.CalculatorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CalculatorApplicationTests {

    @Autowired
    private CalculatorService calculatorService;

    @Test
    void contextLoads() {
    }

    @Test
    void testAddition() {
        CalculatorRequest req = new CalculatorRequest(10.0, 5.0, "Add");
        CalculatorResponse resp = calculatorService.calculate(req);
        assertFalse(resp.isError());
        assertEquals(15.0, resp.result());
        assertEquals("+", resp.symbol());
    }

    @Test
    void testSubtraction() {
        CalculatorRequest req = new CalculatorRequest(10.0, 5.0, "Subtract");
        CalculatorResponse resp = calculatorService.calculate(req);
        assertFalse(resp.isError());
        assertEquals(5.0, resp.result());
        assertEquals("-", resp.symbol());
    }

    @Test
    void testMultiplication() {
        CalculatorRequest req = new CalculatorRequest(4.0, 3.0, "Multiply");
        CalculatorResponse resp = calculatorService.calculate(req);
        assertFalse(resp.isError());
        assertEquals(12.0, resp.result());
        assertEquals("×", resp.symbol());
    }

    @Test
    void testDivision() {
        CalculatorRequest req = new CalculatorRequest(10.0, 2.0, "Divide");
        CalculatorResponse resp = calculatorService.calculate(req);
        assertFalse(resp.isError());
        assertEquals(5.0, resp.result());
        assertEquals("÷", resp.symbol());
    }

    @Test
    void testDivisionByZero() {
        CalculatorRequest req = new CalculatorRequest(10.0, 0.0, "Divide");
        CalculatorResponse resp = calculatorService.calculate(req);
        assertTrue(resp.isError());
        assertEquals("Division by zero is not allowed.", resp.errorMessage());
    }
}
