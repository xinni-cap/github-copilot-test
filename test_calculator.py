"""Unit tests for the calculator module."""

import pytest

from calculator import OPERATIONS, calculate


class TestAdd:
    def test_positive_numbers(self) -> None:
        result, symbol = calculate(3.0, 4.0, "Add")
        assert result == 7.0
        assert symbol == "+"

    def test_negative_numbers(self) -> None:
        result, _ = calculate(-2.0, -3.0, "Add")
        assert result == -5.0

    def test_mixed_sign(self) -> None:
        result, _ = calculate(10.0, -3.0, "Add")
        assert result == 7.0

    def test_zero(self) -> None:
        result, _ = calculate(0.0, 0.0, "Add")
        assert result == 0.0


class TestSubtract:
    def test_positive_numbers(self) -> None:
        result, symbol = calculate(10.0, 3.0, "Subtract")
        assert result == 7.0
        assert symbol == "-"

    def test_negative_result(self) -> None:
        result, _ = calculate(3.0, 10.0, "Subtract")
        assert result == -7.0

    def test_zero(self) -> None:
        result, _ = calculate(5.0, 5.0, "Subtract")
        assert result == 0.0


class TestMultiply:
    def test_positive_numbers(self) -> None:
        result, symbol = calculate(4.0, 3.0, "Multiply")
        assert result == 12.0
        assert symbol == "×"

    def test_by_zero(self) -> None:
        result, _ = calculate(999.0, 0.0, "Multiply")
        assert result == 0.0

    def test_negative_numbers(self) -> None:
        result, _ = calculate(-2.0, -5.0, "Multiply")
        assert result == 10.0

    def test_mixed_sign(self) -> None:
        result, _ = calculate(-3.0, 4.0, "Multiply")
        assert result == -12.0


class TestDivide:
    def test_positive_numbers(self) -> None:
        result, symbol = calculate(10.0, 2.0, "Divide")
        assert result == 5.0
        assert symbol == "÷"

    def test_fractional_result(self) -> None:
        result, _ = calculate(1.0, 3.0, "Divide")
        assert result == pytest.approx(1 / 3)

    def test_divide_by_zero(self) -> None:
        with pytest.raises(ValueError, match="Division by zero"):
            calculate(10.0, 0.0, "Divide")

    def test_negative_divisor(self) -> None:
        result, _ = calculate(10.0, -2.0, "Divide")
        assert result == -5.0


class TestUnknownOperation:
    def test_raises_value_error(self) -> None:
        with pytest.raises(ValueError, match="Unknown operation"):
            calculate(1.0, 2.0, "Power")

    def test_empty_string(self) -> None:
        with pytest.raises(ValueError, match="Unknown operation"):
            calculate(1.0, 2.0, "")


class TestOperationsConstant:
    def test_contains_all_four_operations(self) -> None:
        assert set(OPERATIONS) == {"Add", "Subtract", "Multiply", "Divide"}
