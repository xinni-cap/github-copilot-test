# Generated API test suite

This directory contains a self-contained Cucumber + RestAssured test suite for the calculator behavior inferred from `/home/runner/work/github-copilot-test/github-copilot-test/app.py`.

## Included assets
- `src/test/resources/features/calculator-api.feature` — Gherkin scenarios for calculator operations and validation
- `src/test/java/com/example/calculator/api/steps/CalculatorApiSteps.java` — step definitions
- `src/test/java/com/example/calculator/api/support/TestContext.java` — shared scenario state
- `src/test/java/com/example/calculator/api/support/ApiClient.java` — RestAssured client wrapper
- `src/test/java/com/example/calculator/api/support/TestDataBuilder.java` — request payload builder
- `src/test/java/com/example/calculator/api/support/MockCalculatorApiServer.java` — executable local API stub matching the calculator rules
- `docs/calculator-workflow.md` — inferred workflow
- `docs/calculator-test-data.sql` — inferred test-data model

## Inferred API contract
- **Endpoint:** `POST /api/calculator/calculate`
- **Request body:**
  ```json
  {
    "firstNumber": 12.5,
    "secondNumber": 7.5,
    "operation": "Add"
  }
  ```
- **Success response:** HTTP `200`
  ```json
  {
    "firstNumber": 12.5,
    "secondNumber": 7.5,
    "operation": "Add",
    "result": 20.0
  }
  ```
- **Validation response:** HTTP `400`
  ```json
  {
    "error": "Division by zero is not allowed."
  }
  ```

## Execute the suite
From this directory run:

```bash
mvn test
```

The suite boots a lightweight local HTTP server during execution so it can be run immediately, even though the repository currently exposes calculator behavior through Streamlit rather than a REST controller.
