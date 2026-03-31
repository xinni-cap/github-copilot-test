# Java Calculator

A modern **Java 21** calculator application that mirrors the functionality of the
Streamlit Python calculator (`app.py`) in the parent directory.

## Features

| Feature | Details |
|---|---|
| **Language** | Java 21 LTS |
| **Build** | Maven 3.9+ |
| **Testing** | JUnit 5 (Jupiter) |
| **Operations** | Add, Subtract, Multiply, Divide |
| **Error handling** | Division-by-zero returns a typed result, never throws |
| **UI** | Interactive console (equivalent to the Streamlit web UI) |

## Modern Java Features Used

- **Records** – `CalculationResult.Success` and `CalculationResult.DivisionByZero`
  are immutable value types declared as `record`.
- **Sealed interfaces** – `CalculationResult` is `sealed`, restricting its
  permitted subtypes so every `switch` over it can be exhaustive.
- **Switch expressions** – `Calculator.calculate` uses a `switch` expression
  (not a statement) to dispatch arithmetic; the `ConsoleUI` uses a `switch`
  expression to map numeric shortcuts to `Operation` values.
- **Pattern matching in `switch`** (Java 21 finalised) – `ConsoleUI` uses
  `case CalculationResult.Success s -> ...` to both test the type and bind the
  variable in one step.
- **Text blocks** – multi-line string literals in `Main` (banner, menu) and
  `CalculationResult.Success.details()`.
- **`var`** – local-variable type inference used throughout for conciseness.
- **Formatted strings** – `String::formatted` (instance version of
  `String.format`) used instead of concatenation.

## Project Structure

```
java-calculator/
├── pom.xml
└── src/
    ├── main/java/com/calculator/
    │   ├── Calculator.java          # Pure business logic (stateless)
    │   ├── CalculationResult.java   # Sealed result type (Success | DivisionByZero)
    │   ├── Operation.java           # Enum for the four arithmetic operations
    │   └── Main.java                # Console UI entry point
    └── test/java/com/calculator/
        ├── CalculatorTest.java      # Unit tests for Calculator & related types
        └── ConsoleUITest.java       # Integration tests for the console UI
```

## Prerequisites

| Tool | Minimum version |
|---|---|
| JDK | 21 |
| Maven | 3.9 |

## Build & Run

```bash
# From the java-calculator/ directory

# Compile and run all tests
mvn test

# Package into an executable JAR
mvn package

# Run the interactive calculator
java -jar target/java-calculator.jar
```

## Example Session

```
╔══════════════════════════════╗
║     🧮  Simple Calculator    ║
║   Perform quick arithmetic   ║
╚══════════════════════════════╝
First number  : 12
Second number : 4
Operation:
  1. Add
  2. Subtract
  3. Multiply
  4. Divide
Choose [1-4] or type the name: 4

✅  Result: 12 ÷ 4 = 3
Computation details:
  first_number  : 12.0
  second_number : 4.0
  operation     : Divide
  result        : 3.0

Perform another calculation? [y/N] n

Goodbye! 👋
```

## Running Tests

```bash
mvn test
```

All tests are in `src/test/java/com/calculator/` and cover:

- All four arithmetic operations (parametrized where applicable)
- Division by zero (returns `DivisionByZero` result, no exception)
- String-based operation parsing (case-insensitive)
- Record equality and field accessors
- Console UI end-to-end flows including invalid input retry loops
