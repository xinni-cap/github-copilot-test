package com.example.calculator.service;

import com.example.calculator.exception.DivisionByZeroException;
import com.example.calculator.model.CalculationRequest;
import com.example.calculator.model.CalculationResult;
import org.springframework.stereotype.Service;

/**
 * Stateless service that executes the four basic arithmetic operations.
 *
 * <p>Uses a Java 21 switch expression to dispatch to the correct operation,
 * replacing the if/elif chain from the original Python implementation.
 */
@Service
public class CalculatorService {

    /**
     * Evaluates the given {@link CalculationRequest} and returns a
     * {@link CalculationResult}.
     *
     * @param request the calculation to perform; must not be {@code null}
     * @return an immutable result record
     * @throws DivisionByZeroException if the operation is {@code DIVIDE} and
     *                                 {@code secondNumber} is zero
     */
    public CalculationResult calculate(CalculationRequest request) {
        double a = request.firstNumber();
        double b = request.secondNumber();

        double result = switch (request.operation()) {
            case ADD      -> a + b;
            case SUBTRACT -> a - b;
            case MULTIPLY -> a * b;
            case DIVIDE   -> {
                if (b == 0) {
                    throw new DivisionByZeroException();
                }
                yield a / b;
            }
        };

        return CalculationResult.of(a, b, request.operation(), result);
    }
}
