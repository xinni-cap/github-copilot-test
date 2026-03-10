import pytest

from calculator import DivisionByZeroError, calculate


class TestAdd:
    def test_positive_numbers(self):
        assert calculate(3, 4, "Add") == 7

    def test_negative_numbers(self):
        assert calculate(-2, -5, "Add") == -7

    def test_mixed_signs(self):
        assert calculate(-1, 5, "Add") == 4

    def test_floats(self):
        assert calculate(1.5, 2.5, "Add") == pytest.approx(4.0)

    def test_zero(self):
        assert calculate(0, 0, "Add") == 0


class TestSubtract:
    def test_positive_numbers(self):
        assert calculate(10, 3, "Subtract") == 7

    def test_negative_result(self):
        assert calculate(2, 5, "Subtract") == -3

    def test_floats(self):
        assert calculate(5.5, 2.5, "Subtract") == pytest.approx(3.0)

    def test_zero(self):
        assert calculate(0, 0, "Subtract") == 0


class TestMultiply:
    def test_positive_numbers(self):
        assert calculate(3, 4, "Multiply") == 12

    def test_negative_numbers(self):
        assert calculate(-2, 3, "Multiply") == -6

    def test_floats(self):
        assert calculate(2.5, 4.0, "Multiply") == pytest.approx(10.0)

    def test_by_zero(self):
        assert calculate(5, 0, "Multiply") == 0


class TestDivide:
    def test_positive_numbers(self):
        assert calculate(10, 2, "Divide") == 5

    def test_negative_numbers(self):
        assert calculate(-6, 3, "Divide") == -2

    def test_floats(self):
        assert calculate(7.5, 2.5, "Divide") == pytest.approx(3.0)

    def test_division_by_zero_raises(self):
        with pytest.raises(DivisionByZeroError):
            calculate(5, 0, "Divide")

    def test_division_by_zero_message(self):
        with pytest.raises(DivisionByZeroError, match="Division by zero is not allowed"):
            calculate(1, 0, "Divide")


class TestUnknownOperation:
    def test_unknown_operation_raises(self):
        with pytest.raises(ValueError):
            calculate(1, 2, "Modulo")
