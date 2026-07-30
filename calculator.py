"""Core arithmetic logic for the Simple Calculator."""

OPERATIONS = ("Add", "Subtract", "Multiply", "Divide")

_SYMBOLS: dict[str, str] = {
    "Add": "+",
    "Subtract": "-",
    "Multiply": "×",
    "Divide": "÷",
}


def calculate(num1: float, num2: float, operation: str) -> tuple[float, str]:
    """Perform an arithmetic calculation.

    Args:
        num1: The first operand.
        num2: The second operand.
        operation: One of "Add", "Subtract", "Multiply", or "Divide".

    Returns:
        A ``(result, symbol)`` tuple where *symbol* is the operator character
        used in the expression (e.g. ``"+"`` for addition).

    Raises:
        ValueError: If *operation* is not recognised or *num2* is zero when
            the operation is "Divide".
    """
    if operation not in _SYMBOLS:
        raise ValueError(f"Unknown operation: {operation!r}")

    if operation == "Divide":
        if num2 == 0:
            raise ValueError("Division by zero is not allowed.")
        return num1 / num2, _SYMBOLS[operation]

    if operation == "Add":
        return num1 + num2, _SYMBOLS[operation]
    if operation == "Subtract":
        return num1 - num2, _SYMBOLS[operation]
    # operation == "Multiply"
    return num1 * num2, _SYMBOLS[operation]
