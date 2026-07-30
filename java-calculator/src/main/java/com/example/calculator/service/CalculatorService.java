package com.example.calculator.service;

import com.example.calculator.model.CalculationRequest;
import com.example.calculator.model.CalculationResponse;
import com.example.calculator.model.Operation;
import org.springframework.stereotype.Service;

/**
 * Core calculator service.
 *
 * <p>Demonstrates modern Java 21 features:
 * <ul>
 *   <li>Switch expressions with pattern matching</li>
 *   <li>Text blocks for multi-line strings</li>
 *   <li>Records for immutable data</li>
 *   <li>{@code String::formatted} for interpolation</li>
 * </ul>
 */
@Service
public class CalculatorService {

    /**
     * Perform the arithmetic operation specified in {@code request}.
     *
     * @param request the calculation input
     * @return a {@link CalculationResponse} containing the result and a human-readable expression
     * @throws ArithmeticException      when dividing by zero
     * @throws IllegalArgumentException when the operation string is unrecognised
     */
    public CalculationResponse calculate(CalculationRequest request) {
        Operation op = Operation.fromString(request.operation());
        double a = request.firstNumber();
        double b = request.secondNumber();

        // Java 21 switch expression with enhanced pattern matching
        double result = switch (op) {
            case ADD      -> a + b;
            case SUBTRACT -> a - b;
            case MULTIPLY -> a * b;
            case DIVIDE   -> {
                if (b == 0) {
                    throw new ArithmeticException("Division by zero is not allowed.");
                }
                yield a / b;
            }
        };

        String symbol = switch (op) {
            case ADD      -> "+";
            case SUBTRACT -> "-";
            case MULTIPLY -> "×";
            case DIVIDE   -> "÷";
        };

        String expression = "%s %s %s = %s".formatted(a, symbol, b, result);

        return new CalculationResponse(a, b, op.name(), result, expression);
    }
}
