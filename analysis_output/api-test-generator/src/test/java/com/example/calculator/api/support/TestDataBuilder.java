package com.example.calculator.api.support;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public final class TestDataBuilder {
    private TestDataBuilder() {
    }

    public static Map<String, Object> calculationRequest(BigDecimal firstNumber, BigDecimal secondNumber, String operation) {
        Map<String, Object> request = new LinkedHashMap<>();
        request.put("firstNumber", firstNumber);
        request.put("secondNumber", secondNumber);
        request.put("operation", operation);
        return request;
    }
}
