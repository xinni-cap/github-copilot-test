package com.example.calculator.service;

import com.example.calculator.model.CalculatorRequest;
import com.example.calculator.model.CalculatorResult;
import org.springframework.stereotype.Service;

/**
 * Service containing the core calculator business logic.
 */
@Service
public class CalculatorService {

    /**
     * Performs the calculation described by the given request.
     *
     * @param request the calculator input (num1, num2, operation)
     * @return a {@link CalculatorResult} with all computation details
     * @throws IllegalArgumentException if the operation is unknown or division by zero is attempted
     */
    public CalculatorResult calculate(CalculatorRequest request) {
        double num1 = request.getNum1();
        double num2 = request.getNum2();
        String operation = request.getOperation();

        double result;
        String symbol;

        switch (operation) {
            case "Add" -> {
                result = num1 + num2;
                symbol = "+";
            }
            case "Subtract" -> {
                result = num1 - num2;
                symbol = "-";
            }
            case "Multiply" -> {
                result = num1 * num2;
                symbol = "×";
            }
            case "Divide" -> {
                if (num2 == 0.0) {
                    throw new IllegalArgumentException("Cannot divide by zero");
                }
                result = num1 / num2;
                symbol = "÷";
            }
            default -> throw new IllegalArgumentException("Unknown operation: " + operation);
        }

        return new CalculatorResult(num1, num2, operation, symbol, result);
    }
}
