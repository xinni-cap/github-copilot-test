# Architecture Diagrams — Simple Calculator (Streamlit)

**Repository**: xinni-cap/github-copilot-test  
**Generated**: 2025-07-14

---

## 1. High-Level Component Architecture

```mermaid
graph TB
    subgraph Client ["Client Layer (Browser)"]
        Browser["🌐 Web Browser\nReact Frontend Bundle\n(HTML, CSS, JS)"]
    end

    subgraph Framework ["Streamlit Framework Layer"]
        Tornado["🌀 Tornado HTTP Server\nPort 8501"]
        WebSocket["⚡ WebSocket Handler\n/_stcore/stream"]
        StaticFiles["📦 Static File Server\n(React bundle, assets)"]
    end

    subgraph App ["Application Layer (app.py)"]
        PageConfig["⚙️ Page Config\nst.set_page_config()"]
        UIShell["🖼️ UI Shell\nst.title(), st.caption()"]
        InputForm["📋 Input Form\nst.form() container"]
        CalcEngine["🧮 Calculation Engine\nArithmetic if/elif/else"]
        ResultDisplay["✅ Result Display\nst.success(), st.expander()"]
        ErrorHandler["❌ Error Handler\nst.error(), st.stop()"]
    end

    subgraph Runtime ["Python Runtime"]
        Python["🐍 Python 3.8+\nInterpreter"]
        StdLib["📚 Standard Library\n(Built-in float arithmetic)"]
    end

    Browser <-->|"WebSocket"| WebSocket
    Browser <-->|"HTTP GET"| StaticFiles
    WebSocket --> Tornado
    Tornado --> PageConfig
    PageConfig --> UIShell
    UIShell --> InputForm
    InputForm -->|"submitted=True"| CalcEngine
    CalcEngine -->|"valid"| ResultDisplay
    CalcEngine -->|"div by zero"| ErrorHandler
    App --> Python
    Python --> StdLib
```

---

## 2. Deployment Architecture (Local Development)

```mermaid
graph TB
    subgraph DevMachine ["Developer Machine (localhost)"]
        subgraph VEnv ["Python Virtual Environment (.venv)"]
            PythonInterp["🐍 Python 3.8+ Interpreter"]
            StreamlitPkg["📦 streamlit >= 1.40.0"]
        end

        subgraph FileSystem ["File System"]
            AppPy["📄 app.py"]
            ReqTxt["📄 requirements.txt"]
            ReadmeMd["📄 README.md"]
        end

        subgraph OSProcess ["OS Process (streamlit run app.py)"]
            TornadoSrv["🌀 Tornado Server\nTCP 0.0.0.0:8501"]
            ScriptRunner["🔄 Script Runner\n(re-executes app.py)"]
            SessionMgr["👥 Session Manager\n(per-browser-tab)"]
        end
    end

    subgraph BrowserTab ["Browser Tab"]
        ReactApp["⚛️ React Application\n(Streamlit Frontend)"]
    end

    AppPy -->|"loaded by"| ScriptRunner
    PythonInterp --> TornadoSrv
    StreamlitPkg --> TornadoSrv
    TornadoSrv --> ScriptRunner
    TornadoSrv --> SessionMgr
    TornadoSrv <-->|"HTTP + WebSocket\nlocalhost:8501"| ReactApp
```

---

## 3. Data Flow Diagram

