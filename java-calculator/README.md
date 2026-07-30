# Java Calculator API

A REST API calculator built with **Java 21** (current LTS) and **Spring Boot 3.4**.

This project is the Java counterpart to the Python Streamlit calculator in this repository,
demonstrating modern Java features and best practices.

## Java 21 Features Used

| Feature | Where |
|---|---|
| **Records** | `CalculationRequest`, `CalculationResponse` — immutable data carriers |
| **Switch expressions** | `CalculatorService` — exhaustive arithmetic dispatch |
| **Text blocks** | Javadoc examples |
| **Pattern matching** | `Operation.fromString()` — cleaner string-to-enum parsing |
| **`String::formatted`** | Expression building in `CalculatorService` |
| **`ProblemDetail` (RFC 9457)** | `GlobalExceptionHandler` — structured HTTP error responses |

## Prerequisites

- **Java 21** (Temurin / Eclipse Adoptium recommended)
- **Maven 3.9+**

## Build & Run

```bash
# compile and run all tests
JAVA_HOME=/usr/lib/jvm/temurin-21-jdk-amd64 mvn clean test

# package and start the server
JAVA_HOME=/usr/lib/jvm/temurin-21-jdk-amd64 mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.

## API Reference

### Health check

```
GET /api/v1/calculator/health
```

Response:
```json
{ "status": "UP", "service": "calculator-api" }
```

### Calculate

```
POST /api/v1/calculator/calculate
Content-Type: application/json
```

Request body:
```json
{
  "firstNumber":  10.0,
  "secondNumber": 3.0,
  "operation":    "DIVIDE"
}
```

Supported operations: `ADD`, `SUBTRACT`, `MULTIPLY`, `DIVIDE`
(aliases: `+`, `-`, `*`, `/` are also accepted)

Response:
```json
{
  "firstNumber":  10.0,
  "secondNumber": 3.0,
  "operation":    "DIVIDE",
  "result":       3.3333333333333335,
  "expression":   "10.0 ÷ 3.0 = 3.3333333333333335"
}
```

### Error responses (RFC 9457 Problem Detail)

Division by zero → `422 Unprocessable Entity`
```json
{
  "type":   "https://example.com/errors/arithmetic",
  "title":  "Arithmetic Error",
  "status": 422,
  "detail": "Division by zero is not allowed."
}
```

Unknown operation → `400 Bad Request`

## Project Structure

```
java-calculator/
├── pom.xml                          # Java 21 + Spring Boot 3.4
└── src/
    ├── main/java/com/example/calculator/
    │   ├── CalculatorApplication.java      # Spring Boot entry point
    │   ├── controller/
    │   │   ├── CalculatorController.java   # REST endpoints
    │   │   └── GlobalExceptionHandler.java # RFC 9457 error handling
    │   ├── model/
    │   │   ├── CalculationRequest.java     # record (Java 16+)
    │   │   ├── CalculationResponse.java    # record (Java 16+)
    │   │   └── Operation.java             # enum with switch expression
    │   └── service/
    │       └── CalculatorService.java      # arithmetic logic
    └── test/java/com/example/calculator/
        ├── CalculatorApplicationTests.java # context load test
        └── service/
            └── CalculatorServiceTest.java  # 14 unit tests
```
