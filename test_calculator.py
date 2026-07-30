import pytest

from calculator import DivisionByZeroError, calculate


class TestAdd:
    def test_positive_numbers(self):
        assert calculate(3, 2, "Add") == 5

    def test_negative_numbers(self):
        assert calculate(-1, -4, "Add") == -5

    def test_floats(self):
        assert calculate(0.1, 0.2, "Add") == pytest.approx(0.3)

    def test_zero(self):
        assert calculate(0, 0, "Add") == 0


class TestSubtract:
    def test_positive_result(self):
        assert calculate(10, 3, "Subtract") == 7

    def test_negative_result(self):
        assert calculate(3, 10, "Subtract") == -7

    def test_same_values(self):
        assert calculate(5, 5, "Subtract") == 0


class TestMultiply:
    def test_positive_numbers(self):
        assert calculate(3, 4, "Multiply") == 12

    def test_by_zero(self):
        assert calculate(99, 0, "Multiply") == 0

    def test_negative(self):
        assert calculate(-2, 5, "Multiply") == -10

    def test_floats(self):
        assert calculate(2.5, 4, "Multiply") == pytest.approx(10.0)


class TestDivide:
    def test_exact_division(self):
        assert calculate(10, 2, "Divide") == 5

    def test_float_result(self):
        assert calculate(1, 3, "Divide") == pytest.approx(1 / 3)

    def test_division_by_zero_raises(self):
        with pytest.raises(DivisionByZeroError):
            calculate(5, 0, "Divide")

    def test_negative_divisor(self):
        assert calculate(10, -2, "Divide") == -5


class TestUnknownOperation:
    def test_raises_value_error(self):
        with pytest.raises(ValueError, match="Unknown operation"):
            calculate(1, 2, "Modulo")
