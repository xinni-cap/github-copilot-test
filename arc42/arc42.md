# arc42 Architecture Documentation — Streamlit Calculator App

> **Template version:** arc42 Version 8  
> **Status:** Initial draft

---

## Table of Contents

1. [Introduction and Goals](#1-introduction-and-goals)
2. [Constraints](#2-constraints)
3. [Context and Scope](#3-context-and-scope)
4. [Solution Strategy](#4-solution-strategy)
5. [Building Block View](#5-building-block-view)
6. [Runtime View](#6-runtime-view)
7. [Deployment View](#7-deployment-view)
8. [Cross-cutting Concepts](#8-cross-cutting-concepts)
9. [Architecture Decisions](#9-architecture-decisions)
10. [Quality Requirements](#10-quality-requirements)
11. [Risks and Technical Debt](#11-risks-and-technical-debt)
12. [Glossary](#12-glossary)

---

## 1. Introduction and Goals

### 1.1 Requirements Overview

The Streamlit Calculator App is a lightweight, browser-based arithmetic calculator that allows users to perform basic mathematical operations — addition, subtraction, multiplication, and division — through a simple web interface.

**Key functional requirements:**

| ID  | Requirement |
|-----|-------------|
| F-1 | Users can enter two numeric operands. |
| F-2 | Users can select an arithmetic operation (Add, Subtract, Multiply, Divide). |
| F-3 | The app displays the computed result after form submission. |
| F-4 | The app handles division-by-zero gracefully with a user-friendly error message. |
| F-5 | Computation details (operands, operation, result) are available in an expandable panel. |

### 1.2 Quality Goals

| Priority | Quality Goal | Motivation |
|----------|-------------|------------|
| 1 | **Correctness** | Arithmetic results must be accurate for all supported operations and edge cases. |
| 2 | **Usability** | The UI must be self-explanatory with minimal instructions needed. |
| 3 | **Maintainability** | Calculator logic is decoupled from the UI so it can be tested and extended independently. |
| 4 | **Reliability** | The app must handle invalid inputs (e.g., division by zero) without crashing. |

### 1.3 Stakeholders

| Role | Expectation |
|------|-------------|
| End User | Performs quick arithmetic calculations via a browser. |
| Developer | Extends or maintains calculator logic and the Streamlit UI. |
| QA / Tester | Validates correctness of calculator logic through automated tests. |

---

## 2. Constraints

### 2.1 Technical Constraints

| Constraint | Rationale |
|------------|-----------|
| Python 3.x | The entire application is written in Python. |
| Streamlit framework | The UI is implemented exclusively with Streamlit. |
| Browser-based access only | There is no desktop GUI or REST API; all interaction is through a web browser. |

### 2.2 Organisational Constraints

| Constraint | Rationale |
|------------|-----------|
| Open-source tooling | All dependencies (Streamlit, pytest) are freely available open-source packages. |
| Single-developer scope | The project is a demonstrator / learning application, not a production-grade service. |

---

## 3. Context and Scope

### 3.1 Business Context

```
┌──────────────────────────────────────────────────────┐
│                    User (Browser)                    │
│                                                      │
│  Enters two numbers and selects an operation,        │
│  receives the computed result.                       │
└───────────────────┬──────────────────────────────────┘
                    │  HTTP (localhost / hosted URL)
                    ▼
┌──────────────────────────────────────────────────────┐
│           Streamlit Calculator App                   │
│                                                      │
│  Presents the calculator form and displays results.  │
└──────────────────────────────────────────────────────┘
```

**External interfaces:**

| Interface | Description |
|-----------|-------------|
| Browser (HTTP) | The only external interface. Streamlit serves the app as a web page. |

### 3.2 Technical Context

```
┌───────────────────────────────────────────────────────────┐
│                  Streamlit Process (Python)                │
│                                                           │
│   ┌─────────────┐        ┌───────────────────────────┐   │
│   │   app.py    │──────▶│      calculator.py         │   │
│   │ (UI layer)  │        │   (business logic layer)  │   │
│   └─────────────┘        └───────────────────────────┘   │
└───────────────────────────────────────────────────────────┘
```

| Component | Technology | Role |
|-----------|-----------|------|
| `app.py` | Python + Streamlit | Renders the web UI, captures user input, displays results. |
| `calculator.py` | Pure Python | Implements arithmetic logic, raises typed exceptions. |
| `tests/` | pytest | Automated unit-test suite for `calculator.py`. |

---

## 4. Solution Strategy

| Decision | Rationale |
|----------|-----------|
| **Separation of concerns** | Calculator logic (`calculator.py`) is kept completely independent of the UI (`app.py`) so it can be unit-tested without a browser or Streamlit runtime. |
| **Streamlit for UI** | Streamlit removes the need for HTML/CSS/JavaScript and lets developers build interactive data apps in pure Python. |
| **Typed exceptions** | `DivisionByZeroError` extends `ValueError` to give callers fine-grained control while remaining compatible with Python's built-in exception hierarchy. |
| **Form-based interaction** | Using `st.form` batches all user inputs into a single submit action, preventing re-runs on every widget change. |

---

## 5. Building Block View

### Level 1 — Overall System

```
┌──────────────────────────────────────────────┐
│             Streamlit Calculator App          │
│                                              │
│  ┌──────────────┐    ┌────────────────────┐  │
│  │   app.py     │───▶│   calculator.py    │  │
│  │  (UI layer)  │    │  (logic layer)     │  │
│  └──────────────┘    └────────────────────┘  │
│                                              │
│  ┌──────────────────────────────────────┐    │
│  │   tests/test_calculator.py (pytest)  │    │
│  └──────────────────────────────────────┘    │
└──────────────────────────────────────────────┘
```

### Level 2 — `calculator.py` internals

| Element | Type | Responsibility |
|---------|------|----------------|
| `calculate(num1, num2, operation)` | Function | Dispatches to the correct arithmetic operation and returns the result. |
| `DivisionByZeroError` | Exception class | Signals a division-by-zero attempt in a type-safe way. |
| `OPERATION_SYMBOLS` | Dict constant | Maps operation names to their display symbols (`+`, `-`, `×`, `÷`). |

### Level 2 — `app.py` internals

| Element | Responsibility |
|---------|----------------|
| `st.form("calculator_form")` | Groups all inputs and the submit button so Streamlit only re-runs on submission. |
| Number inputs (`num1`, `num2`) | Capture the two operands with six-decimal precision. |
| `st.selectbox` (operation) | Lets the user pick an arithmetic operation. |
| Result / error display | Shows a success message with the result or an error message on `DivisionByZeroError`. |
| Computation details expander | Provides an expandable view of all inputs and the result for transparency. |

---

## 6. Runtime View

### Scenario 1 — Successful Calculation

```
User                  app.py                 calculator.py
 │                       │                         │
 │  Fill form & Submit   │                         │
 │──────────────────────▶│                         │
 │                       │  calculate(n1, n2, op)  │
 │                       │────────────────────────▶│
 │                       │        result           │
 │                       │◀────────────────────────│
 │   Display result      │                         │
 │◀──────────────────────│                         │
```

### Scenario 2 — Division by Zero

```
User                  app.py                 calculator.py
 │                       │                         │
 │  Divide by 0, Submit  │                         │
 │──────────────────────▶│                         │
 │                       │  calculate(n, 0, "Div") │
 │                       │────────────────────────▶│
 │                       │  DivisionByZeroError    │
 │                       │◀────────────────────────│
 │   Display error msg   │                         │
 │◀──────────────────────│                         │
```

---

## 7. Deployment View

### Local Development

```
Developer Machine
└── Python virtual environment
    ├── streamlit (installed via pip)
    └── pytest   (installed via pip)

$ streamlit run app.py
  → opens http://localhost:8501 in the browser
```

### Infrastructure Requirements

| Requirement | Value |
|-------------|-------|
| Runtime | Python 3.9+ |
| Port | 8501 (Streamlit default) |
| External services | None |
| Persistence | None (stateless, no database) |

The application is stateless and self-contained. It can be deployed on any platform that supports running a Python process and exposing a TCP port (e.g., a cloud VM, a container, or a managed PaaS like Streamlit Community Cloud).

---

## 8. Cross-cutting Concepts

### 8.1 Error Handling

- `DivisionByZeroError` is raised by `calculator.py` and caught in `app.py`, which displays a user-friendly error via `st.error()` and halts further rendering with `st.stop()`.
- Unsupported operations raise Python's built-in `ValueError`.

### 8.2 Testing Strategy

- All arithmetic logic is covered by pytest unit tests in `tests/test_calculator.py`.
- Tests are grouped by operation class (`TestAdd`, `TestSubtract`, `TestMultiply`, `TestDivide`, `TestUnsupportedOperation`).
- The UI layer (`app.py`) is currently not covered by automated tests because it requires a running Streamlit server.

### 8.3 Coding Conventions

- Python type hints are used throughout `calculator.py`.
- Docstrings follow NumPy / plain-English style with Parameters and Raises sections.
- The UI layer imports only the public API of `calculator.py` (`calculate`, `DivisionByZeroError`, `OPERATION_SYMBOLS`).

---

## 9. Architecture Decisions

### ADR-001 — Separate UI and Business Logic

**Date:** Initial design  
**Status:** Accepted

**Context:** A calculator app could be written as a single script mixing UI and logic.

**Decision:** Split the application into `app.py` (UI) and `calculator.py` (logic).

**Consequences:**
- ✅ Business logic can be unit-tested without Streamlit.
- ✅ The UI can be replaced or extended without touching the core logic.
- ❌ Slightly more files to manage in a small project.

---

### ADR-002 — Use `st.form` for Input Batching

**Date:** Initial design  
**Status:** Accepted

**Context:** Streamlit re-runs the entire script on every widget interaction by default, which causes premature calculations while the user is still typing.

**Decision:** Wrap all inputs in an `st.form` so calculation only happens on explicit form submission.

**Consequences:**
- ✅ Prevents flickering / premature results while the user is still filling in numbers.
- ✅ Mirrors the mental model of a physical calculator (press "=" to compute).

---

### ADR-003 — Custom Exception for Division by Zero

**Date:** Initial design  
**Status:** Accepted

**Context:** Division by zero could be signalled by returning `None`, raising `ZeroDivisionError`, or a custom exception.

**Decision:** Introduce `DivisionByZeroError(ValueError)` to give callers a specific, catchable exception type.

**Consequences:**
- ✅ Callers can handle the domain error distinctly from other `ValueError`s.
- ✅ The exception hierarchy remains compatible with standard Python practice.

---

## 10. Quality Requirements

### 10.1 Quality Tree

```
Streamlit Calculator App
├── Correctness
│   ├── All four operations produce mathematically correct results
│   └── Division by zero is detected and reported without a crash
├── Usability
│   ├── Interface requires no instructions to operate
│   └── Error messages are human-readable
├── Maintainability
│   ├── Business logic is decoupled from the UI
│   └── Code is covered by automated unit tests
└── Reliability
    └── Application does not crash on bad user input
```

### 10.2 Quality Scenarios

| ID  | Quality Attribute | Scenario | Measure |
|-----|-------------------|----------|---------|
| Q-1 | Correctness | User divides 10 by 2 — result must be exactly 5.0. | Verified by unit test. |
| Q-2 | Reliability | User divides any number by 0 — error message shown, no Python traceback exposed. | `st.error()` displayed; `st.stop()` prevents further rendering. |
| Q-3 | Maintainability | Developer adds a new operation — only `calculator.py` and `OPERATION_SYMBOLS` need to change; tests can be added in `tests/`. | Architecture enforces this via module boundary. |

---

## 11. Risks and Technical Debt

| ID  | Risk / Debt | Impact | Mitigation |
|-----|-------------|--------|------------|
| R-1 | **No UI tests** — `app.py` has no automated test coverage. | Medium — UI regressions may go undetected. | Add Streamlit AppTest or Playwright-based tests in future. |
| R-2 | **Floating-point precision** — Python's `float` arithmetic can produce small rounding errors. | Low — typical for interactive calculators; no financial calculations expected. | Document limitation; use `decimal.Decimal` if high precision is required. |
| R-3 | **No input validation beyond type** — Streamlit's `st.number_input` prevents non-numeric input, but very large numbers could cause overflow. | Low — edge case for a demonstrator app. | Add range validation if the app is extended for production use. |
| R-4 | **Single-page, single-session design** — The app has no persistent state, user history, or multi-session support. | Low — acceptable for current scope. | Revisit if calculation history or user sessions are needed. |

---

## 12. Glossary

| Term | Definition |
|------|-----------|
| **arc42** | A lean, pragmatic template for software architecture documentation. |
| **Streamlit** | An open-source Python framework for building interactive web applications for data science and machine learning. |
| **pytest** | A Python testing framework used for writing and running unit tests. |
| **DivisionByZeroError** | A custom exception class in this application that signals a division-by-zero attempt. |
| **OPERATION_SYMBOLS** | A Python dictionary that maps operation names (`"Add"`, `"Subtract"`, etc.) to their display symbols (`+`, `-`, etc.). |
| **Form submission** | The act of clicking the "Calculate" button inside an `st.form`, which triggers Streamlit to re-run the script and compute the result. |
| **ADR** | Architecture Decision Record — a short document capturing an important architectural decision, its context, and its consequences. |
