"""
Simple Calculator — Streamlit Web Application
==============================================

Purpose
-------
A single-page, browser-based calculator that performs the four basic arithmetic
operations (addition, subtraction, multiplication, division) on two floating-point
numbers entered by the user.

Usage
-----
    streamlit run app.py

Then open the URL printed in the terminal (default: http://localhost:8501).

Dependencies
------------
    streamlit >= 1.40.0   — reactive web-UI framework (install via requirements.txt)

Architecture
------------
This application follows Streamlit's top-to-bottom reactive execution model: the
entire script re-runs on every user interaction.  A ``st.form`` context is used to
batch all inputs so that the calculation is triggered only when the user clicks the
"Calculate" button, not on every keystroke.

Business Rules
--------------
BR-001  Division by zero is not permitted; an error is shown and execution halts.
BR-002  Numeric inputs are captured with up to 6 decimal places of precision.
BR-003  Exactly four operations are supported: Add, Subtract, Multiply, Divide.
BR-004  Addition is the default pre-selected operation.
BR-005  The result is displayed as a human-readable formatted equation.
BR-006  Computation details are available on demand in a collapsible panel.
BR-007  Calculation is triggered only on explicit form submission.
"""

import streamlit as st

# ---------------------------------------------------------------------------
# SECTION 1 — PAGE CONFIGURATION
# Sets the browser-tab title, favicon emoji, and constrains the page to a
# centred layout so the calculator does not stretch across wide monitors.
# ---------------------------------------------------------------------------
st.set_page_config(page_title="Calculator", page_icon="🧮", layout="centered")

# ---------------------------------------------------------------------------
# SECTION 2 — PAGE HEADER
# Renders the primary heading and a brief descriptive caption.
# ---------------------------------------------------------------------------
st.title("Simple Calculator")
st.caption("Perform quick arithmetic with a clean Streamlit UI.")

# ---------------------------------------------------------------------------
# SECTION 3 — INPUT FORM
# All controls live inside a st.form block so that Streamlit does NOT re-run
# the script (and therefore does not recalculate) while the user is still
# typing or selecting values.  Calculation is deferred until the submit
# button is clicked.
# ---------------------------------------------------------------------------
with st.form("calculator_form"):
    # Place the two number inputs side-by-side using a 2-column layout.
    col1, col2 = st.columns(2)

    with col1:
        # First operand — defaults to 0.0; format="%.6f" allows up to 6
        # decimal places so that fractional values are captured accurately.
        num1 = st.number_input("First number", value=0.0, format="%.6f")
    with col2:
        # Second operand — also the divisor when operation == "Divide".
        # IMPORTANT: this value must not be zero for division (see BR-001).
        num2 = st.number_input("Second number", value=0.0, format="%.6f")

    # Operation selector — constrained to exactly four valid values so that
    # no invalid operation string can ever reach the computation block.
    # index=0 pre-selects "Add" as the default (see BR-003, BR-004).
    operation = st.selectbox(
        "Operation",
        ("Add", "Subtract", "Multiply", "Divide"),
        index=0,
    )

    # The form submit button sets `submitted = True` only for the re-run that
    # immediately follows the button click.  On every other re-run, `submitted`
    # is False and the computation block below is skipped entirely (see BR-007).
    submitted = st.form_submit_button("Calculate")

# ---------------------------------------------------------------------------
# SECTION 4 — COMPUTATION ENGINE
# Executes only after the user clicks "Calculate" (submitted == True).
# Routes to the correct arithmetic expression based on the selected operation
# and assigns a matching display symbol for the result equation.
# ---------------------------------------------------------------------------
if submitted:
    if operation == "Add":
        result = num1 + num2
        symbol = "+"
    elif operation == "Subtract":
        result = num1 - num2
        symbol = "-"
    elif operation == "Multiply":
        result = num1 * num2
        symbol = "×"
    else:
        # --- Division branch ---
        # The display symbol is assigned before the guard check so it is
        # always available for any subsequent error messages if needed.
        symbol = "÷"

        # BUSINESS RULE BR-001 — Division-by-Zero Guard
        # Division by zero is mathematically undefined and would raise a
        # ZeroDivisionError.  We detect this condition early and surface a
        # clear error message to the user instead of crashing.
        #
        # st.stop() raises a Streamlit StopException that immediately halts
        # further script execution for this re-run.  This prevents a
        # NameError on the `result` variable (which is never assigned in
        # this branch) and ensures that the success banner below is never
        # rendered in an error state.
        if num2 == 0:
            st.error("Division by zero is not allowed.")
            st.stop()  # Halt execution — nothing below this line will run.

        result = num1 / num2

    # -----------------------------------------------------------------------
    # SECTION 5 — RESULT DISPLAY
    # Renders the computed result as a formatted equation inside a green
    # success banner so it stands out clearly from the input form.
    # Format: "Result: <num1> <symbol> <num2> = <result>"
    # -----------------------------------------------------------------------
    st.success(f"Result: {num1} {symbol} {num2} = {result}")

    # Collapsible details panel — hidden by default to keep the UI clean.
    # Exposes the raw computation dictionary for transparency or debugging.
    # Users can click "Computation details" to expand and inspect the values.
    with st.expander("Computation details"):
        st.write({
            "first_number": num1,
            "second_number": num2,
            "operation": operation,
            "result": result,
        })
