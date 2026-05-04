package com.calculator.api.tests.context;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Shared test context that is created fresh for each Cucumber scenario.
 * <p>
 * Holds state such as the last HTTP request/response, stored intermediate
 * results, and any scenario-scoped data that step definitions need to share.
 * PicoContainer injects this into every step definition class automatically.
 * </p>
 */
public class TestContext {

    // ---- Request state -------------------------------------------------------

    /** Raw JSON body used when a step builds the request manually. */
    private String rawRequestBody;

    /** Structured calculation request (built via TestDataBuilder). */
    private Map<String, Object> calculationRequest = new HashMap<>();

    // ---- Response state ------------------------------------------------------

    /** The RestAssured response from the most recent API call. */
    private Response lastResponse;

    // ---- Workflow state ------------------------------------------------------

    /**
     * Running numeric result used when chaining multiple calculations in a
     * single scenario (e.g., the workflow scenario in edge_cases.feature).
     */
    private Double chainedResult;

    // ---- Accessors -----------------------------------------------------------

    public String getRawRequestBody() {
        return rawRequestBody;
    }

    public void setRawRequestBody(String rawRequestBody) {
        this.rawRequestBody = rawRequestBody;
    }

    public Map<String, Object> getCalculationRequest() {
        return calculationRequest;
    }

    public void setCalculationRequest(Map<String, Object> calculationRequest) {
        this.calculationRequest = calculationRequest;
    }

    public void setRequestField(String key, Object value) {
        this.calculationRequest.put(key, value);
    }

    public Response getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(Response lastResponse) {
        this.lastResponse = lastResponse;
    }

    public Double getChainedResult() {
        return chainedResult;
    }

    public void setChainedResult(Double chainedResult) {
        this.chainedResult = chainedResult;
    }

    /** Resets mutable state between scenarios (called from a Cucumber hook). */
    public void reset() {
        rawRequestBody = null;
        calculationRequest = new HashMap<>();
        lastResponse = null;
        chainedResult = null;
    }
}
