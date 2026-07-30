package com.example.calculator.controller;

import com.example.calculator.exception.DivisionByZeroException;
import com.example.calculator.model.CalculationRequest;
import com.example.calculator.model.CalculationResult;
import com.example.calculator.model.Operation;
import com.example.calculator.service.CalculatorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * MVC controller that renders the calculator's Thymeleaf web UI.
 *
 * <p>Mirrors the Streamlit form layout: two numeric inputs, an operation
 * selector, and a result area that shows the formatted expression on success
 * or an error message on division-by-zero.
 */
@Controller
public class CalculatorController {

    private final CalculatorService calculatorService;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    /** Renders the blank calculator form. */
    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("operations", Operation.values());
        model.addAttribute("selectedOperation", Operation.ADD);
        model.addAttribute("firstNumber", 0.0);
        model.addAttribute("secondNumber", 0.0);
        return "index";
    }

    /** Handles form submission and re-renders the page with the result. */
    @PostMapping("/calculate")
    public String calculate(
            @RequestParam double firstNumber,
            @RequestParam double secondNumber,
            @RequestParam Operation operation,
            Model model) {

        model.addAttribute("operations", Operation.values());
        model.addAttribute("selectedOperation", operation);
        model.addAttribute("firstNumber", firstNumber);
        model.addAttribute("secondNumber", secondNumber);

        try {
            CalculationResult calculationResult =
                    calculatorService.calculate(new CalculationRequest(firstNumber, secondNumber, operation));
            model.addAttribute("result", calculationResult);
        } catch (DivisionByZeroException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        }

        return "index";
    }
}
