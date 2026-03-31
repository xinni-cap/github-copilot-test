package com.example.calculator.controller;

import com.example.calculator.exception.DivisionByZeroException;
import com.example.calculator.model.CalculationRequest;
import com.example.calculator.model.CalculationResult;
import com.example.calculator.service.CalculatorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST API controller that exposes calculator operations as JSON endpoints.
 *
 * <p>Accepts a {@link CalculationRequest} JSON body and returns either a
 * {@link CalculationResult} on success or a descriptive error body on failure.
 */
@RestController
@RequestMapping("/api")
public class CalculatorRestController {

    private final CalculatorService calculatorService;

    public CalculatorRestController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    /**
     * Performs a calculation and returns the result.
     *
     * <p>Example request body:
     * <pre>{@code
     * {
     *   "firstNumber": 10.0,
     *   "secondNumber": 4.0,
     *   "operation": "DIVIDE"
     * }
     * }</pre>
     *
     * @param request validated request body
     * @return 200 OK with the {@link CalculationResult}, or 400 Bad Request on
     *         division-by-zero / validation error
     */
    @PostMapping("/calculate")
    public ResponseEntity<?> calculate(@Valid @RequestBody CalculationRequest request) {
        try {
            CalculationResult result = calculatorService.calculate(request);
            return ResponseEntity.ok(result);
        } catch (DivisionByZeroException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }
}
