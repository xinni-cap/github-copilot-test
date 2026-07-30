# Calculator App

A simple calculator that performs Add, Subtract, Multiply, and Divide operations.
Two implementations are provided side-by-side:

| | Python (Streamlit) | Java (Spring Boot) |
|---|---|---|
| Language | Python 3 | Java 17 |
| Framework | Streamlit | Spring Boot 3.4.x + Thymeleaf |
| Entry point | `app.py` | `CalculatorApplication.java` |
| Default port | 8501 | 8080 |

---

## Java Spring Boot Application

### Prerequisites

- Java 17+ (OpenJDK or Temurin)
- Apache Maven 3.9+

### Build & Test

```bash
mvn test
```

### Run

```bash
mvn spring-boot:run
```

Then open <http://localhost:8080> in your browser.

### Project layout

```
pom.xml
src/
  main/
    java/com/example/calculator/
      CalculatorApplication.java   # Spring Boot entry point
      CalculatorController.java    # MVC controller (GET / + POST /calculate)
      CalculatorService.java       # Arithmetic logic
      model/
        CalculatorRequest.java     # Form-binding model
        CalculatorResult.java      # Computation result
    resources/
      templates/index.html         # Thymeleaf UI
      application.properties
  test/
    java/com/example/calculator/
      CalculatorServiceTest.java   # Unit tests (8 cases)
      CalculatorControllerTest.java# MockMvc integration tests (6 cases)
```

---

## Python Streamlit Application

### Prerequisites

- Python 3.8+

### Setup

1. Create and activate a virtual environment (optional but recommended):
   - `python3 -m venv .venv`
   - `source .venv/bin/activate`
2. Install dependencies:
   - `pip install -r requirements.txt`

### Run

```bash
streamlit run app.py
```

Then open the local URL shown in the terminal (usually <http://localhost:8501>).