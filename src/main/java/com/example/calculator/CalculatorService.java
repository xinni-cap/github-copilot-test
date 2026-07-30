package com.example.calculator;

import com.example.calculator.model.CalculatorRequest;
import com.example.calculator.model.CalculatorResult;
import org.springframework.stereotype.Service;

/**
 * Performs arithmetic calculations and maps each operation to its
 * human-readable symbol.  Division by zero is handled gracefully by
 * returning an error message rather than throwing an exception.
 */
@Service
public class CalculatorService {

    /**
     * Evaluates the calculation described in {@code request} and returns
     * a {@link CalculatorResult} that the controller / view can render.
     *
     * @param request validated request containing two operands and an operation
     * @return result object; {@link CalculatorResult#hasError()} is {@code true}
     *         when division by zero is attempted
     */
    public CalculatorResult calculate(CalculatorRequest request) {
        CalculatorResult result = new CalculatorResult();
        result.setNum1(request.getNum1());
        result.setNum2(request.getNum2());
        result.setOperation(request.getOperation());

        double a = request.getNum1();
        double b = request.getNum2();

        switch (request.getOperation()) {
            case "Add" -> {
                result.setSymbol("+");
                result.setResult(a + b);
            }
            case "Subtract" -> {
                result.setSymbol("-");
                result.setResult(a - b);
            }
            case "Multiply" -> {
                result.setSymbol("×");
                result.setResult(a * b);
            }
            case "Divide" -> {
                result.setSymbol("÷");
                if (b == 0) {
                    result.setErrorMessage("Division by zero is not allowed.");
                } else {
                    result.setResult(a / b);
                }
            }
            default -> result.setErrorMessage("Unknown operation: " + request.getOperation());
        }

        return result;
    }
}
