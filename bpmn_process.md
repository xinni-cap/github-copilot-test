# BPMN 2.0 Process Model — Streamlit Calculator Application

> **Source**: `app.py` · **Framework**: Streamlit ≥ 1.40.0 · **Language**: Python 3  
> **Process Model Standard**: BPMN 2.0 (visualised as Mermaid flowcharts)  
> **Generated from**: `app.py` source code, `business_rules_extractor_analysis.json`, `ast_analysis.json`

---

## Table of Contents

1. [Process Narrative](#1-process-narrative)
2. [Process Elements Catalog](#2-process-elements-catalog)
3. [BPMN Diagrams](#3-bpmn-diagrams)
   - 3.1 [Full Process with Swimlanes](#31-full-process-with-swimlanes)
   - 3.2 [Computation Engine — Decision Detail](#32-computation-engine--decision-detail)
   - 3.3 [Error Handling Sub-Process](#33-error-handling-sub-process)
   - 3.4 [Operation Routing Detail](#34-operation-routing-detail)
4. [Process Metrics](#4-process-metrics)
5. [Lane / Participant Breakdown](#5-lane--participant-breakdown)
6. [Business Rule — Process Mapping](#6-business-rule--process-mapping)
7. [Data Objects and Field Catalog](#7-data-objects-and-field-catalog)

---

## 1. Process Narrative

### 1.1 Overview

The **Simple Calculator** is a single-page, browser-based Streamlit web application that performs the four fundamental arithmetic operations — Addition, Subtraction, Multiplication, and Division — on two floating-point numbers provided by the user.

The process follows Streamlit's **reactive top-to-bottom execution model**: the entire Python script re-runs on every user interaction. A `st.form` context batches all input widgets so that no calculation is triggered while the user is still editing values. Computation is deferred and only begins when the user explicitly clicks the **"Calculate"** button, causing Streamlit to re-run the script with `submitted = True`.

### 1.2 Process Boundaries

| Boundary | Element | Description |
|---|---|---|
| **Start** | User opens the calculator URL in a browser | Streamlit serves the application page |
| **Happy End** | Formatted result equation displayed on screen | Calculation completes without error |
| **Error End** | Division-by-zero error banner shown, execution halted | `st.stop()` raises a `StopException` |

### 1.3 Participants / Pools

| Pool | Role |
|---|---|
| **User** | The human actor who provides inputs and reads outputs |
| **Streamlit System** | The framework and application code that renders the UI, validates inputs, computes results, and displays outputs |

### 1.4 Narrative Walk-Through

**Step 1 — Application Initialisation (System)**  
When the user navigates to the calculator URL, Streamlit executes `app.py` from top to bottom with `submitted = False`. The system configures the page metadata (`st.set_page_config`), renders the title and caption, and draws the input form containing: two floating-point number inputs (defaulting to `0.0`, formatted to six decimal places per **BR-002** and **BR-006**), an operation selector defaulting to "Add" (**BR-003**, **BR-004**), and a "Calculate" submit button. The computation block is skipped entirely because `submitted` is `False` — the `if submitted:` guard at line 95 is not entered.

**Step 2 — User Input Collection (User)**  
The user fills in the form fields at their own pace. Each individual keystroke or dropdown change does **not** trigger a recalculation because all widgets are wrapped in a `st.form` context manager (**BR-004** / **BR-007**). Input changes are batched within the browser until the form is submitted.

**Step 3 — Form Submission (User → System boundary)**  
The user clicks "Calculate". Streamlit detects the submit event and re-runs the script with `submitted = True`, passing the current form values as `num1`, `num2`, and `operation`.

**Step 4 — Submission Gate (System)**  
The `if submitted:` guard at line 95 of `app.py` acts as the first exclusive gateway. Because `submitted = True`, execution enters the computation block.

**Step 5 — Input Validation: Division-by-Zero Guard (System)**  
Inside the computation block, if the user selected "Divide", the system performs a pre-computation guard check (**BR-001**): it tests whether `num2 == 0`. If the divisor is zero, the system displays a red error banner and immediately calls `st.stop()`, which raises a Streamlit `StopException` and halts further script execution. The `result` variable is never assigned. No success banner or expander is rendered. The process terminates at the **Error End Event**.

**Step 6 — Operation Routing (System)**  
If the input is valid, an exclusive four-way gateway routes execution to the matching arithmetic branch:

- **Add**: `result = num1 + num2`, `symbol = "+"`
- **Subtract**: `result = num1 - num2`, `symbol = "-"`
- **Multiply**: `result = num1 * num2`, `symbol = "×"`
- **Divide** (only reached when `num2 ≠ 0`): `result = num1 / num2`, `symbol = "÷"`

**Step 7 — Result Display (System)**  
After the arithmetic branch completes, the system renders a green success banner (`st.success`) containing the formatted equation: `"Result: {num1} {symbol} {num2} = {result}"` (**BR-005** / **BR-006**).

**Step 8 — Optional Details Panel (User → System)**  
A collapsible `st.expander` labelled "Computation details" is rendered but collapsed by default (**BR-007**). If the user clicks to expand it, the system displays the raw computation dictionary: `{first_number, second_number, operation, result}`. This is an optional extension activity.

**Step 9 — Process End**  
The process reaches the **Successful Calculation End Event**. The user may repeat the process by modifying inputs and clicking "Calculate" again, which triggers a fresh re-run.

---

## 2. Process Elements Catalog

### 2.1 Events

| ID | Element | BPMN Type | Description | Code Reference |
|---|---|---|---|---|
| **EVT-001** | User Opens Calculator App | Start Event (None) | User navigates to the Streamlit URL; script executes for the first time | `streamlit run app.py` |
| **EVT-002** | Calculate Button Clicked | Boundary Event (Form Submit) | User clicks the "Calculate" button; triggers script re-run with `submitted=True` | `st.form_submit_button("Calculate")` |
| **EVT-003** | Successful Calculation End | End Event (None) | Result displayed; process completes successfully | After `st.success()` / `st.expander()` |
| **EVT-004** | Division-by-Zero Error End | End Event (Error) | `st.stop()` called; process terminates in error state | `app.py` line 123 |

### 2.2 Tasks

| ID | Task Name | BPMN Task Type | Lane | Description | Code Reference |
|---|---|---|---|---|---|
| **T-001** | Configure Page | Service Task | System | Set browser tab title, icon, and layout | `st.set_page_config(...)` |
| **T-002** | Render Calculator Form | Service Task | System | Draw title, caption, two number inputs, operation selector, and Calculate button | `st.form("calculator_form")` |
| **T-003** | Enter First Number | User Task | User | User types or adjusts `num1` (float, default `0.0`, 6 d.p.) | `st.number_input("First number", ...)` |
| **T-004** | Enter Second Number | User Task | User | User types or adjusts `num2` (float, default `0.0`, 6 d.p.) | `st.number_input("Second number", ...)` |
| **T-005** | Select Operation | User Task | User | User picks one of: Add, Subtract, Multiply, Divide (default: Add) | `st.selectbox("Operation", ...)` |
| **T-006** | Submit Form | User Task | User | User explicitly clicks "Calculate" to batch-submit all form values | `st.form_submit_button("Calculate")` |
| **T-007** | Display Error Banner | Service Task | System | Render `st.error("Division by zero is not allowed.")` then call `st.stop()` | `app.py` lines 122–123 |
| **T-008** | Compute Addition | Script Task | System | `result = num1 + num2`; `symbol = "+"` | `app.py` lines 97–98 |
| **T-009** | Compute Subtraction | Script Task | System | `result = num1 - num2`; `symbol = "-"` | `app.py` lines 99–100 |
| **T-010** | Compute Multiplication | Script Task | System | `result = num1 * num2`; `symbol = "×"` | `app.py` lines 101–102 |
| **T-011** | Compute Division | Script Task | System | `result = num1 / num2`; `symbol = "÷"` (guard already passed) | `app.py` line 125 |
| **T-012** | Display Formatted Result | Service Task | System | Render `st.success(f"Result: {num1} {symbol} {num2} = {result}")` | `app.py` line 133 |
| **T-013** | View Result Equation | User Task | User | User reads the formatted result in the green success banner | UI interaction |
| **T-014** | Expand Computation Details | User Task | User | User optionally clicks the "Computation details" expander | `st.expander(...)` |
| **T-015** | Render Computation Details | Service Task | System | Display raw dict: `{first_number, second_number, operation, result}` | `app.py` lines 138–144 |

### 2.3 Gateways

| ID | Gateway Name | BPMN Type | Description | Outgoing Flows | Code Reference |
|---|---|---|---|---|---|
| **GW-001** | Form Submission Check | Exclusive (XOR) | Was the form explicitly submitted in this script re-run? | Yes → Validate Input · No → Show Form Only | `if submitted:` (line 95) |
| **GW-002** | Division-by-Zero Check | Exclusive (XOR) | Is the selected operation "Divide" AND is `num2 == 0`? | Yes → Error Path · No → Route Operation | `if num2 == 0:` (line 121) |
| **GW-003** | Operation Router | Exclusive (XOR) | Which arithmetic operation was selected? | Add · Subtract · Multiply · Divide | `if/elif/else` chain (lines 96–125) |
| **GW-004** | Expand Details Check | Exclusive (XOR) | Did the user click to expand the computation details panel? | Yes → Show Details · No → End | `st.expander(...)` interaction |

### 2.4 Sequence Flows

| ID | From | To | Condition |
|---|---|---|---|
| **SF-001** | EVT-001 | T-001 | Always — script starts |
| **SF-002** | T-001 | T-002 | Always |
| **SF-003** | T-002 | T-003 | Always — form rendered |
| **SF-004** | T-003 | T-004 | Always |
| **SF-005** | T-004 | T-005 | Always |
| **SF-006** | T-005 | T-006 | Always |
| **SF-007** | T-006 | GW-001 | Always — form submitted signal sent |
| **SF-008** | GW-001 | T-002 | `submitted == False` (page load / no submit) |
| **SF-009** | GW-001 | GW-002 | `submitted == True` |
| **SF-010** | GW-002 | T-007 | `operation == "Divide" AND num2 == 0` |
| **SF-011** | T-007 | EVT-004 | Always — st.stop() halts |
| **SF-012** | GW-002 | GW-003 | `NOT (operation == "Divide" AND num2 == 0)` |
| **SF-013** | GW-003 | T-008 | `operation == "Add"` |
| **SF-014** | GW-003 | T-009 | `operation == "Subtract"` |
| **SF-015** | GW-003 | T-010 | `operation == "Multiply"` |
| **SF-016** | GW-003 | T-011 | `operation == "Divide"` (guard passed) |
| **SF-017** | T-008 | T-012 | Always |
| **SF-018** | T-009 | T-012 | Always |
| **SF-019** | T-010 | T-012 | Always |
| **SF-020** | T-011 | T-012 | Always |
| **SF-021** | T-012 | T-013 | Always — user reads result |
| **SF-022** | T-013 | GW-004 | Always |
| **SF-023** | GW-004 | T-014 | User clicks expander |
| **SF-024** | T-014 | T-015 | Always |
| **SF-025** | T-015 | EVT-003 | Always |
| **SF-026** | GW-004 | EVT-003 | User does not expand details |

---

## 3. BPMN Diagrams

### 3.1 Full Process with Swimlanes

This diagram presents the complete end-to-end calculator workflow organised into two swimlanes: the **User Lane** (human actor interactions) and the **System Lane** (automated Streamlit processing). All four BPMN element types — events, tasks, gateways, and flows — are represented.

> **Legend**:  
> 🟢 Green nodes = Start / Success End events  
> 🔴 Red nodes = Error End event / Error tasks  
> 🟡 Yellow nodes = Exclusive decision gateways  
> 🔵 Blue nodes = User tasks  
> ⚪ White/beige nodes = System tasks  
> Green compute nodes = Arithmetic script tasks

```mermaid
flowchart TD
    subgraph UserLane["👤  USER LANE"]
        direction TB
        U_START(["🟢 START\nUser Opens Calculator App"])
        U_NUM1["T-003 · Enter First Number\nnum1 · float · default 0.0\nformat: 6 decimal places"]
        U_NUM2["T-004 · Enter Second Number\nnum2 · float · default 0.0\nformat: 6 decimal places"]
        U_OP["T-005 · Select Operation\nAdd · Subtract · Multiply · Divide\ndefault: Add  "]
        U_SUBMIT["T-006 · Click Calculate\nExplicit Form Submission\nBR-004 · BR-007"]
        U_VIEW["T-013 · Read Result Equation\nGreen Success Banner · BR-005"]
        U_DET_GW{"GW-004\nExpand Computation\nDetails?\nBR-007"}
        U_DET["T-014 · Click Expander\nView Computation Details"]
    end

    subgraph SystemLane["⚙️  SYSTEM LANE"]
        direction TB
        S_CONFIG["T-001 · Configure Page\nst.set_page_config\nTitle · Icon · Layout"]
        S_RENDER["T-002 · Render Calculator Form\nst.form context manager\nInputs · Selector · Button"]
        S_GATE{"GW-001\nForm Submitted?\nsubmitted == True\napp.py line 95"}
        S_DIVCHK{"GW-002 · Division by Zero?\noperation == Divide\nAND num2 == 0\napp.py line 121"}
        S_ERROR["T-007 · Display Error Banner\nst.error · Division by zero\nnot allowed · app.py line 122\nst.stop halts execution · line 123"]
        S_ROUTER{"GW-003\nRoute Operation\nExclusive XOR\napp.py lines 96-125"}
        S_ADD["T-008 · Compute Addition\nresult = num1 + num2\nsymbol = +"]
        S_SUB["T-009 · Compute Subtraction\nresult = num1 - num2\nsymbol = -"]
        S_MUL["T-010 · Compute Multiplication\nresult = num1 x num2\nsymbol = x"]
        S_DIV["T-011 · Compute Division\nresult = num1 / num2\nsymbol = div"]
        S_RESULT["T-012 · Display Formatted Result\nst.success green banner\nResult: num1 OP num2 = result\nBR-005 · BR-006"]
        S_EXPANDER["T-015 · Render Computation Details\nst.expander + st.write\nfirst_number · second_number\noperation · result · BR-007"]
        S_ERR_END(["🔴 ERROR END\nEVT-004 · EC-001\nst.stop · StopException\nExecution Halted"])
        S_SUC_END(["🟢 SUCCESS END\nEVT-003\nCalculation Complete"])
    end

    U_START --> S_CONFIG
    S_CONFIG --> S_RENDER
    S_RENDER --> U_NUM1
    U_NUM1 --> U_NUM2
    U_NUM2 --> U_OP
    U_OP --> U_SUBMIT
    U_SUBMIT --> S_GATE
    S_GATE -->|"No — SF-008\nPage Load · submitted=False\nAwaiting Input"| S_RENDER
    S_GATE -->|"Yes — SF-009\nsubmitted=True\nEnter Computation Block"| S_DIVCHK
    S_DIVCHK -->|"YES — SF-010\nBR-001 Violated\nError Path"| S_ERROR
    S_ERROR --> S_ERR_END
    S_DIVCHK -->|"NO — SF-012\nValid Input\nProceed to Routing"| S_ROUTER
    S_ROUTER -->|"Add\nSF-013"| S_ADD
    S_ROUTER -->|"Subtract\nSF-014"| S_SUB
    S_ROUTER -->|"Multiply\nSF-015"| S_MUL
    S_ROUTER -->|"Divide\nnum2 not 0\nSF-016"| S_DIV
    S_ADD --> S_RESULT
    S_SUB --> S_RESULT
    S_MUL --> S_RESULT
    S_DIV --> S_RESULT
    S_RESULT --> U_VIEW
    U_VIEW --> U_DET_GW
    U_DET_GW -->|"Yes — SF-023\nBR-007"| U_DET
    U_DET --> S_EXPANDER
    S_EXPANDER --> S_SUC_END
    U_DET_GW -->|"No — SF-026"| S_SUC_END

    style U_START fill:#90EE90,stroke:#2d7a2d,color:#000
    style S_ERR_END fill:#FF4444,stroke:#8B0000,color:#fff
    style S_SUC_END fill:#90EE90,stroke:#2d7a2d,color:#000
    style S_GATE fill:#FFD700,stroke:#B8860B,color:#000
    style S_DIVCHK fill:#FFD700,stroke:#B8860B,color:#000
    style S_ROUTER fill:#FFD700,stroke:#B8860B,color:#000
    style U_DET_GW fill:#FFD700,stroke:#B8860B,color:#000
    style S_ERROR fill:#FF9999,stroke:#CC0000,color:#000
    style U_NUM1 fill:#CCE5FF,stroke:#4A90D9,color:#000
    style U_NUM2 fill:#CCE5FF,stroke:#4A90D9,color:#000
    style U_OP fill:#CCE5FF,stroke:#4A90D9,color:#000
    style U_SUBMIT fill:#CCE5FF,stroke:#4A90D9,color:#000
    style U_VIEW fill:#CCE5FF,stroke:#4A90D9,color:#000
    style U_DET fill:#CCE5FF,stroke:#4A90D9,color:#000
    style S_ADD fill:#E8F8E8,stroke:#5cb85c,color:#000
    style S_SUB fill:#E8F8E8,stroke:#5cb85c,color:#000
    style S_MUL fill:#E8F8E8,stroke:#5cb85c,color:#000
    style S_DIV fill:#E8F8E8,stroke:#5cb85c,color:#000
    style S_RESULT fill:#D4EDDA,stroke:#28a745,color:#000
    style S_CONFIG fill:#FFF8E7,stroke:#F5A623,color:#000
    style S_RENDER fill:#FFF8E7,stroke:#F5A623,color:#000
    style S_EXPANDER fill:#D4EDDA,stroke:#28a745,color:#000
```

---

### 3.2 Computation Engine — Decision Detail

This diagram zooms into the **Computation Engine** (Section 4 of `app.py`, lines 95–144) and shows every decision branch and arithmetic path in full detail, including the critical division-by-zero guard, the symbol assignment ordering, and the result display chain.

```mermaid
flowchart TD
    ENTRY(["⚡ Computation Block Entered\nsubmitted == True\napp.py line 95"])

    ENTRY --> DIVCHK{"GW-002\nIs operation == Divide\nAND num2 == 0?\napp.py line 121"}

    DIVCHK -->|"YES\nBR-001 Active\nError Path"| SYMBOL_D["Assign Symbol Early\nsymbol = div\napp.py line 109\nNote: assigned BEFORE guard\nso it is always available"]
    SYMBOL_D --> ERROR_BANNER["T-007 · st.error\nDivision by zero\nis not allowed\napp.py line 122"]
    ERROR_BANNER --> STOP["st.stop\napp.py line 123\nRaises StopException\nHalts script immediately"]
    STOP --> ERR_END(["🔴 ERROR END · EC-001\nresult never assigned\nNameError avoided\nst.success never rendered\nst.expander never reached"])

    DIVCHK -->|"NO\nValid Input\nProceed"| OP_GATE{"GW-003\nOperation Router\nWhich branch?\napp.py if/elif/else"}

    OP_GATE -->|"operation == Add\nSF-013"| BRANCH_ADD["T-008 · Addition\nresult = num1 + num2\nsymbol = +\nWF-001"]
    OP_GATE -->|"operation == Subtract\nSF-014"| BRANCH_SUB["T-009 · Subtraction\nresult = num1 - num2\nsymbol = -\nWF-002"]
    OP_GATE -->|"operation == Multiply\nSF-015"| BRANCH_MUL["T-010 · Multiplication\nresult = num1 x num2\nsymbol = x\nWF-003"]
    OP_GATE -->|"operation == Divide\nnum2 not 0\nSF-016"| BRANCH_DIV["T-011 · Division\nresult = num1 / num2\nsymbol = div\nWF-004"]

    BRANCH_ADD --> MERGE(["Merge Point\nresult and symbol assigned\nAll branches converge"])
    BRANCH_SUB --> MERGE
    BRANCH_MUL --> MERGE
    BRANCH_DIV --> MERGE

    MERGE --> SUCCESS_BANNER["T-012 · st.success\nResult: num1 OP num2 = result\nFormatted equation\nBR-005 · BR-006\napp.py line 133"]

    SUCCESS_BANNER --> EXPANDER_GW{"GW-004\nUser Expands\nComputation Details?\nBR-007"}

    EXPANDER_GW -->|"Yes\nBR-007 Active"| EXPANDER["T-015 · st.expander\nRender computation dict\nfirst_number · second_number\noperation · result\napp.py lines 138-144"]
    EXPANDER_GW -->|"No\nSkip details"| SUCCESS_END
    EXPANDER --> SUCCESS_END(["🟢 SUCCESS END · EVT-003\nCalculation Complete"])

    style ENTRY fill:#B0C4DE,stroke:#4682B4,color:#000
    style ERR_END fill:#FF4444,stroke:#8B0000,color:#fff
    style SUCCESS_END fill:#90EE90,stroke:#2d7a2d,color:#000
    style DIVCHK fill:#FFD700,stroke:#B8860B,color:#000
    style OP_GATE fill:#FFD700,stroke:#B8860B,color:#000
    style EXPANDER_GW fill:#FFD700,stroke:#B8860B,color:#000
    style ERROR_BANNER fill:#FF9999,stroke:#CC0000,color:#000
    style STOP fill:#FF6B6B,stroke:#8B0000,color:#fff
    style SYMBOL_D fill:#FFCCCC,stroke:#CC6666,color:#000
    style MERGE fill:#E0E0E0,stroke:#999999,color:#000
    style BRANCH_ADD fill:#E8F8E8,stroke:#5cb85c,color:#000
    style BRANCH_SUB fill:#E8F8E8,stroke:#5cb85c,color:#000
    style BRANCH_MUL fill:#E8F8E8,stroke:#5cb85c,color:#000
    style BRANCH_DIV fill:#E8F8E8,stroke:#5cb85c,color:#000
    style SUCCESS_BANNER fill:#D4EDDA,stroke:#28a745,color:#000
    style EXPANDER fill:#D4EDDA,stroke:#28a745,color:#000
```

---

### 3.3 Error Handling Sub-Process

This diagram isolates the **Division-by-Zero error sub-process** (Business Rule BR-001 / Error Condition EC-001) and shows the complete guard chain, the `st.stop()` termination mechanism, all side effects, and the recovery options available to the user. It maps directly to `app.py` lines 105–123.

```mermaid
flowchart TD
    TRIGGER(["⚡ Divide Branch Entered\noperation == Divide selected\napp.py line 105"])

    TRIGGER --> ASSIGN_SYM["Assign Display Symbol First\nsymbol = div\napp.py line 109\nDesign intent: symbol is always\navailable for error messages\nbefore any guard fires"]

    ASSIGN_SYM --> GUARD{"BR-001 Guard Check\nnum2 == 0?\napp.py line 121\nVR-001: Divisor Non-Zero Constraint"}

    GUARD -->|"TRUE\nDivisor is zero\nBR-001 triggered"| ERR_BANNER["st.error\nDivision by zero\nis not allowed\napp.py line 122\nRed error banner rendered in UI"]

    ERR_BANNER --> STOP_CALL["st.stop call\napp.py line 123\nInternally raises\nStreamlit StopException"]

    STOP_CALL --> EFFECTS["Side Effects Catalogue\n1. result never assigned\n2. NameError on result avoided\n3. st.success never rendered\n4. st.expander never reached\n5. Script execution halted\n   for this re-run only\n6. Form remains visible\n   for user correction"]

    EFFECTS --> ERR_END(["🔴 ERROR END · EVT-004 · EC-001\nDivision by Zero\nUser must correct num2\nand click Calculate again"])

    ERR_END --> RECOVERY["Recovery Options Available\n1. User changes num2 to non-zero\n2. User changes operation to\n   Add / Subtract / Multiply\n3. User clicks Calculate again\nProcess restarts from GW-001"]

    GUARD -->|"FALSE\nValid divisor\nnum2 not 0"| DIVISION["T-011 · Perform Division\nresult = num1 / num2\napp.py line 125\nNo ZeroDivisionError possible"]

    DIVISION --> HAPPY(["Continue to Happy Path\nResult display · T-012\nSF-020 active"])

    style TRIGGER fill:#FFE4B5,stroke:#DAA520,color:#000
    style ASSIGN_SYM fill:#FFF0E0,stroke:#D2691E,color:#000
    style GUARD fill:#FFD700,stroke:#B8860B,color:#000
    style ERR_BANNER fill:#FF9999,stroke:#CC0000,color:#000
    style STOP_CALL fill:#FF6B6B,stroke:#8B0000,color:#fff
    style EFFECTS fill:#FFCCCC,stroke:#CC6666,color:#000
    style ERR_END fill:#FF4444,stroke:#8B0000,color:#fff
    style RECOVERY fill:#FFFACD,stroke:#DAA520,color:#000
    style DIVISION fill:#E8F8E8,stroke:#5cb85c,color:#000
    style HAPPY fill:#90EE90,stroke:#2d7a2d,color:#000
```

---

### 3.4 Operation Routing Detail

This diagram shows the **four-way exclusive gateway** (GW-003 / DL-001) with all branch conditions, the co-located `symbol` variable assignment for each operation, the embedded division guard, and the convergence to the result display. It directly mirrors the `if/elif/else` chain at `app.py` lines 96–125.

```mermaid
flowchart LR
    GATE{"GW-003\nOperation Router\nExclusive XOR\napp.py lines 96-125"}

    GATE -->|"operation == Add\nSF-013 · WF-001"| A_TASK["T-008 · Compute Addition\nresult = num1 + num2\nsymbol = +\napp.py lines 97-98"]

    GATE -->|"operation == Subtract\nSF-014 · WF-002"| S_TASK["T-009 · Compute Subtraction\nresult = num1 - num2\nsymbol = -\napp.py lines 99-100"]

    GATE -->|"operation == Multiply\nSF-015 · WF-003"| M_TASK["T-010 · Compute Multiplication\nresult = num1 x num2\nsymbol = x\napp.py lines 101-102"]

    GATE -->|"operation == Divide\nSF-016 · else branch"| D_GUARD{"BR-001 Guard\nGW-002\nnum2 == 0?\napp.py line 121"}

    D_GUARD -->|"YES\nBR-001 error\nEC-001"| D_ERROR["T-007 · Display Error\nst.error + st.stop\napp.py lines 122-123"]
    D_GUARD -->|"NO\nSafe to divide\nWF-004"| D_TASK["T-011 · Compute Division\nresult = num1 / num2\nsymbol = div\napp.py line 125"]

    A_TASK --> JOIN(["Merge Point\nresult assigned\nsymbol assigned"])
    S_TASK --> JOIN
    M_TASK --> JOIN
    D_TASK --> JOIN

    D_ERROR --> ERR(["🔴 Error End · EVT-004"])
    JOIN --> DISPLAY["T-012 · Display Result\nst.success formatted equation\nnum1 symbol num2 = result\nBR-005 · BR-006"]
    DISPLAY --> SUCCESS(["🟢 Success continues"])

    style GATE fill:#FFD700,stroke:#B8860B,color:#000
    style D_GUARD fill:#FFD700,stroke:#B8860B,color:#000
    style D_ERROR fill:#FF9999,stroke:#CC0000,color:#000
    style ERR fill:#FF4444,stroke:#8B0000,color:#fff
    style A_TASK fill:#E8F8E8,stroke:#5cb85c,color:#000
    style S_TASK fill:#E8F8E8,stroke:#5cb85c,color:#000
    style M_TASK fill:#E8F8E8,stroke:#5cb85c,color:#000
    style D_TASK fill:#E8F8E8,stroke:#5cb85c,color:#000
    style JOIN fill:#E0E0E0,stroke:#999999,color:#000
    style DISPLAY fill:#D4EDDA,stroke:#28a745,color:#000
    style SUCCESS fill:#90EE90,stroke:#2d7a2d,color:#000
```

---

## 4. Process Metrics

### 4.1 Element Count

| BPMN Element Category | Count | Details |
|---|---|---|
| **Start Events** | 1 | EVT-001: User Opens Calculator App |
| **End Events** | 2 | EVT-003: Success End · EVT-004: Error End |
| **Intermediate / Boundary Events** | 1 | EVT-002: Form Submit Boundary Event |
| **Total Events** | **4** | |
| **User Tasks** | 6 | T-003, T-004, T-005, T-006, T-013, T-014 |
| **Service Tasks** | 5 | T-001, T-002, T-007, T-012, T-015 |
| **Script Tasks** | 4 | T-008, T-009, T-010, T-011 |
| **Total Tasks** | **15** | All activities combined |
| **Exclusive Gateways (XOR)** | 4 | GW-001, GW-002, GW-003, GW-004 |
| **Parallel Gateways (AND)** | 0 | No concurrent execution paths |
| **Total Gateways** | **4** | |
| **Sequence Flows** | 26 | SF-001 through SF-026 |
| **Pools / Participants** | 2 | User · Streamlit System |
| **Swimlanes** | 2 | User Lane · System Lane |
| **Data Objects** | 6 | num1 · num2 · operation · submitted · result · symbol |

### 4.2 Path Analysis

#### Happy Path — Add Operation (Minimum Required Steps)

The shortest complete path for a successful calculation (Add, no details expansion):

| Step # | Element ID | Type | Description |
|---|---|---|---|
| 1 | EVT-001 | Start Event | User opens calculator |
| 2 | T-001 | Service Task | Configure page metadata |
| 3 | T-002 | Service Task | Render calculator form |
| 4 | T-003 | User Task | Enter first number (num1) |
| 5 | T-004 | User Task | Enter second number (num2) |
| 6 | T-005 | User Task | Select operation (Add) |
| 7 | T-006 | User Task | Click Calculate button |
| 8 | GW-001 | XOR Gateway | submitted=True → proceed |
| 9 | GW-002 | XOR Gateway | Not divide/zero → proceed |
| 10 | GW-003 | XOR Gateway | Route to Add branch |
| 11 | T-008 | Script Task | Compute addition |
| 12 | T-012 | Service Task | Display formatted result |
| 13 | T-013 | User Task | View result equation |
| 14 | GW-004 | XOR Gateway | No details expansion |
| 15 | EVT-003 | End Event | Successful Calculation End |

**Happy path length: 15 steps** (12 tasks + 3 gateways + start/end)  
**With optional details panel: 17 steps** (adds T-014 and T-015)

#### Error Path — Division by Zero

| Step # | Element ID | Type | Description |
|---|---|---|---|
| 1 | EVT-001 | Start Event | User opens calculator |
| 2 | T-001 | Service Task | Configure page |
| 3 | T-002 | Service Task | Render form |
| 4 | T-003 | User Task | Enter num1 |
| 5 | T-004 | User Task | Enter 0 as num2 |
| 6 | T-005 | User Task | Select Divide |
| 7 | T-006 | User Task | Click Calculate |
| 8 | GW-001 | XOR Gateway | submitted=True |
| 9 | GW-002 | XOR Gateway | Divide AND num2=0 → error |
| 10 | T-007 | Service Task | Display error + st.stop() |
| 11 | EVT-004 | Error End Event | Execution halted |

**Error path length: 11 steps** (8 tasks + 2 gateways + start/error end)

### 4.3 Complete Execution Path Inventory

All distinct complete execution paths through the process:

| Path ID | Operation | Division Guard | Details Expanded | Steps | End Event |
|---|---|---|---|---|---|
| **P-01** | Add | N/A | No | 15 | Success |
| **P-02** | Add | N/A | Yes | 17 | Success |
| **P-03** | Subtract | N/A | No | 15 | Success |
| **P-04** | Subtract | N/A | Yes | 17 | Success |
| **P-05** | Multiply | N/A | No | 15 | Success |
| **P-06** | Multiply | N/A | Yes | 17 | Success |
| **P-07** | Divide (num2 ≠ 0) | Pass | No | 15 | Success |
| **P-08** | Divide (num2 ≠ 0) | Pass | Yes | 17 | Success |
| **P-09** | Divide (num2 = 0) | Fail | N/A | 11 | Error |

**Total distinct complete paths: 9**  
*(The page-load-only path where submitted=False does not constitute a complete business process execution)*

### 4.4 Cyclomatic Complexity

Cyclomatic complexity measures the number of linearly independent paths through the process graph. It indicates the minimum number of test cases required for full path coverage.

#### Formula 1 — McCabe's Formula: `V(G) = E − N + 2P`

| Variable | Value | Explanation |
|---|---|---|
| **E** (edges / sequence flows) | 26 | Total directed flows (SF-001 through SF-026) |
| **N** (nodes / process elements) | 23 | 15 tasks + 4 gateways + 4 events |
| **P** (connected components) | 1 | Single connected process graph |
| **V(G)** | **5** | `26 − 23 + 2(1) = 5` |

#### Formula 2 — Decision Point Count: `V(G) = binary_decisions + 1`

| Gateway | Branches | Binary Decision Equivalent |
|---|---|---|
| GW-001: Form Submitted? | 2 outgoing | 1 binary decision |
| GW-002: Division by Zero? | 2 outgoing | 1 binary decision |
| GW-003: Operation Router | 4 outgoing | 3 binary decisions |
| GW-004: Expand Details? | 2 outgoing | 1 binary decision |
| **Total binary decisions** | | **6** |
| **V(G) = 6 + 1** | | **7** |

#### Summary

| Metric | Value | Interpretation |
|---|---|---|
| **Base Cyclomatic Complexity** | **5** | E−N+2P formula |
| **Extended Cyclomatic Complexity** | **7** | All binary decision points |
| **Distinct Execution Paths** | **9** | All complete paths through process |
| **Error Paths** | **1** | Division by Zero (BR-001 / EC-001) |
| **Happy Paths** | **8** | 4 operations × 2 detail options |
| **Minimum Test Cases for Full Coverage** | **9** | One per distinct path |

> **Complexity Assessment**: A cyclomatic complexity of 5–7 falls in the **low complexity** range (1–10 = simple, well-structured). The process is linear, easy to understand, and has a single well-defined error boundary. All error handling is centralised at GW-002 with no cascading failures.

---

## 5. Lane / Participant Breakdown

### 5.1 User Lane

The User Lane contains all activities requiring direct human interaction. The user is the sole external actor and interacts with the system exclusively through the Streamlit web UI rendered in a browser.

```mermaid
flowchart LR
    subgraph UserLane["👤 USER LANE — All Human-Initiated Activities"]
        direction LR
        U1["T-003\nEnter First Number\nnum1 · float\ndefault: 0.0\n6 decimal places"]
        U2["T-004\nEnter Second Number\nnum2 · float\ndefault: 0.0\n6 decimal places"]
        U3["T-005\nSelect Operation\nAdd · Subtract\nMultiply · Divide\ndefault: Add"]
        U4["T-006\nClick Calculate\nExplicit Submit\nBR-004 · BR-007"]
        U5["T-013\nRead Result\nEquation Banner\nBR-005 · BR-006"]
        U6["T-014\nOptionally Expand\nComputation Details\nBR-007 optional"]
    end

    U1 --> U2 --> U3 --> U4 --> U5 -.->|"Optional\nBR-007"| U6

    style U1 fill:#CCE5FF,stroke:#4A90D9,color:#000
    style U2 fill:#CCE5FF,stroke:#4A90D9,color:#000
    style U3 fill:#CCE5FF,stroke:#4A90D9,color:#000
    style U4 fill:#CCE5FF,stroke:#4A90D9,color:#000
    style U5 fill:#CCE5FF,stroke:#4A90D9,color:#000
    style U6 fill:#CCE5FF,stroke:#4A90D9,color:#000
```

| Task ID | Activity | Trigger | Outputs |
|---|---|---|---|
| T-003 | Enter First Number | Form displayed | `num1` (float, 6 d.p.) |
| T-004 | Enter Second Number | After T-003 | `num2` (float, 6 d.p.) |
| T-005 | Select Operation | After T-004 | `operation` ∈ {Add, Subtract, Multiply, Divide} |
| T-006 | Click Calculate | User decision | `submitted = True`; script re-run triggered |
| T-013 | Read Result Equation | After T-012 completes | (perception — no system output generated) |
| T-014 | Expand Details Panel | User decision (optional) | Expander opens; T-015 triggered |

### 5.2 System Lane

The System Lane contains all automated activities performed by the Streamlit framework and the application's Python logic, with no direct user action required to execute them.

```mermaid
flowchart TB
    subgraph SystemLane["⚙️ SYSTEM LANE — All Automated Activities"]
        direction LR
        S_A["T-001\nConfigure Page\nst.set_page_config\nTitle · Icon · Layout"]
        S_B["T-002\nRender Calculator Form\nst.form context\nInputs · Selector · Button"]
        S_C["T-007\nDisplay Error + Halt\nst.error + st.stop\nBR-001 · EC-001"]
        S_D["T-008\nCompute Addition\nresult = num1 + num2\nBR-002"]
        S_E["T-009\nCompute Subtraction\nresult = num1 - num2\nBR-002"]
        S_F["T-010\nCompute Multiplication\nresult = num1 x num2\nBR-002"]
        S_G["T-011\nCompute Division\nresult = num1 / num2\nBR-001 · BR-002"]
        S_H["T-012\nDisplay Formatted Result\nst.success equation\nBR-005 · BR-006"]
        S_I["T-015\nRender Computation Details\nst.expander + st.write\nBR-007"]
    end

    style S_A fill:#FFF8E7,stroke:#F5A623,color:#000
    style S_B fill:#FFF8E7,stroke:#F5A623,color:#000
    style S_C fill:#FF9999,stroke:#CC0000,color:#000
    style S_D fill:#E8F8E8,stroke:#5cb85c,color:#000
    style S_E fill:#E8F8E8,stroke:#5cb85c,color:#000
    style S_F fill:#E8F8E8,stroke:#5cb85c,color:#000
    style S_G fill:#E8F8E8,stroke:#5cb85c,color:#000
    style S_H fill:#D4EDDA,stroke:#28a745,color:#000
    style S_I fill:#D4EDDA,stroke:#28a745,color:#000
```

| Task ID | Activity | BPMN Type | Key Code | Business Rule |
|---|---|---|---|---|
| T-001 | Configure Page | Service Task | `st.set_page_config(...)` | — |
| T-002 | Render Calculator Form | Service Task | `st.form("calculator_form")` | BR-004, BR-007 |
| T-007 | Display Error + Halt | Service Task | `st.error(...); st.stop()` | BR-001 |
| T-008 | Compute Addition | Script Task | `result = num1 + num2` | BR-002 |
| T-009 | Compute Subtraction | Script Task | `result = num1 - num2` | BR-002 |
| T-010 | Compute Multiplication | Script Task | `result = num1 * num2` | BR-002 |
| T-011 | Compute Division | Script Task | `result = num1 / num2` | BR-001, BR-002 |
| T-012 | Display Formatted Result | Service Task | `st.success(f"Result: ...")` | BR-005, BR-006 |
| T-015 | Render Computation Details | Service Task | `st.expander(...); st.write(...)` | BR-007 |

### 5.3 Responsibility Matrix (RACI)

| Activity | User | Streamlit System |
|---|---|---|
| Provide num1 input | **R** | I |
| Provide num2 input | **R** | I |
| Select operation | **R** | I |
| Trigger calculation | **R** | I |
| Render form | C | **R** |
| Validate division guard (BR-001) | — | **R** |
| Route operation (GW-003) | — | **R** |
| Compute arithmetic result | — | **R** |
| Assign display symbol | — | **R** |
| Display result equation | I | **R** |
| Display error banner | I | **R** |
| Optionally expand details | **R** | I |
| Render details expander panel | C | **R** |

*R = Responsible · C = Consulted · I = Informed*

---

## 6. Business Rule — Process Mapping

This table maps each Business Rule from `app.py` to the specific BPMN process element(s) that enforce it, enabling full traceability from specification to model.

| Rule ID | Rule Name | Priority | Enforcing BPMN Element(s) | Code Reference |
|---|---|---|---|---|
| **BR-001** | Division by Zero Prevention | 🔴 Critical | GW-002 (guard check) + T-007 (error banner) + EVT-004 (error end) | `app.py` lines 121–123 |
| **BR-002** | Floating-Point Precision (6 d.p.) | 🟡 Medium | T-003, T-004 (number input attributes) | `format="%.6f"` in `st.number_input` |
| **BR-003** | Default Operation is Addition | 🟢 Low | T-002 (form render — selectbox default) | `selectbox(index=0)` |
| **BR-004** | Calculation Gated by Explicit Submission | 🟠 High | GW-001 (submission gate) + EVT-002 (boundary event) | `if submitted:` line 95 |
| **BR-005** | Exactly Four Operations Supported | 🟠 High | GW-003 (4-way exclusive operation router) | `options=("Add","Subtract","Multiply","Divide")` |
| **BR-006** | Result Displayed as Formatted Equation | 🟡 Medium | T-012 (result display service task) | `st.success(f"Result: {num1} {symbol} {num2} = {result}")` |
| **BR-007** | Computation Details Available on Demand | 🟢 Low | GW-004 (expand check) + T-015 (details render) | `st.expander("Computation details")` |

---

## 7. Data Objects and Field Catalog

### 7.1 Process Data Flow

```mermaid
flowchart LR
    subgraph Inputs["📥 INPUT DATA OBJECTS\nCollected via st.form"]
        D1[("num1\nfloat\ndefault: 0.0\nformat: %.6f\nBR-002")]
        D2[("num2\nfloat\ndefault: 0.0\nformat: %.6f\nBR-002\nBR-001 constraint")]
        D3[("operation\nstring enum\ndefault: Add\nBR-003 · BR-005")]
        D4[("submitted\nboolean\nFalse on load\nTrue on submit\nBR-004")]
    end

    subgraph Computed["⚙️ COMPUTED DATA OBJECTS\nAssigned in computation block"]
        D5[("symbol\nstring\n+ or - or x or div\nAssigned per branch")]
        D6[("result\nfloat\nArithmetic output\nNever assigned on\nerror path")]
    end

    subgraph Outputs["📤 OUTPUT DATA OBJECTS\nRendered in UI"]
        D7[("Formatted Equation\nResult: num1 OP\nnum2 = result\nBR-005 · BR-006")]
        D8[("Computation Dict\nfirst_number\nsecond_number\noperation\nresult\nBR-007")]
    end

    D1 --> D6
    D2 --> D6
    D3 --> D5
    D3 --> D6
    D4 -->|"gates computation\nGW-001"| D6
    D1 --> D7
    D2 --> D7
    D5 --> D7
    D6 --> D7
    D6 --> D8
    D1 --> D8
    D2 --> D8
    D3 --> D8

    style D1 fill:#CCE5FF,stroke:#4A90D9,color:#000
    style D2 fill:#CCE5FF,stroke:#4A90D9,color:#000
    style D3 fill:#CCE5FF,stroke:#4A90D9,color:#000
    style D4 fill:#CCE5FF,stroke:#4A90D9,color:#000
    style D5 fill:#FFF8E7,stroke:#F5A623,color:#000
    style D6 fill:#FFF8E7,stroke:#F5A623,color:#000
    style D7 fill:#D4EDDA,stroke:#28a745,color:#000
    style D8 fill:#D4EDDA,stroke:#28a745,color:#000
```

### 7.2 Field Definitions

| Field | Type | Source | Default | Constraints | Lifecycle |
|---|---|---|---|---|---|
| `num1` | `float` | `st.number_input` | `0.0` | Format `%.6f`; any valid float | Input → Computation → Result display → Details dict |
| `num2` | `float` | `st.number_input` | `0.0` | Format `%.6f`; **must ≠ 0 when Divide (BR-001)** | Input → Guard check → Computation → Result display → Details dict |
| `operation` | `str` | `st.selectbox` | `"Add"` | ∈ {Add, Subtract, Multiply, Divide} (UI-enforced) | Input → GW-002 check → GW-003 routing → Details dict |
| `submitted` | `bool` | `st.form_submit_button` | `False` | `True` only on the re-run immediately following button click | GW-001 gate |
| `symbol` | `str` | Assigned in computation block | N/A | ∈ {+, -, ×, ÷}; assigned before guard in Divide branch | Computation → Result display |
| `result` | `float` | Arithmetic computation | N/A | **Never assigned on error path** (EC-001 guard) | Computation → Result display → Details dict |

### 7.3 Variable Lifecycle Diagram

```mermaid
flowchart LR
    F1(["Form\nSubmit"])

    F1 -->|"num1 captured"| V1["num1\nfloat value\navailable"]
    F1 -->|"num2 captured"| V2["num2\nfloat value\navailable"]
    F1 -->|"operation captured"| V3["operation\nstring value\navailable"]
    F1 -->|"submitted=True"| V4["submitted\nboolean\nTrue"]

    V3 -->|"operation branch\nselects symbol"| V5["symbol\nassigned\n+ - x div"]
    V1 & V2 & V3 -->|"arithmetic\nbranch executes"| V6{"result\nassigned?"}

    V6 -->|"Yes\nnormal path"| V7["result\nfloat value\navailable"]
    V6 -->|"No\nerror path\nst.stop"| V8["result\nNEVER assigned\nNameError avoided"]

    V1 & V2 & V5 & V7 --> DISP["Formatted Equation\nDisplayed"]
    V1 & V2 & V3 & V7 --> DICT["Computation Dict\nRendered in Expander"]

    style V6 fill:#FFD700,stroke:#B8860B,color:#000
    style V8 fill:#FF9999,stroke:#CC0000,color:#000
    style V7 fill:#E8F8E8,stroke:#5cb85c,color:#000
    style DISP fill:#D4EDDA,stroke:#28a745,color:#000
    style DICT fill:#D4EDDA,stroke:#28a745,color:#000
```

---

## Summary

| Metric | Value |
|---|---|
| **Total Process Tasks** | 15 (6 user · 5 service · 4 script) |
| **Total Gateways** | 4 (all Exclusive / XOR) |
| **Total Events** | 4 (1 start · 2 end · 1 boundary) |
| **Total Sequence Flows** | 26 |
| **Process Participants** | 2 (User · Streamlit System) |
| **Swimlanes** | 2 (User Lane · System Lane) |
| **Happy Path Length** | 15 steps (17 with optional details) |
| **Error Path Length** | 11 steps |
| **Total Distinct Execution Paths** | 9 |
| **Happy Paths** | 8 (4 operations × no/yes details) |
| **Error Paths** | 1 (BR-001: Division by Zero) |
| **Cyclomatic Complexity** | V(G) = 5 (base) · 7 (extended) |
| **Complexity Rating** | Low (1–10 scale) |
| **Business Rules Modelled** | 7 (BR-001 through BR-007) |
| **Error Conditions** | 1 (EC-001: Division by Zero) |
| **Validation Rules** | 3 (VR-001, VR-002, VR-003) |
| **BPMN Diagrams Generated** | 4 (swimlane · engine detail · error sub-process · routing) |

> **Process Assessment**: The Streamlit Calculator exhibits a **well-structured, low-complexity BPMN process** with a single clearly defined error boundary (BR-001) and a linear happy path. The four-way exclusive gateway (GW-003) is the primary branching point, mapping directly to the Python `if/elif/else` chain. All seven business rules are enforced at explicit, identifiable process elements, making the process fully traceable from specification to code. The `st.form` batching mechanism (GW-001) is the key architectural decision that prevents intermediate recalculation during input editing.

---

*Generated by BPMN Generator Agent · Source: `app.py` · Model Standard: BPMN 2.0 (Mermaid representation) · Diagrams: 4 Mermaid flowcharts*
