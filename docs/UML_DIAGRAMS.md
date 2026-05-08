# UML Diagrams — Simple Calculator (Streamlit)

**Repository**: xinni-cap/github-copilot-test  
**Generated**: 2025-07-14

---

## 1. Class Diagram

```mermaid
classDiagram
    direction TB

    class StreamlitApp {
        <<entrypoint>>
        +page_title: String = "Calculator"
        +page_icon: String = "🧮"
        +layout: String = "centered"
        +run() void
    }

    class CalculatorForm {
        <<UI Component>>
        +form_key: String = "calculator_form"
        +submitted: Boolean
        +render() void
        +submit() Boolean
    }

    class NumberInput {
        <<UI Component>>
        +label: String
        +value: Float = 0.0
        +format: String = "%.6f"
        +getValue() Float
    }

    class OperationSelector {
        <<UI Component>>
        +options: List~String~
        +index: Integer = 0
        +selected: String
        +getOperation() String
    }

    class CalculationEngine {
        <<Service>>
        +num1: Float
        +num2: Float
        +operation: String
        +result: Float
        +calculate() Float
        +add(a: Float, b: Float) Float
        +subtract(a: Float, b: Float) Float
        +multiply(a: Float, b: Float) Float
        +divide(a: Float, b: Float) Float
    }

    class DivisionGuard {
        <<Validator>>
        +divisor: Float
        +validate(divisor: Float) Boolean
        +raiseError() void
    }

    class ResultDisplay {
        <<UI Component>>
        +result: Float
        +symbol: String
        +showSuccess(message: String) void
        +showError(message: String) void
        +stop() void
    }

    class Operation {
        <<enumeration>>
        ADD
        SUBTRACT
        MULTIPLY
        DIVIDE
    }

    StreamlitApp "1" *-- "1" CalculatorForm : contains
    CalculatorForm "1" *-- "2" NumberInput : has
    CalculatorForm "1" *-- "1" OperationSelector : has
    CalculatorForm ..> CalculationEngine : triggers
    CalculationEngine "1" --> "1" Operation : uses
    CalculationEngine ..> DivisionGuard : delegates to
    CalculationEngine ..> ResultDisplay : populates
```

---

## 2. Sequence Diagram

```mermaid
sequenceDiagram
    actor User
    participant StreamlitApp
    participant Form as CalculatorForm
    participant Engine as CalculationEngine
    participant Guard as DivisionGuard
    participant UI as ResultDisplay

    User->>StreamlitApp: Navigate to app URL
    StreamlitApp->>Form: render()
    Form-->>User: Display num1, num2 inputs + Calculate button

    User->>Form: Enter num1, num2, select operation
    User->>Form: Click "Calculate"
    Form->>StreamlitApp: submitted = True

    alt operation == "Add"
        StreamlitApp->>Engine: add(num1, num2)
        Engine-->>StreamlitApp: result = num1 + num2
    else operation == "Subtract"
        StreamlitApp->>Engine: subtract(num1, num2)
        Engine-->>StreamlitApp: result = num1 - num2
    else operation == "Multiply"
        StreamlitApp->>Engine: multiply(num1, num2)
        Engine-->>StreamlitApp: result = num1 * num2
    else operation == "Divide"
        StreamlitApp->>Guard: validate(num2)
        alt num2 == 0
            Guard-->>UI: raiseError()
            UI-->>User: st.error + st.stop()
        else num2 != 0
            StreamlitApp->>Engine: divide(num1, num2)
            Engine-->>StreamlitApp: result = num1 / num2
        end
    end

    StreamlitApp->>UI: showSuccess(result)
    UI-->>User: Green success banner
    User->>UI: Expand "Computation details"
    UI-->>User: Detail dict {first_number, second_number, operation, result}
```

---

## 3. Use Case Diagram

