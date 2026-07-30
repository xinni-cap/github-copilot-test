package com.example.calculator.controller;

import com.example.calculator.model.CalculationRequest;
import com.example.calculator.model.CalculationResponse;
import com.example.calculator.service.CalculatorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller exposing the calculator API.
 *
 * <p>Endpoints:
 * <ul>
 *   <li>{@code GET  /api/v1/calculator/health} — liveness check</li>
 *   <li>{@code POST /api/v1/calculator/calculate} — perform arithmetic</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v1/calculator")
public class CalculatorController {

    private final CalculatorService calculatorService;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    /** Simple liveness probe. */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "calculator-api"));
    }

    /**
     * Perform an arithmetic calculation.
     *
     * <p>Example request body:
     * <pre>{@code
     * {
     *   "firstNumber":  10.0,
     *   "secondNumber": 3.0,
     *   "operation":    "DIVIDE"
     * }
     * }</pre>
     *
     * @param request the validated calculation request
     * @return a {@link CalculationResponse} with the result and expression
     */
    @PostMapping("/calculate")
    public ResponseEntity<CalculationResponse> calculate(
            @Valid @RequestBody CalculationRequest request) {
        CalculationResponse response = calculatorService.calculate(request);
        return ResponseEntity.ok(response);
    }
}
