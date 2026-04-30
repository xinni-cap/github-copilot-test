package com.calculator.api.test.builders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Fluent builder for {@code CalculationRequest} JSON payloads.
 *
 * <p>Usage:
 * <pre>{@code
 * String body = new CalculationRequestBuilder()
 *         .withNum1(10.0)
 *         .withNum2(5.0)
 *         .withOperation("Add")
 *         .build();
 * }</pre>
 */
public class CalculationRequestBuilder {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Double num1;
    private Double num2;
    private String operation;

    // Optional raw overrides for negative-test scenarios
    private String rawNum1;
    private String rawNum2;

    // -----------------------------------------------------------------------
    // Builder methods
    // -----------------------------------------------------------------------

    public CalculationRequestBuilder withNum1(double num1) {
        this.num1 = num1;
        this.rawNum1 = null;
        return this;
    }

    public CalculationRequestBuilder withNum2(double num2) {
        this.num2 = num2;
        this.rawNum2 = null;
        return this;
    }

    public CalculationRequestBuilder withOperation(String operation) {
        this.operation = operation;
        return this;
    }

    /**
     * Injects an arbitrary string as the {@code num1} value, bypassing numeric
     * parsing. Useful for testing input-validation error paths.
     */
    public CalculationRequestBuilder withRawNum1(String rawNum1) {
        this.rawNum1 = rawNum1;
        this.num1 = null;
        return this;
    }

    /**
     * Injects an arbitrary string as the {@code num2} value, bypassing numeric
     * parsing. Useful for testing input-validation error paths.
     */
    public CalculationRequestBuilder withRawNum2(String rawNum2) {
        this.rawNum2 = rawNum2;
        this.num2 = null;
        return this;
    }

    // -----------------------------------------------------------------------
    // Build
    // -----------------------------------------------------------------------

    /**
     * Serialises the request to a JSON string.
     *
     * @return a valid JSON string representing the calculation request
     * @throws IllegalStateException if Jackson serialisation fails
     */
    public String build() {
        ObjectNode node = MAPPER.createObjectNode();

        if (rawNum1 != null) {
            node.put("num1", rawNum1);
        } else if (num1 != null) {
            node.put("num1", num1);
        }

        if (rawNum2 != null) {
            node.put("num2", rawNum2);
        } else if (num2 != null) {
            node.put("num2", num2);
        }

        if (operation != null) {
            node.put("operation", operation);
        }

        try {
            return MAPPER.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialise CalculationRequest", e);
        }
    }

    // -----------------------------------------------------------------------
    // Factory / preset helpers
    // -----------------------------------------------------------------------

    /** Creates a pre-filled builder for an addition request. */
    public static CalculationRequestBuilder addRequest(double num1, double num2) {
        return new CalculationRequestBuilder().withNum1(num1).withNum2(num2).withOperation("Add");
    }

    /** Creates a pre-filled builder for a subtraction request. */
    public static CalculationRequestBuilder subtractRequest(double num1, double num2) {
        return new CalculationRequestBuilder().withNum1(num1).withNum2(num2).withOperation("Subtract");
    }

    /** Creates a pre-filled builder for a multiplication request. */
    public static CalculationRequestBuilder multiplyRequest(double num1, double num2) {
        return new CalculationRequestBuilder().withNum1(num1).withNum2(num2).withOperation("Multiply");
    }

    /** Creates a pre-filled builder for a division request. */
    public static CalculationRequestBuilder divideRequest(double num1, double num2) {
        return new CalculationRequestBuilder().withNum1(num1).withNum2(num2).withOperation("Divide");
    }
}
