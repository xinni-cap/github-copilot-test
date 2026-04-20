# Calculator API – Cucumber BDD Test Suite

Comprehensive API test suite for the **Calculator REST API** using
[Cucumber](https://cucumber.io/) (BDD / Gherkin) and
[RestAssured](https://rest-assured.io/) (HTTP assertions).

---

## Prerequisites

| Tool | Version |
|------|---------|
| Java | 17+ |
| Maven | 3.9+ |
| Calculator API service | running on `http://localhost:8080` (configurable) |

---

## Project Structure

```
analysis_output/api-test-generator/
├── pom.xml                              # Maven build descriptor
└── src/
    └── test/
        ├── java/com/calculator/api/test/
        │   ├── CucumberRunnerTest.java  # JUnit 5 Cucumber runner
        │   ├── config/
        │   │   └── TestConfig.java      # Base URL / timeout config
        │   ├── context/
        │   │   └── TestContext.java     # Per-scenario shared state
        │   ├── client/
        │   │   └── CalculatorApiClient.java  # RestAssured API wrapper
        │   ├── builders/
        │   │   └── CalculationRequestBuilder.java  # Request builder
        │   └── steps/
        │       ├── CommonSteps.java     # @Given steps (availability check)
        │       └── CalculationSteps.java # @When/@Then calculation steps
        └── resources/
            ├── cucumber.properties      # Cucumber plugin / glue config
            ├── test-config.properties   # Base URL & timeout defaults
            ├── logback-test.xml         # Logging config
            └── features/
                ├── calculator/
                │   ├── add.feature           # Addition scenarios
                │   ├── subtract.feature      # Subtraction scenarios
                │   ├── multiply.feature      # Multiplication scenarios
                │   ├── divide.feature        # Division scenarios
                │   └── error_handling.feature # Error / negative scenarios
                └── integration/
                    └── calculator_workflow.feature  # End-to-end workflows
```

---

## Running the Tests

### Run all tests
```bash
cd analysis_output/api-test-generator
mvn test
```

### Run tests with a specific tag
```bash
# Run only addition tests
mvn test -Dcucumber.filter.tags="@addition"

# Run all calculator tests
mvn test -Dcucumber.filter.tags="@calculator"

# Run error-handling tests
mvn test -Dcucumber.filter.tags="@error-handling"

# Run integration / workflow tests
mvn test -Dcucumber.filter.tags="@workflow"

# Combine tags
mvn test -Dcucumber.filter.tags="@addition or @subtraction"

# Exclude a tag
mvn test -Dcucumber.filter.tags="@calculator and not @negative-tests"
```

### Run against a non-default API host
```bash
export CALCULATOR_API_BASE_URL=http://my-staging-server:8080
mvn test
```

Or as a Maven system property:
```bash
mvn test -DCALCULATOR_API_BASE_URL=http://my-staging-server:8080
```

### Generate HTML report
```bash
mvn verify
# Report written to: target/cucumber-html-reports/
```

---

## Configuration

Edit `src/test/resources/test-config.properties` to change defaults:

```properties
api.base.url=http://localhost:8080
api.version=/api/v1
api.timeout.connection=5000
api.timeout.read=10000
```

All properties can be overridden at runtime via **environment variables** or
**Maven `-D` flags** (see `TestConfig.java`).

---

## Expected API Contract

The tests assume the Calculator REST API exposes the following endpoint:

### `POST /api/v1/calculate`

**Request body (JSON)**
```json
{
  "num1": 10.0,
  "num2": 5.0,
  "operation": "Add"
}
```

Supported operations: `Add`, `Subtract`, `Multiply`, `Divide`

**Success response (HTTP 200)**
```json
{
  "result": 15.0,
  "expression": "10.0 + 5.0 = 15.0"
}
```

**Error response (HTTP 400)**
```json
{
  "message": "Division by zero is not allowed.",
  "code": "DIVISION_BY_ZERO"
}
```

### `GET /actuator/health`

Used as the availability probe in the `Background` step of each feature.  
Returns HTTP 200 (healthy) or 503 (degraded).

---

## Feature Coverage

| Feature file | Scenarios | Tags |
|---|---|---|
| `add.feature` | 7 (+ outline with 5 rows) | `@addition` |
| `subtract.feature` | 7 (+ outline with 4 rows) | `@subtraction` |
| `multiply.feature` | 7 (+ outline with 4 rows) | `@multiplication` |
| `divide.feature` | 9 (+ outline with 4 rows) | `@division` |
| `error_handling.feature` | 9 | `@error-handling @negative-tests` |
| `calculator_workflow.feature` | 4 (multi-step) | `@workflow @integration` |

---

## Business Workflows Covered

The tests are based on the following business workflows derived from the
Calculator application:

1. **Basic Arithmetic Workflow** – user supplies two numbers and an operation;
   the system returns the result.
2. **Tax Calculation Workflow** – multiply base price by tax rate, then add to
   get final price.
3. **Split Bill Workflow** – sum individual costs, then divide by number of
   people.
4. **Interest Calculation Workflow** – compute simple interest and accumulate
   total.
5. **Error Recovery Workflow** – demonstrates API behaviour after a validation
   error.

---

## Data Model (DDL Reference)

The test data is based on the following logical schema:

```sql
-- Calculation request
CREATE TABLE calculation_request (
    id          BIGINT        NOT NULL PRIMARY KEY AUTO_INCREMENT,
    num1        DOUBLE        NOT NULL,
    num2        DOUBLE        NOT NULL,
    operation   VARCHAR(20)   NOT NULL CHECK (operation IN ('Add','Subtract','Multiply','Divide')),
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Calculation result
CREATE TABLE calculation_result (
    id          BIGINT        NOT NULL PRIMARY KEY AUTO_INCREMENT,
    request_id  BIGINT        NOT NULL REFERENCES calculation_request(id),
    result      DOUBLE        NOT NULL,
    expression  VARCHAR(255)  NOT NULL,
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Error log
CREATE TABLE calculation_error (
    id          BIGINT        NOT NULL PRIMARY KEY AUTO_INCREMENT,
    request_id  BIGINT        REFERENCES calculation_request(id),
    error_code  VARCHAR(50)   NOT NULL,
    message     VARCHAR(500)  NOT NULL,
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```
