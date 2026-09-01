package com.example.calculator.api.support;

import static io.restassured.http.ContentType.JSON;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;

public class ApiClient {
    private final RequestSpecification requestSpecification;

    public ApiClient(String baseUri) {
        this.requestSpecification = new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setBasePath("/api/calculator")
                .setContentType(JSON)
                .build();
    }

    public Response calculate(Map<String, Object> request) {
        return RestAssured
                .given()
                .spec(requestSpecification)
                .body(request)
                .post("/calculate")
                .then()
                .extract()
                .response();
    }
}