```mermaid
graph LR
    User(["👤 User"])

    subgraph boundary ["🧮 Streamlit Calculator System"]
        direction TB
        UC0["⚙️ Open Calculator App"]
        UC1["➕ Perform Addition"]
        UC2["➖ Perform Subtraction"]
        UC3["✖️ Perform Multiplication"]
        UC4["➗ Perform Division"]
        UC5["📋 View Calculation Result"]
        UC6["🔍 View Computation Details"]
        UC7["⚠️ Handle Division by Zero Error"]
        UC_INPUT["📝 Enter Two Numbers"]
        UC_SELECT["🔽 Select Operation"]
        UC_SUBMIT["🖱️ Submit Form"]
    end

    User --> UC0
    User --> UC_INPUT
    User --> UC_SELECT
    User --> UC_SUBMIT
    User --> UC6
    UC_SUBMIT -.->|"«include»"| UC_INPUT
    UC_SUBMIT -.->|"«include»"| UC_SELECT
    UC_SUBMIT -->|triggers| UC1
    UC_SUBMIT -->|triggers| UC2
    UC_SUBMIT -->|triggers| UC3
    UC_SUBMIT -->|triggers| UC4
    UC1 -.->|"«include»"| UC5
    UC2 -.->|"«include»"| UC5
    UC3 -.->|"«include»"| UC5
    UC4 -.->|"«include»"| UC5
    UC4 -.->|"«extend»"| UC7
    UC5 -.->|"«extend»"| UC6
```

---

## 4. State Diagram

```mermaid
stateDiagram-v2
    [*] --> Initializing : App loads
    Initializing --> AwaitingInput : Page config set

    state AwaitingInput {
        [*] --> EmptyForm
        EmptyForm --> InputReady : User enters values
        InputReady --> ReadyToSubmit : Operation selected
    }

    AwaitingInput --> FormSubmitted : User clicks "Calculate"

    state FormSubmitted {
        [*] --> EvaluatingOperation
        EvaluatingOperation --> AdditionPath : op == Add
        EvaluatingOperation --> SubtractionPath : op == Subtract
        EvaluatingOperation --> MultiplicationPath : op == Multiply
        EvaluatingOperation --> DivisionPath : op == Divide
    }

    state DivisionPath {
        [*] --> ValidatingDivisor
        ValidatingDivisor --> DivisionReady : num2 != 0
        ValidatingDivisor --> DivisionError : num2 == 0
    }

    FormSubmitted --> Calculating : Valid operation
    DivisionError --> ErrorState : st.error() called
    Calculating --> DisplayingResult : Result computed
    ErrorState --> AwaitingInput : User corrects input
    DisplayingResult --> AwaitingInput : User modifies inputs
```

---

## 5. Activity Diagram

```mermaid
flowchart TD
    Start([App Start]) --> Config["Set Page Config"]
    Config --> RenderTitle["Render Title and Caption"]
    RenderTitle --> RenderForm["Render Calculator Form"]
    RenderForm --> WaitSubmit{Form Submitted?}
    WaitSubmit -- No --> WaitSubmit
    WaitSubmit -- Yes --> CheckOp{Which Operation?}

    CheckOp -- Add --> DoAdd["result = num1 + num2, symbol = +"]
    CheckOp -- Subtract --> DoSub["result = num1 - num2, symbol = -"]
    CheckOp -- Multiply --> DoMul["result = num1 x num2, symbol = x"]
    CheckOp -- Divide --> CheckZero{num2 == 0?}

    CheckZero -- Yes --> ShowError["st.error: Division by zero not allowed"]
    ShowError --> StopExec["st.stop() - Execution Halted"]
    StopExec --> End2([Halted])

    CheckZero -- No --> DoDiv["result = num1 / num2, symbol = div"]

    DoAdd --> ShowSuccess["st.success: Result display"]
    DoSub --> ShowSuccess
    DoMul --> ShowSuccess
    DoDiv --> ShowSuccess

    ShowSuccess --> RenderExpander["Render Computation Details Expander"]
    RenderExpander --> NewCalc{User Modifies Inputs?}
    NewCalc -- Yes --> WaitSubmit
    NewCalc -- No --> End([Session Ends])
```
