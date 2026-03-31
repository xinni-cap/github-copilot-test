package com.example.calculator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CalculatorController.class)
@Import(CalculatorService.class)
class CalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getIndex_returnsCalculatorPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Simple Calculator")));
    }

    @Test
    void postCalculate_add_displaysResult() throws Exception {
        mockMvc.perform(post("/calculate")
                        .param("num1", "3")
                        .param("num2", "4")
                        .param("operation", "Add"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("7.0")));
    }

    @Test
    void postCalculate_subtract_displaysResult() throws Exception {
        mockMvc.perform(post("/calculate")
                        .param("num1", "10")
                        .param("num2", "3")
                        .param("operation", "Subtract"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("7.0")));
    }

    @Test
    void postCalculate_multiply_displaysResult() throws Exception {
        mockMvc.perform(post("/calculate")
                        .param("num1", "6")
                        .param("num2", "7")
                        .param("operation", "Multiply"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("42.0")));
    }

    @Test
    void postCalculate_divide_displaysResult() throws Exception {
        mockMvc.perform(post("/calculate")
                        .param("num1", "10")
                        .param("num2", "4")
                        .param("operation", "Divide"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("2.5")));
    }

    @Test
    void postCalculate_divideByZero_displaysError() throws Exception {
        mockMvc.perform(post("/calculate")
                        .param("num1", "5")
                        .param("num2", "0")
                        .param("operation", "Divide"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("zero")));
    }
}
