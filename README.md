# Streamlit Calculator App

A simple calculator web app built with Streamlit.

## Setup

1. Create and activate a virtual environment (optional but recommended):
	- `python3 -m venv .venv`
	- `source .venv/bin/activate`
2. Install dependencies:
	- `pip install -r requirements.txt`

## Run

Start the app with:

`streamlit run app.py`

Then open the local URL shown in the terminal (usually `http://localhost:8501`).

---

## Java Calculator

A Java 21 command-line equivalent of the Streamlit calculator, located in the
[`java-calculator/`](java-calculator/) directory.

### Requirements

- Java 21 (LTS) — [Eclipse Temurin](https://adoptium.net/) recommended
- Apache Maven 3.9+

### Build & test

```bash
cd java-calculator
mvn clean verify
```

### Run

```bash
mvn package -DskipTests
java -jar target/java-calculator-1.0.0.jar
```

### Project structure

```
java-calculator/
├── pom.xml
└── src/
    ├── main/java/com/example/calculator/
    │   ├── Calculator.java   # Core arithmetic operations
    │   └── Main.java         # CLI entry point
    └── test/java/com/example/calculator/
        └── CalculatorTest.java
```