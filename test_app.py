import pytest
from calculator import calculate


def test_add():
    result, symbol = calculate(3, 4, "Add")
    assert result == 7
    assert symbol == "+"


def test_subtract():
    result, symbol = calculate(10, 3, "Subtract")
    assert result == 7
    assert symbol == "-"


def test_multiply():
    result, symbol = calculate(3, 4, "Multiply")
    assert result == 12
    assert symbol == "×"


def test_divide():
    result, symbol = calculate(10, 2, "Divide")
    assert result == 5
    assert symbol == "÷"


def test_divide_by_zero():
    with pytest.raises(ValueError, match="Division by zero"):
        calculate(10, 0, "Divide")


def test_add_floats():
    result, _ = calculate(1.5, 2.5, "Add")
    assert result == pytest.approx(4.0)


def test_subtract_negative():
    result, _ = calculate(3, 5, "Subtract")
    assert result == -2