```mermaid
flowchart TD
    User(["👤 User"])

    subgraph InputPhase ["Input Phase"]
        Num1["num1: float\n(default: 0.0, format: %.6f)"]
        Num2["num2: float\n(default: 0.0, format: %.6f)"]
        Op["operation: str\n(Add|Subtract|Multiply|Divide)"]
    end

    subgraph FormBatch ["st.form Batching Layer"]
        FormSubmit["Form Submit Button\n'Calculate'"]
    end

    subgraph Routing ["Operation Routing"]
        Router{Operation\nRouter}
        Add["+ Addition"]
        Sub["- Subtraction"]
        Mul["× Multiplication"]
        DivGuard{"÷ Division Guard\nnum2 == 0?"}
        Div["÷ Division\n(num2 != 0 only)"]
    end

    subgraph ErrorPath ["Error Path"]
        ErrDisplay["st.error()\n'Division by zero...'"]
        Halt["st.stop()\nExecution Halted"]
    end

    subgraph OutputPhase ["Output Phase"]
        Result["result: float"]
        Symbol["symbol: str (+/-/×/÷)"]
        SuccessBanner["st.success()\n'Result: {n1} {sym} {n2} = {result}'"]
        DetailExpander["st.expander()\nComputation Details Dict"]
    end

    User --> Num1
    User --> Num2
    User --> Op
    Num1 --> FormSubmit
    Num2 --> FormSubmit
    Op --> FormSubmit
    FormSubmit -->|"submitted=True"| Router

    Router -->|"Add"| Add
    Router -->|"Subtract"| Sub
    Router -->|"Multiply"| Mul
    Router -->|"Divide"| DivGuard

    DivGuard -->|"num2==0"| ErrDisplay
    ErrDisplay --> Halt

    DivGuard -->|"num2!=0"| Div
    Add --> Result
    Sub --> Result
    Mul --> Result
    Div --> Result

    Add --> Symbol
    Sub --> Symbol
    Mul --> Symbol
    Div --> Symbol

    Result --> SuccessBanner
    Symbol --> SuccessBanner
    SuccessBanner --> DetailExpander
    DetailExpander --> User
```

---

## 4. Layer Architecture

```mermaid
graph TB
    subgraph L1 ["Presentation Layer (Streamlit Widgets)"]
        W1["st.set_page_config"]
        W2["st.title + st.caption"]
        W3["st.form + st.columns"]
        W4["st.number_input (×2)"]
        W5["st.selectbox"]
        W6["st.form_submit_button"]
        W7["st.success"]
        W8["st.error"]
        W9["st.expander + st.write"]
    end

    subgraph L2 ["Application / Business Logic Layer"]
        BL1["Form submission check\n(if submitted:)"]
        BL2["Operation router\n(if/elif/else)"]
        BL3["Arithmetic operations\n(+, -, *, /)"]
        BL4["Division guard\n(num2 == 0 check)"]
        BL5["Result formatting\n(f-string template)"]
    end

    subgraph L3 ["Runtime / Infrastructure Layer"]
        R1["Python 3.8+ Interpreter"]
        R2["Streamlit >= 1.40.0"]
        R3["Tornado WebSocket Server"]
        R4["React Frontend Bundle"]
    end

    subgraph L4 ["Persistence Layer"]
        P1["❌ NOT PRESENT\n(Stateless application)"]
    end

    L1 <-->|"Widget state → Script vars"| L2
    L2 -->|"Depends on"| L3
    L3 -.->|"No persistence"| L4

    style L4 fill:#ffcccc,stroke:#ff6666,color:#333
    style P1 fill:#ffcccc,stroke:#ff6666,color:#333
```

---

## Key Architectural Insights

### 🔄 Streamlit Re-run Model

The entire `app.py` re-executes **top-to-bottom on every interaction**. The `st.form` widget deliberately **batches** all three inputs so the re-run only triggers on "Calculate" — not on every keystroke.

### 🛡️ Guard Clause Pattern

```python
if num2 == 0:
    st.error("Division by zero is not allowed.")
    st.stop()   # ← halts the re-run; nothing below executes
result = num1 / num2  # Only reached if num2 != 0
```

### 🌐 Transport Layer (Managed by Streamlit)

```
Browser ←── HTTP GET ──────────→ Tornado :8501  [initial page shell]
Browser ←── WebSocket ─────────→ Tornado :8501  [/_stcore/stream]
Browser ←── HTTP GET (static) →  Tornado :8501  [React bundle, CSS, JS]
```

All network code is **fully managed by Streamlit** — the developer writes zero network code.

### ☁️ Cloud-Readiness Checklist

| Feature | Status |
|---|---|
| Stateless (no shared state between sessions) | ✅ Ready |
| Single port (8501) for load balancer | ✅ Ready |
| Built-in health endpoint (`/_stcore/health`) | ✅ Ready |
| Single-file (easy to containerize) | ✅ Ready |
| HTTPS/TLS termination | ❌ Needs reverse proxy |
| Authentication | ❌ Needs implementation |
| Calculation history | ❌ Needs `st.session_state` |
| Dockerfile | ❌ Missing |
