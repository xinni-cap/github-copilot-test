package com.example.calculator.controller;

import com.example.calculator.model.Operation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the REST API ({@link CalculatorRestController}) and
 * the MVC UI ({@link CalculatorController}).
 */
@SpringBootTest
@AutoConfigureMockMvc
class CalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ── REST API tests ─────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/calculate returns correct result for addition")
    void apiAddition() throws Exception {
        String body = """
                {
                  "firstNumber": 3.0,
                  "secondNumber": 4.0,
                  "operation": "ADD"
                }
                """;

        mockMvc.perform(post("/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(7.0))
                .andExpect(jsonPath("$.operation").value("ADD"))
                .andExpect(jsonPath("$.expression").value("3.0 + 4.0 = 7.0"));
    }

    @Test
    @DisplayName("POST /api/calculate returns 400 for division by zero")
    void apiDivisionByZero() throws Exception {
        String body = """
                {
                  "firstNumber": 5.0,
                  "secondNumber": 0.0,
                  "operation": "DIVIDE"
                }
                """;

        mockMvc.perform(post("/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    @DisplayName("POST /api/calculate handles multiplication correctly")
    void apiMultiplication() throws Exception {
        String body = """
                {
                  "firstNumber": 6.0,
                  "secondNumber": 7.0,
                  "operation": "MULTIPLY"
                }
                """;

        mockMvc.perform(post("/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(42.0));
    }

    // ── MVC / Thymeleaf UI tests ───────────────────────────────────────────

    @Test
    @DisplayName("GET / renders the calculator form")
    void getIndexPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Simple Calculator")));
    }

    @Test
    @DisplayName("POST /calculate renders result in the page")
    void postCalculateForm() throws Exception {
        mockMvc.perform(post("/calculate")
                        .param("firstNumber", "10")
                        .param("secondNumber", "3")
                        .param("operation", Operation.SUBTRACT.name()))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("result"));
    }

    @Test
    @DisplayName("POST /calculate renders error on division by zero")
    void postCalculateDivisionByZero() throws Exception {
        mockMvc.perform(post("/calculate")
                        .param("firstNumber", "9")
                        .param("secondNumber", "0")
                        .param("operation", Operation.DIVIDE.name()))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("errorMessage"));
    }
}
