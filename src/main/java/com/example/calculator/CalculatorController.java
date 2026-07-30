package com.example.calculator;

import com.example.calculator.model.CalculatorRequest;
import com.example.calculator.model.CalculatorResult;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * MVC controller that serves the calculator web UI.
 *
 * <ul>
 *   <li>{@code GET /}  – renders the empty calculator form</li>
 *   <li>{@code POST /calculate} – processes the form submission and
 *       re-renders the page with the computation result</li>
 * </ul>
 */
@Controller
public class CalculatorController {

    private final CalculatorService calculatorService;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("request", new CalculatorRequest(0.0, 0.0, "Add"));
        return "index";
    }

    @PostMapping("/calculate")
    public String calculate(
            @Valid @ModelAttribute("request") CalculatorRequest request,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "index";
        }

        CalculatorResult result = calculatorService.calculate(request);
        model.addAttribute("calcResult", result);
        return "index";
    }
}
