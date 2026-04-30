package com.calculator.service;

import com.calculator.model.CalculatorRequest;
import com.calculator.model.CalculatorResponse;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

    public CalculatorResponse calculate(CalculatorRequest request) {
        double a = request.firstNumber();
        double b = request.secondNumber();
        String operation = request.operation();

        return switch (operation) {
            case "Add"      -> CalculatorResponse.success(a, b, operation, "+", a + b);
            case "Subtract" -> CalculatorResponse.success(a, b, operation, "-", a - b);
            case "Multiply" -> CalculatorResponse.success(a, b, operation, "×", a * b);
            case "Divide"   -> b == 0
                    ? CalculatorResponse.error("Division by zero is not allowed.")
                    : CalculatorResponse.success(a, b, operation, "÷", a / b);
            default         -> CalculatorResponse.error("Unknown operation: " + operation);
        };
    }
}
