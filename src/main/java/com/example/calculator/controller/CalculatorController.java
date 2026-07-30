package com.example.calculator.controller;

import com.example.calculator.model.CalculatorRequest;
import com.example.calculator.model.CalculatorResult;
import com.example.calculator.service.CalculatorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Web MVC controller for the calculator UI.
 */
@Controller
@RequestMapping("/")
public class CalculatorController {

    private final CalculatorService calculatorService;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    /**
     * Renders the calculator form (GET /).
     */
    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("calculatorRequest", new CalculatorRequest());
        return "calculator";
    }

    /**
     * Handles form submission (POST /), performs calculation, and returns result or error.
     */
    @PostMapping
    public String calculate(
            @Valid @ModelAttribute("calculatorRequest") CalculatorRequest request,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "calculator";
        }

        try {
            CalculatorResult result = calculatorService.calculate(request);
            model.addAttribute("result", result);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "calculator";
    }
}
