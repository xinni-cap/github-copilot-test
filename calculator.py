"""Core calculator logic, decoupled from the Streamlit UI."""

from __future__ import annotations


class DivisionByZeroError(ValueError):
    """Raised when a division-by-zero is attempted."""


def calculate(num1: float, num2: float, operation: str) -> float:
    """Perform *operation* on *num1* and *num2* and return the result.

    Parameters
    ----------
    num1:
        The first operand.
    num2:
        The second operand.
    operation:
        One of ``"Add"``, ``"Subtract"``, ``"Multiply"``, or ``"Divide"``.

    Raises
    ------
    DivisionByZeroError
        When *operation* is ``"Divide"`` and *num2* is zero.
    ValueError
        When *operation* is not one of the supported operations.
    """
    if operation == "Add":
        return num1 + num2
    if operation == "Subtract":
        return num1 - num2
    if operation == "Multiply":
        return num1 * num2
    if operation == "Divide":
        if num2 == 0:
            raise DivisionByZeroError("Division by zero is not allowed.")
        return num1 / num2
    raise ValueError(f"Unsupported operation: {operation!r}")


OPERATION_SYMBOLS: dict[str, str] = {
    "Add": "+",
    "Subtract": "-",
    "Multiply": "×",
    "Divide": "÷",
}
