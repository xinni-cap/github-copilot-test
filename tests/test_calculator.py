"""Tests for the calculator business logic."""

import pytest

from calculator import (
    DivisionByZeroError,
    add,
    calculate,
    divide,
    multiply,
    subtract,
)


# ---------------------------------------------------------------------------
# Individual operation tests
# ---------------------------------------------------------------------------


class TestAdd:
    def test_positive_numbers(self) -> None:
        assert add(2, 3) == 5

    def test_negative_numbers(self) -> None:
        assert add(-1, -4) == -5

    def test_floats(self) -> None:
        assert add(0.1, 0.2) == pytest.approx(0.3)

    def test_zero(self) -> None:
        assert add(0, 0) == 0


class TestSubtract:
    def test_positive_numbers(self) -> None:
        assert subtract(10, 4) == 6

    def test_result_negative(self) -> None:
        assert subtract(3, 7) == -4

    def test_floats(self) -> None:
        assert subtract(1.5, 0.5) == pytest.approx(1.0)

    def test_zero(self) -> None:
        assert subtract(0, 0) == 0


class TestMultiply:
    def test_positive_numbers(self) -> None:
        assert multiply(3, 4) == 12

    def test_by_zero(self) -> None:
        assert multiply(99, 0) == 0

    def test_negative(self) -> None:
        assert multiply(-2, 5) == -10

    def test_floats(self) -> None:
        assert multiply(2.5, 4.0) == pytest.approx(10.0)


class TestDivide:
    def test_positive_numbers(self) -> None:
        assert divide(10, 2) == 5.0

    def test_float_result(self) -> None:
        assert divide(7, 2) == pytest.approx(3.5)

    def test_negative_numerator(self) -> None:
        assert divide(-9, 3) == pytest.approx(-3.0)

    def test_by_zero_raises(self) -> None:
        with pytest.raises(DivisionByZeroError):
            divide(5, 0)


# ---------------------------------------------------------------------------
# calculate() dispatcher tests
# ---------------------------------------------------------------------------


class TestCalculate:
    def test_add_returns_symbol(self) -> None:
        result, symbol = calculate("Add", 1, 2)
        assert result == 3
        assert symbol == "+"

    def test_subtract_returns_symbol(self) -> None:
        result, symbol = calculate("Subtract", 5, 3)
        assert result == 2
        assert symbol == "-"

    def test_multiply_returns_symbol(self) -> None:
        result, symbol = calculate("Multiply", 4, 5)
        assert result == 20
        assert symbol == "×"

    def test_divide_returns_symbol(self) -> None:
        result, symbol = calculate("Divide", 10, 4)
        assert result == pytest.approx(2.5)
        assert symbol == "÷"

    def test_divide_by_zero_raises(self) -> None:
        with pytest.raises(DivisionByZeroError):
            calculate("Divide", 1, 0)

    def test_unknown_operation_raises(self) -> None:
        with pytest.raises(ValueError, match="Unknown operation"):
            calculate("Modulo", 10, 3)
