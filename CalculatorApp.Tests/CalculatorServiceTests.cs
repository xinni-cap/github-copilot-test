using CalculatorApp.Models;
using CalculatorApp.Services;

namespace CalculatorApp.Tests;

public sealed class CalculatorServiceTests
{
    private readonly CalculatorService _sut = new();

    // ── Addition ─────────────────────────────────────────────────────────────

    [Theory]
    [InlineData(2.0,  3.0,  5.0)]
    [InlineData(-1.0, 1.0,  0.0)]
    [InlineData(0.0,  0.0,  0.0)]
    [InlineData(1.5,  2.5,  4.0)]
    public void Calculate_Add_ReturnsCorrectResult(double a, double b, double expected)
    {
        var request = new CalculationRequest(a, b, Operation.Add);
        var result  = _sut.Calculate(request);

        Assert.NotNull(result);
        Assert.Equal(expected, result.Result, precision: 10);
        Assert.Equal("+", result.Symbol);
        Assert.Equal(Operation.Add, result.Operation);
    }

    // ── Subtraction ───────────────────────────────────────────────────────────

    [Theory]
    [InlineData(5.0,  3.0,   2.0)]
    [InlineData(0.0,  0.0,   0.0)]
    [InlineData(1.0,  4.0,  -3.0)]
    public void Calculate_Subtract_ReturnsCorrectResult(double a, double b, double expected)
    {
        var request = new CalculationRequest(a, b, Operation.Subtract);
        var result  = _sut.Calculate(request);

        Assert.NotNull(result);
        Assert.Equal(expected, result.Result, precision: 10);
        Assert.Equal("−", result.Symbol);
    }

    // ── Multiplication ────────────────────────────────────────────────────────

    [Theory]
    [InlineData(3.0,  4.0,  12.0)]
    [InlineData(0.0,  99.0,  0.0)]
    [InlineData(-2.0, 5.0, -10.0)]
    public void Calculate_Multiply_ReturnsCorrectResult(double a, double b, double expected)
    {
        var request = new CalculationRequest(a, b, Operation.Multiply);
        var result  = _sut.Calculate(request);

        Assert.NotNull(result);
        Assert.Equal(expected, result.Result, precision: 10);
        Assert.Equal("×", result.Symbol);
    }

    // ── Division ──────────────────────────────────────────────────────────────

    [Theory]
    [InlineData(10.0,  2.0,  5.0)]
    [InlineData(7.0,   2.0,  3.5)]
    [InlineData(-6.0,  3.0, -2.0)]
    public void Calculate_Divide_ReturnsCorrectResult(double a, double b, double expected)
    {
        var request = new CalculationRequest(a, b, Operation.Divide);
        var result  = _sut.Calculate(request);

        Assert.NotNull(result);
        Assert.Equal(expected, result.Result, precision: 10);
        Assert.Equal("÷", result.Symbol);
    }

    [Fact]
    public void Calculate_DivideByZero_ReturnsNull()
    {
        var request = new CalculationRequest(5.0, 0.0, Operation.Divide);
        var result  = _sut.Calculate(request);

        Assert.Null(result);
    }

    // ── Result record properties ──────────────────────────────────────────────

    [Fact]
    public void CalculationResult_ContainsInputOperands()
    {
        var request = new CalculationRequest(3.0, 4.0, Operation.Add);
        var result  = _sut.Calculate(request)!;

        Assert.Equal(3.0, result.FirstNumber);
        Assert.Equal(4.0, result.SecondNumber);
    }
}
