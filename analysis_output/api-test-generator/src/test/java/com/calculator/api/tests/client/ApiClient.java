package com.calculator.api.tests.client;

import com.calculator.api.tests.config.TestConfig;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

/**
 * Thin RestAssured wrapper that encapsulates all HTTP interactions with the
 * Calculator REST API.
 * <p>
 * A single instance is created per test run and shared across step definitions
 * via PicoContainer.  All public methods return a {@link Response} which the
 * caller is responsible for asserting on.
 * </p>
 */
public class ApiClient {

    private static final String CALCULATE_ENDPOINT = "/calculate";
    private static final String OPERATIONS_ENDPOINT = "/operations";

    private final TestConfig config;

    public ApiClient() {
        this.config = TestConfig.getInstance();
        configureRestAssured();
    }

    // ---- Configuration -------------------------------------------------------

    private void configureRestAssured() {
        RestAssured.baseURI = config.getBaseUrl();
        RestAssured.basePath = config.getApiPath();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    private RequestSpecification baseSpec() {
        RequestSpecification spec = RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
        if (config.isLoggingEnabled()) {
            spec = spec.log().ifValidationFails(LogDetail.ALL);
        }
        return spec;
    }

    // ---- API Operations ------------------------------------------------------

    /**
     * POST /api/v1/calculate — performs a calculation.
     *
     * @param requestBody map with keys {@code firstNumber}, {@code secondNumber}, {@code operation}
     * @return the raw RestAssured {@link Response}
     */
    public Response calculate(Map<String, Object> requestBody) {
        return baseSpec()
                .body(requestBody)
                .when()
                .post(CALCULATE_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    /**
     * POST /api/v1/calculate — performs a calculation with a raw JSON body.
     * Useful for testing invalid input that cannot be expressed as a typed map.
     *
     * @param rawJson raw JSON string to send as the request body
     * @return the raw RestAssured {@link Response}
     */
    public Response calculateRaw(String rawJson) {
        return baseSpec()
                .body(rawJson)
                .when()
                .post(CALCULATE_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    /**
     * GET /api/v1/operations — retrieves the list of supported operations.
     *
     * @return the raw RestAssured {@link Response}
     */
    public Response getOperations() {
        return baseSpec()
                .when()
                .get(OPERATIONS_ENDPOINT)
                .then()
                .extract()
                .response();
    }
}
