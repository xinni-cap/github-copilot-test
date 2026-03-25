def calculate(num1, num2, operation):
    """Perform a calculator operation and return (result, symbol)."""
    if operation == "Add":
        return num1 + num2, "+"
    elif operation == "Subtract":
        return num1 - num2, "-"
    elif operation == "Multiply":
        return num1 * num2, "×"
    elif operation == "Divide":
        if num2 == 0:
            raise ValueError("Division by zero is not allowed.")
        return num1 / num2, "÷"
    else:
        raise ValueError(f"Unknown operation: {operation}")
