package com.calculator.controller;

import com.calculator.model.CalculatorRequest;
import com.calculator.model.CalculatorResponse;
import com.calculator.service.CalculatorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CalculatorController {

    private static final String[] OPERATIONS = {"Add", "Subtract", "Multiply", "Divide"};

    private final CalculatorService calculatorService;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("operations", OPERATIONS);
        return "calculator";
    }

    @PostMapping("/calculate")
    public String calculate(
            @RequestParam(defaultValue = "0") Double firstNumber,
            @RequestParam(defaultValue = "0") Double secondNumber,
            @RequestParam String operation,
            Model model) {

        CalculatorRequest request = new CalculatorRequest(firstNumber, secondNumber, operation);
        CalculatorResponse response = calculatorService.calculate(request);

        model.addAttribute("operations", OPERATIONS);
        model.addAttribute("firstNumber", firstNumber);
        model.addAttribute("secondNumber", secondNumber);
        model.addAttribute("selectedOperation", operation);
        model.addAttribute("response", response);

        return "calculator";
    }
}
