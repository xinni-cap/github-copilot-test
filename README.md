# Simple Calculator — Java Spring Boot 3.4

A production-ready web calculator built with **Spring Boot 3.4** and **Thymeleaf**, converted from the original Python/Streamlit implementation.

---

## Features

- Two floating-point number inputs (up to 6 decimal places)
- Four arithmetic operations: **Add (+)**, **Subtract (−)**, **Multiply (×)**, **Divide (÷)**
- Result display showing the full expression (e.g. `3.5 + 1.5 = 5`)
- Collapsible **Computation Details** section (first number, second number, operation, result)
- Division-by-zero error handling — shows a clear error message without crashing
- Bean Validation for form inputs
- Clean, responsive Bootstrap 5 UI

---

## Prerequisites

| Tool | Version |
|------|---------|
| Java | 17 (OpenJDK / Temurin) |
| Maven | 3.9+ |

---

## Build

```bash
mvn clean package
```

All unit and integration tests will run automatically during the build.

---

## Run

**Using Maven Spring Boot plugin:**
```bash
mvn spring-boot:run
```

**Using the packaged JAR:**
```bash
java -jar target/calculator-1.0.0.jar
```

---

## Access

Open your browser at: [http://localhost:8080](http://localhost:8080)

---

## Project Structure

```
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/example/calculator/
│   │   │   ├── CalculatorApplication.java       # Spring Boot entry point
│   │   │   ├── controller/
│   │   │   │   └── CalculatorController.java    # Web MVC controller (GET & POST /)
│   │   │   ├── model/
│   │   │   │   ├── CalculatorRequest.java       # Form input model (with validation)
│   │   │   │   └── CalculatorResult.java        # Computation result model
│   │   │   └── service/
│   │   │       └── CalculatorService.java       # Core arithmetic logic
│   │   └── resources/
│   │       ├── templates/
│   │       │   └── calculator.html              # Thymeleaf template (Bootstrap 5)
│   │       └── application.properties
│   └── test/
│       └── java/com/example/calculator/
│           ├── CalculatorServiceTest.java        # Unit tests for service
│           └── CalculatorControllerTest.java     # MockMvc integration tests
└── README.md
```

---

## Technology Stack

| Layer | Technology |
|-------|-----------|
| Framework | Spring Boot 3.4.1 |
| Web | Spring MVC |
| Templating | Thymeleaf |
| Validation | Jakarta Bean Validation |
| Styling | Bootstrap 5.3 (CDN) |
| Testing | JUnit 5, MockMvc |
| Build | Maven 3.9+ |
