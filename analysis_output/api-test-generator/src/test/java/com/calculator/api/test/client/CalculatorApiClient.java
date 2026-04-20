package com.calculator.api.test.client;

import com.calculator.api.test.config.TestConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thin wrapper around RestAssured for the Calculator REST API.
 *
 * <p>All requests are routed through this client so that base-URL and
 * common header configuration is centralised in one place.
 */
public class CalculatorApiClient {

    private static final Logger log = LoggerFactory.getLogger(CalculatorApiClient.class);

    private static final String CALCULATE_ENDPOINT = "/calculate";

    private final TestConfig config;

    public CalculatorApiClient() {
        this.config = TestConfig.getInstance();
        configureRestAssured();
    }

    // -----------------------------------------------------------------------
    // API methods
    // -----------------------------------------------------------------------

    /**
     * POST /api/v1/calculate with the supplied JSON body.
     *
     * @param requestBody the serialised JSON payload
     * @return the raw RestAssured {@link Response}
     */
    public Response calculate(String requestBody) {
        log.debug("POST {} body={}", calculateUrl(), requestBody);
        Response response = baseRequest()
                .body(requestBody)
                .post(calculateUrl());
        log.debug("Response status={} body={}", response.getStatusCode(), response.asString());
        return response;
    }

    /**
     * POST /api/v1/calculate with an empty body (used for negative testing).
     *
     * @return the raw RestAssured {@link Response}
     */
    public Response calculateEmpty() {
        log.debug("POST {} (empty body)", calculateUrl());
        Response response = baseRequest()
                .post(calculateUrl());
        log.debug("Response status={} body={}", response.getStatusCode(), response.asString());
        return response;
    }

    /**
     * GET /actuator/health – used as the API availability probe.
     *
     * @return the raw RestAssured {@link Response}
     */
    public Response healthCheck() {
        log.debug("GET {}/actuator/health", config.getBaseUrl());
        return RestAssured.given()
                .baseUri(config.getBaseUrl())
                .connectionTimeout(config.getConnectionTimeout())
                .readTimeout(config.getReadTimeout())
                .get("/actuator/health");
    }

    // -----------------------------------------------------------------------
    // Internal helpers
    // -----------------------------------------------------------------------

    private RequestSpecification baseRequest() {
        return RestAssured.given()
                .baseUri(config.getBaseUrl())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .connectionTimeout(config.getConnectionTimeout())
                .readTimeout(config.getReadTimeout());
    }

    private String calculateUrl() {
        return config.getApiVersion() + CALCULATE_ENDPOINT;
    }

    private void configureRestAssured() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
