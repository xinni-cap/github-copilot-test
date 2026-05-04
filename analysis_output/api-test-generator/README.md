# Calculator API Tests — README

## Overview

This module provides a comprehensive BDD test suite for the **Calculator REST API** using:

| Tool | Purpose |
|------|---------|
| [Cucumber 7](https://cucumber.io/) | BDD scenario runner (Gherkin feature files) |
| [RestAssured 5](https://rest-assured.io/) | HTTP client for API assertions |
| [JUnit 5](https://junit.org/junit5/) | Test engine & reporting |
| [AssertJ](https://assertj.github.io/doc/) | Fluent assertion library |
| [PicoContainer](https://picocontainer.com/) | Dependency injection between step classes |

---

## Project Structure

```
src/
└── test/
    ├── java/com/calculator/api/tests/
    │   ├── config/
    │   │   └── TestConfig.java          # Centralised configuration (base URL, timeouts)
    │   ├── context/
    │   │   └── TestContext.java         # Per-scenario shared state holder
    │   ├── client/
    │   │   └── ApiClient.java           # RestAssured API client wrapper
    │   ├── builder/
    │   │   └── TestDataBuilder.java     # Fluent builder for request payloads
    │   ├── steps/
    │   │   ├── CalculatorSteps.java     # Main step definitions (Given/When/Then)
    │   │   └── CommonSteps.java         # Cucumber @Before/@After hooks
    │   └── runners/
    │       └── CucumberTestRunner.java  # JUnit 5 suite runner
    └── resources/
        ├── features/
        │   ├── calculator_operations.feature   # Happy-path & outline scenarios
        │   └── calculator_edge_cases.feature   # Error handling & boundary scenarios
        ├── application.properties              # Default configuration values
        └── cucumber.properties                 # Cucumber plugin & glue config
```

---

## Prerequisites

- Java 17 or later
- Maven 3.8 or later
- A running instance of the Calculator REST API (see [API Requirements](#api-requirements) below)

---

## Configuration

The API base URL defaults to `http://localhost:8080`. Override it with:

| Method | Example |
|--------|---------|
| Maven system property | `mvn test -Dapi.base.url=http://staging.example.com` |
| Environment variable | `export API_BASE_URL=http://staging.example.com` |
| Edit `application.properties` | Change `api.base.url=...` |

All configuration keys:

| Key | Default | Description |
|-----|---------|-------------|
| `api.base.url` | `http://localhost:8080` | Base URL of the API under test |
| `api.path` | `/api/v1` | Path prefix for all endpoints |
| `api.response.timeout.ms` | `2000` | Max acceptable response time (ms) |
| `api.logging.enabled` | `true` | Enable request/response logging |

---

## Running the Tests

### Run all tests

```bash
mvn test
```

### Run only smoke tests

```bash
mvn test -Dcucumber.filter.tags="@smoke"
```

### Run tests by operation type

```bash
# Addition tests only
mvn test -Dcucumber.filter.tags="@addition"

# Error handling tests only
mvn test -Dcucumber.filter.tags="@error"

# Boundary tests only
mvn test -Dcucumber.filter.tags="@boundary"
```

### Run against a remote server

```bash
mvn test -Dapi.base.url=https://api.example.com
```

---

## Available Tags

| Tag | Description |
|-----|-------------|
| `@smoke` | Critical happy-path tests — run before every deployment |
| `@addition` | Tests specific to the ADD operation |
| `@subtraction` | Tests specific to the SUBTRACT operation |
| `@multiplication` | Tests specific to the MULTIPLY operation |
| `@division` | Tests specific to the DIVIDE operation |
| `@error` | Tests for error conditions and validation failures |
| `@validation` | Tests for request validation errors |
| `@boundary` | Tests for boundary and extreme value inputs |
| `@batch` | Scenario outline tests covering multiple inputs |
| `@operations` | Tests for the /operations endpoint |
| `@performance` | Tests asserting response-time SLAs |
| `@content-type` | Tests asserting correct Content-Type headers |
| `@response-structure` | Tests verifying the shape of JSON responses |
| `@workflow` | Multi-step workflow / integration-style tests |

---

## Test Reports

After a test run, reports are generated in `target/cucumber-reports/`:

| Report | Path |
|--------|------|
| HTML (human-readable) | `target/cucumber-reports/report.html` |
| JSON (CI integration) | `target/cucumber-reports/report.json` |
| JUnit XML (CI integration) | `target/cucumber-reports/report.xml` |

---

## API Requirements

The tests expect the following REST API to be running and accessible:

### `POST /api/v1/calculate`

**Request body**

```json
{
  "firstNumber": 10.0,
  "secondNumber": 5.0,
  "operation": "ADD"
}
```

**Successful response (200)**

```json
{
  "result": 15.0,
  "firstNumber": 10.0,
  "secondNumber": 5.0,
  "operation": "ADD",
  "expression": "10.0 + 5.0 = 15.0"
}
```

**Error response (400)**

```json
{
  "error": "Division by zero is not allowed"
}
```

Supported operations: `ADD`, `SUBTRACT`, `MULTIPLY`, `DIVIDE`

### `GET /api/v1/operations`

**Response (200)**

```json
["ADD", "SUBTRACT", "MULTIPLY", "DIVIDE"]
```

---

## Scenarios Coverage

| Feature File | Scenarios | Tags |
|---|---|---|
| `calculator_operations.feature` | 19 | smoke, addition, subtraction, multiplication, division, batch, operations |
| `calculator_edge_cases.feature` | 13 | error, validation, boundary, performance, content-type, response-structure, workflow |

**Total: 32 scenarios**

---

## Extending the Tests

1. **Add a new feature file** under `src/test/resources/features/`.
2. **Add step definitions** in `src/test/java/.../steps/` — PicoContainer will inject `TestContext` and `ApiClient` automatically.
3. **Add new configuration** in `TestConfig.java` and `application.properties`.
4. Tag new scenarios consistently so they can be run selectively.
