class DivisionByZeroError(ValueError):
    """Raised when division by zero is attempted."""


def calculate(num1: float, num2: float, operation: str) -> float:
    """Perform an arithmetic operation on two numbers.

    Args:
        num1: The first operand.
        num2: The second operand.
        operation: One of 'Add', 'Subtract', 'Multiply', or 'Divide'.

    Returns:
        The result of the arithmetic operation.

    Raises:
        DivisionByZeroError: If operation is 'Divide' and num2 is zero.
        ValueError: If operation is not recognized.
    """
    if operation == "Add":
        return num1 + num2
    elif operation == "Subtract":
        return num1 - num2
    elif operation == "Multiply":
        return num1 * num2
    elif operation == "Divide":
        if num2 == 0:
            raise DivisionByZeroError("Division by zero is not allowed.")
        return num1 / num2
    else:
        raise ValueError(f"Unknown operation: {operation}")
