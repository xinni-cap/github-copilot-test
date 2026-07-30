package com.example.calculator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getCalculator_returns200WithForm() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("calculator"))
                .andExpect(model().attributeExists("calculatorRequest"));
    }

    @Test
    void postCalculator_validAddition_returnsResult() throws Exception {
        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("num1", "3.5")
                        .param("num2", "1.5")
                        .param("operation", "Add"))
                .andExpect(status().isOk())
                .andExpect(view().name("calculator"))
                .andExpect(model().attributeExists("result"))
                .andExpect(model().attributeDoesNotExist("errorMessage"));
    }

    @Test
    void postCalculator_validSubtraction_returnsResult() throws Exception {
        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("num1", "10.0")
                        .param("num2", "4.0")
                        .param("operation", "Subtract"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("result"));
    }

    @Test
    void postCalculator_validMultiplication_returnsResult() throws Exception {
        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("num1", "2.5")
                        .param("num2", "4.0")
                        .param("operation", "Multiply"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("result"));
    }

    @Test
    void postCalculator_validDivision_returnsResult() throws Exception {
        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("num1", "9.0")
                        .param("num2", "3.0")
                        .param("operation", "Divide"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("result"));
    }

    @Test
    void postCalculator_divideByZero_showsErrorMessage() throws Exception {
        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("num1", "5.0")
                        .param("num2", "0.0")
                        .param("operation", "Divide"))
                .andExpect(status().isOk())
                .andExpect(view().name("calculator"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attributeDoesNotExist("result"));
    }

    @Test
    void postCalculator_missingNum1_showsValidationError() throws Exception {
        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("num2", "2.0")
                        .param("operation", "Add"))
                .andExpect(status().isOk())
                .andExpect(view().name("calculator"))
                .andExpect(model().attributeHasFieldErrors("calculatorRequest", "num1"));
    }

    @Test
    void postCalculator_missingOperation_showsValidationError() throws Exception {
        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("num1", "1.0")
                        .param("num2", "2.0")
                        .param("operation", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("calculator"))
                .andExpect(model().attributeHasFieldErrors("calculatorRequest", "operation"));
    }
}
