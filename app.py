import streamlit as st

from calculator import OPERATIONS, calculate

st.set_page_config(page_title="Calculator", page_icon="🧮", layout="centered")

st.title("Simple Calculator")
st.caption("Perform quick arithmetic with a clean Streamlit UI.")

with st.form("calculator_form"):
    col1, col2 = st.columns(2)

    with col1:
        num1 = st.number_input("First number", value=0.0, format="%.6f")
    with col2:
        num2 = st.number_input("Second number", value=0.0, format="%.6f")

    operation = st.selectbox(
        "Operation",
        OPERATIONS,
        index=0,
    )

    submitted = st.form_submit_button("Calculate")

if submitted:
    try:
        result, symbol = calculate(num1, num2, operation)
    except ValueError as exc:
        st.error(str(exc))
        st.stop()

    st.success(f"Result: {num1} {symbol} {num2} = {result}")

    with st.expander("Computation details"):
        st.write({
            "first_number": num1,
            "second_number": num2,
            "operation": operation,
            "result": result,
        })
