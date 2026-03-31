"""Business logic for the calculator application."""

from collections.abc import Callable


class DivisionByZeroError(ValueError):
    """Raised when a division by zero is attempted."""


def add(a: float, b: float) -> float:
    """Return the sum of *a* and *b*."""
    return a + b


def subtract(a: float, b: float) -> float:
    """Return the difference of *a* minus *b*."""
    return a - b


def multiply(a: float, b: float) -> float:
    """Return the product of *a* and *b*."""
    return a * b


def divide(a: float, b: float) -> float:
    """Return the quotient of *a* divided by *b*.

    Raises:
        DivisionByZeroError: if *b* is zero.
    """
    if b == 0:
        raise DivisionByZeroError("Division by zero is not allowed.")
    return a / b


OPERATIONS: dict[str, tuple[str, Callable[[float, float], float]]] = {
    "Add": ("+", add),
    "Subtract": ("-", subtract),
    "Multiply": ("×", multiply),
    "Divide": ("÷", divide),
}


def calculate(operation: str, a: float, b: float) -> tuple[float, str]:
    """Perform *operation* on *a* and *b* and return ``(result, symbol)``.

    Args:
        operation: One of ``"Add"``, ``"Subtract"``, ``"Multiply"``, or
            ``"Divide"``.
        a: The first operand.
        b: The second operand.

    Returns:
        A ``(result, symbol)`` tuple where *symbol* is the operator character.

    Raises:
        ValueError: if *operation* is not recognised.
        DivisionByZeroError: if *operation* is ``"Divide"`` and *b* is zero.
    """
    if operation not in OPERATIONS:
        raise ValueError(f"Unknown operation: {operation!r}")
    symbol, func = OPERATIONS[operation]
    return func(a, b), symbol
