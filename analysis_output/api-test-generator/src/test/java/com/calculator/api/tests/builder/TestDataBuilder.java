package com.calculator.api.tests.builder;

import java.util.HashMap;
import java.util.Map;

/**
 * Builder for constructing Calculator API request payloads.
 * <p>
 * Provides a fluent API for assembling valid and intentionally-invalid
 * request bodies used in Cucumber step definitions and scenario outlines.
 * </p>
 *
 * <pre>{@code
 * Map<String, Object> request = TestDataBuilder.aCalculation()
 *     .withFirstNumber(10.0)
 *     .withSecondNumber(5.0)
 *     .withOperation("ADD")
 *     .build();
 * }</pre>
 */
public class TestDataBuilder {

    private final Map<String, Object> fields = new HashMap<>();

    private TestDataBuilder() {
    }

    /** Entry point — creates a new builder for a calculation request. */
    public static TestDataBuilder aCalculation() {
        return new TestDataBuilder();
    }

    /**
     * Sets the {@code firstNumber} field.
     *
     * @param number the first operand
     * @return this builder
     */
    public TestDataBuilder withFirstNumber(double number) {
        fields.put("firstNumber", number);
        return this;
    }

    /**
     * Sets the {@code secondNumber} field.
     *
     * @param number the second operand
     * @return this builder
     */
    public TestDataBuilder withSecondNumber(double number) {
        fields.put("secondNumber", number);
        return this;
    }

    /**
     * Sets the {@code operation} field.
     *
     * @param operation one of {@code ADD}, {@code SUBTRACT}, {@code MULTIPLY}, {@code DIVIDE}
     * @return this builder
     */
    public TestDataBuilder withOperation(String operation) {
        fields.put("operation", operation);
        return this;
    }

    /**
     * Omits the {@code firstNumber} field to trigger validation errors.
     *
     * @return this builder
     */
    public TestDataBuilder withoutFirstNumber() {
        fields.remove("firstNumber");
        return this;
    }

    /**
     * Omits the {@code secondNumber} field to trigger validation errors.
     *
     * @return this builder
     */
    public TestDataBuilder withoutSecondNumber() {
        fields.remove("secondNumber");
        return this;
    }

    /**
     * Omits the {@code operation} field to trigger validation errors.
     *
     * @return this builder
     */
    public TestDataBuilder withoutOperation() {
        fields.remove("operation");
        return this;
    }

    /**
     * Builds the request body as an unmodifiable {@link Map}.
     *
     * @return a copy of the accumulated fields
     */
    public Map<String, Object> build() {
        return new HashMap<>(fields);
    }

    // ---- Convenience factory methods -----------------------------------------

    /** Returns a fully valid ADD request. */
    public static Map<String, Object> addRequest(double a, double b) {
        return aCalculation().withFirstNumber(a).withSecondNumber(b).withOperation("ADD").build();
    }

    /** Returns a fully valid SUBTRACT request. */
    public static Map<String, Object> subtractRequest(double a, double b) {
        return aCalculation().withFirstNumber(a).withSecondNumber(b).withOperation("SUBTRACT").build();
    }

    /** Returns a fully valid MULTIPLY request. */
    public static Map<String, Object> multiplyRequest(double a, double b) {
        return aCalculation().withFirstNumber(a).withSecondNumber(b).withOperation("MULTIPLY").build();
    }

    /** Returns a fully valid DIVIDE request. */
    public static Map<String, Object> divideRequest(double a, double b) {
        return aCalculation().withFirstNumber(a).withSecondNumber(b).withOperation("DIVIDE").build();
    }
}
