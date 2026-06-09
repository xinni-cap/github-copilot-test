package com.example.calculator.api.support;

import io.restassured.response.Response;
import java.util.Map;

public class TestContext {
    private static MockCalculatorApiServer server;

    private ApiClient apiClient;
    private Map<String, Object> request;
    private Response response;

    public void ensureApiIsAvailable() {
        ensureServerStarted();
        this.apiClient = new ApiClient(server.baseUri());
    }

    public static synchronized void ensureServerStarted() {
        if (server == null) {
            server = new MockCalculatorApiServer();
            server.start();
        }
    }

    public static synchronized void stopServer() {
        if (server != null) {
            server.stop();
            server = null;
        }
    }

    public ApiClient apiClient() {
        return apiClient;
    }

    public Map<String, Object> request() {
        return request;
    }

    public void setRequest(Map<String, Object> request) {
        this.request = request;
    }

    public Response response() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
