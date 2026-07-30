package com.calculator.api.test.context;

import io.restassured.response.Response;

/**
 * Thread-local test context that holds shared state between Cucumber step definitions.
 * One instance is created per scenario via PicoContainer dependency injection.
 */
public class TestContext {

    /** The most recent HTTP response from the API. */
    private Response lastResponse;

    /** The most recent HTTP request body sent to the API. */
    private String lastRequestBody;

    /** The most recent numeric result extracted from the response. */
    private Double lastResult;

    // -----------------------------------------------------------------------
    // Response
    // -----------------------------------------------------------------------

    public Response getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(Response lastResponse) {
        this.lastResponse = lastResponse;
    }

    // -----------------------------------------------------------------------
    // Request body
    // -----------------------------------------------------------------------

    public String getLastRequestBody() {
        return lastRequestBody;
    }

    public void setLastRequestBody(String lastRequestBody) {
        this.lastRequestBody = lastRequestBody;
    }

    // -----------------------------------------------------------------------
    // Result
    // -----------------------------------------------------------------------

    public Double getLastResult() {
        return lastResult;
    }

    public void setLastResult(Double lastResult) {
        this.lastResult = lastResult;
    }

    // -----------------------------------------------------------------------
    // Convenience helpers
    // -----------------------------------------------------------------------

    /** Returns the HTTP status code of the last response, or -1 if none. */
    public int getLastStatusCode() {
        return lastResponse != null ? lastResponse.getStatusCode() : -1;
    }

    /** Resets all state – called implicitly by PicoContainer between scenarios. */
    public void reset() {
        lastResponse = null;
        lastRequestBody = null;
        lastResult = null;
    }
}
