# Calculator App

This repository contains two implementations of a simple calculator that supports **Add, Subtract, Multiply,** and **Divide** operations on two numbers.

| Implementation | Stack | Entry point |
|---|---|---|
| Python Streamlit UI | Python 3 + Streamlit | `app.py` |
| .NET 8 REST API | ASP.NET Core Minimal APIs | `CalculatorApi/` |

---

## .NET 8 Calculator API

A modern ASP.NET Core Web API built with:

- **.NET 8 LTS** – long-term support release
- **Minimal APIs** – lean, low-ceremony HTTP endpoint style
- **Record types** – immutable request/response models
- **Nullable reference types** – enabled project-wide (`<Nullable>enable</Nullable>`)
- **`System.Text.Json`** – built-in JSON with enum-as-string serialisation
- **Swagger / OpenAPI** – auto-generated interactive docs (available in Development)
- **xUnit + FluentAssertions** – unit & integration tests (28 tests)

### Project structure

```
CalculatorApp.slnx
├── CalculatorApi/
│   ├── Models/
│   │   ├── CalculationRequest.cs   # record – input payload
│   │   ├── CalculationResponse.cs  # record – result payload
│   │   └── OperationType.cs        # enum  – Add / Subtract / Multiply / Divide
│   ├── Services/
│   │   ├── ICalculatorService.cs   # interface
│   │   └── CalculatorService.cs    # implementation (switch expression)
│   └── Program.cs                  # Minimal API wiring & endpoints
└── CalculatorApi.Tests/
    ├── CalculatorServiceTests.cs   # unit tests (17 tests)
    └── CalculatorEndpointTests.cs  # integration tests via WebApplicationFactory (11 tests)
```

### Prerequisites

- [.NET 8 SDK](https://dotnet.microsoft.com/download/dotnet/8.0) (or later)

Verify your installation:

```bash
dotnet --version   # should be 8.x.x or higher
```

### Build

```bash
dotnet build CalculatorApp.slnx -c Release
```

### Run

```bash
dotnet run --project CalculatorApi -c Release
```

The API starts on `http://localhost:5000` (and `https://localhost:5001`).  
Open `http://localhost:5000/swagger` in a browser for the interactive Swagger UI.

### API endpoints

#### `POST /api/calculate`

Perform any of the four operations in a single endpoint.

```bash
curl -X POST http://localhost:5000/api/calculate \
     -H "Content-Type: application/json" \
     -d '{"number1": 10, "number2": 2, "operation": "Divide"}'
```

Response:

```json
{
  "number1": 10,
  "number2": 2,
  "operation": "Divide",
  "symbol": "÷",
  "result": 5,
  "expression": "10 ÷ 2 = 5"
}
```

#### Convenience GET endpoints

| Method | URL | Example |
|---|---|---|
| GET | `/api/calculate/add?num1=X&num2=Y` | `/api/calculate/add?num1=3&num2=4` → `7` |
| GET | `/api/calculate/subtract?num1=X&num2=Y` | `/api/calculate/subtract?num1=10&num2=3` → `7` |
| GET | `/api/calculate/multiply?num1=X&num2=Y` | `/api/calculate/multiply?num1=6&num2=7` → `42` |
| GET | `/api/calculate/divide?num1=X&num2=Y` | `/api/calculate/divide?num1=15&num2=3` → `5` |

Division by zero returns `400 Bad Request`:

```bash
curl http://localhost:5000/api/calculate/divide?num1=5\&num2=0
# {"error":"Division by zero is not allowed."}
```

### Test

```bash
dotnet test CalculatorApp.slnx
```

Expected output:

```
Total tests: 28
     Passed: 28
```

---

## Python Streamlit Calculator

### Setup

1. Create and activate a virtual environment (optional but recommended):
   ```bash
   python3 -m venv .venv
   source .venv/bin/activate
   ```
2. Install dependencies:
   ```bash
   pip install -r requirements.txt
   ```

### Run

```bash
streamlit run app.py
```

Then open the local URL shown in the terminal (usually `http://localhost:8501`).
