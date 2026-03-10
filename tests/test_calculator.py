"""Unit tests for the calculator module."""

import pytest

from calculator import DivisionByZeroError, calculate


class TestAdd:
    def test_add_two_positive_numbers(self):
        assert calculate(3.0, 4.0, "Add") == 7.0

    def test_add_negative_numbers(self):
        assert calculate(-2.0, -5.0, "Add") == -7.0

    def test_add_zero(self):
        assert calculate(0.0, 0.0, "Add") == 0.0

    def test_add_floats(self):
        assert calculate(1.5, 2.5, "Add") == pytest.approx(4.0)


class TestSubtract:
    def test_subtract_positive_numbers(self):
        assert calculate(10.0, 4.0, "Subtract") == 6.0

    def test_subtract_yields_negative(self):
        assert calculate(3.0, 7.0, "Subtract") == -4.0

    def test_subtract_zero(self):
        assert calculate(5.0, 0.0, "Subtract") == 5.0


class TestMultiply:
    def test_multiply_positive_numbers(self):
        assert calculate(3.0, 4.0, "Multiply") == 12.0

    def test_multiply_by_zero(self):
        assert calculate(9.0, 0.0, "Multiply") == 0.0

    def test_multiply_negative_numbers(self):
        assert calculate(-2.0, -3.0, "Multiply") == 6.0

    def test_multiply_floats(self):
        assert calculate(2.5, 4.0, "Multiply") == pytest.approx(10.0)


class TestDivide:
    def test_divide_positive_numbers(self):
        assert calculate(10.0, 2.0, "Divide") == 5.0

    def test_divide_yields_float(self):
        assert calculate(1.0, 3.0, "Divide") == pytest.approx(1 / 3)

    def test_divide_negative_dividend(self):
        assert calculate(-9.0, 3.0, "Divide") == -3.0

    def test_divide_by_zero_raises(self):
        with pytest.raises(DivisionByZeroError):
            calculate(5.0, 0.0, "Divide")


class TestUnsupportedOperation:
    def test_invalid_operation_raises(self):
        with pytest.raises(ValueError):
            calculate(1.0, 2.0, "Modulo")
