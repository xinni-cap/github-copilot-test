package com.example.calculator.api.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MockCalculatorApiServer {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final int DIVISION_SCALE = 6;

    private final HttpServer server;
    private final ExecutorService executorService;

    public MockCalculatorApiServer() {
        try {
            this.server = HttpServer.create(new InetSocketAddress(0), 0);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to create calculator API server", exception);
        }
        this.server.createContext("/api/calculator/calculate", this::handleCalculation);
        this.executorService = Executors.newSingleThreadExecutor();
        this.server.setExecutor(executorService);
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException exception) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public String baseUri() {
        return "http://localhost:" + server.getAddress().getPort();
    }

    private void handleCalculation(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            writeJson(exchange, 405, Map.of("error", "Method not allowed"));
            return;
        }

        try (InputStream requestBody = exchange.getRequestBody()) {
            CalculationRequest request = OBJECT_MAPPER.readValue(requestBody, CalculationRequest.class);
            BigDecimal result = calculate(request);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("firstNumber", request.firstNumber());
            response.put("secondNumber", request.secondNumber());
            response.put("operation", request.operation());
            response.put("result", result);
            writeJson(exchange, 200, response);
        } catch (DivisionByZeroException exception) {
            writeJson(exchange, 400, Map.of("error", exception.getMessage()));
        }
    }

    private BigDecimal calculate(CalculationRequest request) {
        return switch (request.operation()) {
            case "Add" -> request.firstNumber().add(request.secondNumber());
            case "Subtract" -> request.firstNumber().subtract(request.secondNumber());
            case "Multiply" -> request.firstNumber().multiply(request.secondNumber());
            case "Divide" -> divide(request.firstNumber(), request.secondNumber());
            default -> throw new IllegalArgumentException("Unsupported operation: " + request.operation());
        };
    }

    private BigDecimal divide(BigDecimal firstNumber, BigDecimal secondNumber) {
        if (BigDecimal.ZERO.compareTo(secondNumber) == 0) {
            throw new DivisionByZeroException("Division by zero is not allowed.");
        }
        return firstNumber.divide(secondNumber, DIVISION_SCALE, RoundingMode.HALF_UP).stripTrailingZeros();
    }

    private void writeJson(HttpExchange exchange, int statusCode, Map<String, ?> payload) throws IOException {
        byte[] responseBody = OBJECT_MAPPER.writeValueAsString(payload).getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, responseBody.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(responseBody);
        }
    }

    private record CalculationRequest(BigDecimal firstNumber, BigDecimal secondNumber, String operation) {
    }

    private static final class DivisionByZeroException extends RuntimeException {
        private DivisionByZeroException(String message) {
            super(message);
        }
    }
}
