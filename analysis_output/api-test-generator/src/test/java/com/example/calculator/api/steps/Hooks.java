package com.example.calculator.api.steps;

import com.example.calculator.api.support.TestContext;
import io.cucumber.java.AfterAll;

public class Hooks {
    @AfterAll
    public static void stopCalculatorApi() {
        TestContext.stopServer();
    }
}
