# Calculator App — .NET 8 Blazor

A modern calculator web application built with **ASP.NET Core Blazor** (.NET 8), modernized from a Python Streamlit prototype.

## Features

- ➕ Add, ➖ Subtract, ✖️ Multiply, ➗ Divide
- Instant server-side calculation with Blazor Interactive Server rendering
- Division-by-zero error handling with user-friendly alert
- Collapsible computation-details panel
- Fully accessible (ARIA labels, keyboard navigation)
- Health-check endpoint at `/healthz`
- Clean, responsive UI (Bootstrap 5)

## Project Structure

```
CalculatorApp/                   # ASP.NET Core Blazor Web App (net8.0)
│  Program.cs                    # Application entry point & DI setup
│  CalculatorApp.csproj
├── Components/
│   ├── Pages/
│   │   └── Home.razor           # Calculator page (Interactive Server)
│   └── Layout/
│       └── MainLayout.razor     # Minimal layout wrapper
├── Services/
│   ├── CalculatorService.cs     # Business logic + domain models
│   └── OperationExtensions.cs  # Enum display helpers
└── wwwroot/
    └── app.css                  # Custom styles

CalculatorApp.Tests/             # xUnit test project (net8.0)
├── CalculatorServiceTests.cs    # Comprehensive arithmetic tests
└── OperationExtensionsTests.cs  # Display-string tests
```

## Prerequisites

- [.NET 8 SDK](https://dotnet.microsoft.com/download/dotnet/8.0) or later

## Run

```bash
cd CalculatorApp
dotnet run
```

Then open <http://localhost:5000> (or the URL shown in the terminal).

## Test

```bash
dotnet test
```

All tests should pass with no warnings.

## Build

```bash
dotnet build --configuration Release
```

---

*Migrated from Python Streamlit — see `app.py` / `requirements.txt` for the original implementation.*