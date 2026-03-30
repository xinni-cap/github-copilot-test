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

# .NET 8 Calculator App

A modern rewrite of the calculator using **ASP.NET Core 8 Minimal APIs** and a clean HTML/JS frontend.  
The app lives in the [`calculator-dotnet/`](./calculator-dotnet/) subdirectory.

## Features

- ➕ Add, ➖ Subtract, ✖️ Multiply, ➗ Divide two numbers
- Modern C# 12 / .NET 8 features: file-scoped namespaces, record types, nullable reference types, pattern matching, minimal APIs
- OpenAPI/Swagger documentation at `/swagger` (development mode)
- Static HTML/CSS/JS frontend served from `wwwroot/`
- Full xUnit test suite (unit tests + integration tests via `WebApplicationFactory`)

## Prerequisites

- [.NET 8 SDK](https://dotnet.microsoft.com/download/dotnet/8.0) or later

## Run

```bash
cd calculator-dotnet
dotnet run
```

The app starts on `http://localhost:5000` (or the port shown in the terminal).  
Open that URL in your browser to use the calculator UI.

> In development mode, the Swagger UI is available at `http://localhost:5000/swagger`.

## API

`POST /api/calculate`

**Request body:**

```json
{
  "firstNumber":  10.5,
  "secondNumber": 3.0,
  "operation":    "Divide"
}
```

`operation` is one of: `Add`, `Subtract`, `Multiply`, `Divide`.

**Success response (200):**

```json
{
  "firstNumber":  10.5,
  "secondNumber": 3.0,
  "operation":    "Divide",
  "symbol":       "÷",
  "result":       3.5
}
```

**Error response (400) — division by zero:**

```json
{ "error": "Division by zero is not allowed." }
```

## Tests

```bash
cd CalculatorApp.Tests
dotnet test
```

21 tests covering the `CalculatorService` (unit) and the `/api/calculate` endpoint (integration).

## Project structure

```
calculator-dotnet/
├── CalculatorApp.csproj       # SDK-style project — net8.0, Nullable, TreatWarningsAsErrors
├── Program.cs                 # Minimal API entry point (top-level statements)
├── Models/
│   ├── Operation.cs           # enum with [JsonConverter] attribute
│   ├── CalculationRequest.cs  # positional record
│   └── CalculationResult.cs   # positional record
├── Services/
│   ├── ICalculatorService.cs  # interface
│   └── CalculatorService.cs   # implementation using switch expression
└── wwwroot/
    └── index.html             # vanilla HTML/CSS/JS frontend

CalculatorApp.Tests/
├── CalculatorApp.Tests.csproj
├── CalculatorServiceTests.cs  # xUnit unit tests (Theory + Fact)
└── CalculateEndpointTests.cs  # WebApplicationFactory integration tests
```
