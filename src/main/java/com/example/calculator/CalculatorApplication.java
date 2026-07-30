package com.example.calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Calculator Spring Boot application.
 *
 * <p>This application is a Java 21 + Spring Boot 3 modernisation of the original
 * Python/Streamlit calculator.  It exposes the same four arithmetic operations
 * (add, subtract, multiply, divide) through a Thymeleaf-rendered web UI and a
 * companion REST API.
 */
@SpringBootApplication
public class CalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalculatorApplication.class, args);
    }
}
