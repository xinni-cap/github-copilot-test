# BPMN 2.0 Process Model — Simple Calculator (Streamlit)

**Repository**: xinni-cap/github-copilot-test  
**Generated**: 2025-07-14  
**Standard**: BPMN 2.0 (Business Process Model and Notation)

---

## Overview

This document describes the business processes of the Simple Calculator application using BPMN 2.0 notation, rendered as Mermaid flowcharts.

### Process Participants (Swimlanes)
- **User** — the human actor interacting with the browser
- **Streamlit App** — the Python/Streamlit server executing app.py
- **Calculation Engine** — the arithmetic computation logic within app.py

---

## 1. Main Calculation Process

```mermaid
flowchart TD
    subgraph User ["👤 User Swimlane"]
        U_Start(["▶ Start\nOpen Calculator App"])
        U_Input["Enter First Number (num1)"]
        U_Input2["Enter Second Number (num2)"]
        U_Select["Select Operation\nAdd / Subtract / Multiply / Divide"]
        U_Click["Click 'Calculate' Button"]
        U_View["View Result Banner"]
        U_Expand{"Expand Details?"}
        U_ViewDetail["View Computation Details Dict"]
        U_Again{"Calculate Again?"}
        U_End(["⏹ End\nClose App"])
    end

    subgraph App ["⚙️ Streamlit App Swimlane"]
        A_Init["Initialize Page\nset_page_config()"]
        A_Render["Render Form\nnum1, num2, operation, button"]
        A_Receive["Receive Form Submission\nsubmitted = True"]
        A_Route{"Route to\nOperation"}
        A_Success["Display Success Banner\nst.success()"]
        A_Expander["Render Expander\nst.expander()"]
        A_Error["Display Error\nst.error()"]
        A_Stop["Halt Execution\nst.stop()"]
    end

    subgraph Engine ["🧮 Calculation Engine Swimlane"]
        E_Add["Add\nresult = num1 + num2\nsymbol = '+'"]
        E_Sub["Subtract\nresult = num1 - num2\nsymbol = '-'"]
        E_Mul["Multiply\nresult = num1 × num2\nsymbol = '×'"]
        E_DivCheck{"Division Guard\nnum2 == 0?"}
        E_Div["Divide\nresult = num1 / num2\nsymbol = '÷'"]
    end

    U_Start --> A_Init
    A_Init --> A_Render
    A_Render --> U_Input
    U_Input --> U_Input2
    U_Input2 --> U_Select
    U_Select --> U_Click
    U_Click --> A_Receive
    A_Receive --> A_Route

    A_Route -->|"Add"| E_Add
    A_Route -->|"Subtract"| E_Sub
    A_Route -->|"Multiply"| E_Mul
    A_Route -->|"Divide"| E_DivCheck

    E_DivCheck -->|"num2 == 0"| A_Error
    A_Error --> A_Stop
    A_Stop --> U_Again

    E_DivCheck -->|"num2 != 0"| E_Div
    E_Add --> A_Success
    E_Sub --> A_Success
    E_Mul --> A_Success
    E_Div --> A_Success

    A_Success --> U_View
    U_View --> A_Expander
    A_Expander --> U_Expand

    U_Expand -->|"Yes"| U_ViewDetail
    U_Expand -->|"No"| U_Again
    U_ViewDetail --> U_Again

    U_Again -->|"Yes"| U_Input
    U_Again -->|"No"| U_End
```

---

## 2. Error Handling Sub-Process

```mermaid
flowchart TD
    subgraph ErrorProcess ["Error Handling Sub-Process: Division by Zero"]
        EP_Start(["▶ Trigger\noperation == Divide\nnum2 == 0"])
        EP_Check{"Confirm\nnum2 == 0?"}
        EP_Error["Display Error Banner\nst.error('Division by zero\nis not allowed.')"]
        EP_Stop["Halt Script Execution\nst.stop()"]
        EP_NoResult["No Result Computed\nNo Success Banner Shown"]
        EP_End(["⏹ End\nUser sees error only"])

        EP_Normal["Continue Normal Flow\nresult = num1 / num2"]
        EP_NormalEnd(["→ Success Path"])
    end

    EP_Start --> EP_Check
    EP_Check -->|"YES - zero divisor"| EP_Error
    EP_Error --> EP_Stop
    EP_Stop --> EP_NoResult
    EP_NoResult --> EP_End

    EP_Check -->|"NO - valid divisor"| EP_Normal
    EP_Normal --> EP_NormalEnd
```

---

## 3. BPMN Element Legend

| BPMN Element | Mermaid Representation | Usage in Calculator |
|---|---|---|
| **Start Event** | Rounded rectangle with ▶ | App open, trigger event |
| **End Event** | Rounded rectangle with ⏹ | App close, execution halt |
| **Task** | Rectangle | Enter input, click button, display result |
| **Exclusive Gateway (XOR)** | Diamond `{}` with conditions | Operation routing, error check, user decisions |
| **Sequence Flow** | Arrow `-->` | Process transitions |
| **Swimlane** | `subgraph` block | User, App, Engine participants |
| **Sub-Process** | Separate `subgraph` | Error handling sub-process |

---

## 4. Process Metrics

| Metric | Value |
|---|---|
| **Number of Tasks** | 14 |
| **Number of Gateways** | 5 |
| **Number of Swimlanes** | 3 (User, App, Engine) |
| **Happy Paths** | 4 (one per operation) |
| **Error Paths** | 1 (division by zero) |
| **Cyclomatic Complexity** | 6 (5 branches + 1) |
| **Process Participants** | 2 (Human: User; System: Calculator) |

---

## 5. Process Description

### Happy Path (Any Valid Calculation)
1. User opens the application in a browser
2. Streamlit initializes and renders the calculator form
3. User enters `num1`, `num2`, selects an operation, clicks "Calculate"
4. Streamlit receives the form submission (`submitted = True`)
5. The operation router determines the arithmetic path
6. The calculation engine computes the result
7. Streamlit displays a green success banner with the formatted result
8. User optionally expands "Computation details" to view the detail dict
9. User either starts another calculation or closes the app

### Error Path (Division by Zero)
1. Steps 1-5 same as above, with `operation = "Divide"`
2. Division Guard checks: `num2 == 0` → **TRUE**
3. `st.error("Division by zero is not allowed.")` displays red error banner
4. `st.stop()` halts all script execution immediately
5. No result is computed, no success banner is shown
6. User must modify `num2` to a non-zero value and resubmit
