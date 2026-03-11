import streamlit as st

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
        ("Add", "Subtract", "Multiply", "Divide"),
        index=0,
    )

    submitted = st.form_submit_button("Calculate")

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
        symbol = "÷"
        if num2 == 0:
            st.error("Division by zero is not allowed.")
            st.stop()
        result = num1 / num2

    st.success(f"Result: {num1} {symbol} {num2} = {result}")

    with st.expander("Computation details"):
        st.write({
            "first_number": num1,
            "second_number": num2,
            "operation": operation,
            "result": result,
        })
