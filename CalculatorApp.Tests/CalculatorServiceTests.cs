using CalculatorApp.Services;

namespace CalculatorApp.Tests;

/// <summary>Unit tests for <see cref="CalculatorService"/>.</summary>
public sealed class CalculatorServiceTests
{
    private readonly CalculatorService _sut = new();

    // ── Addition ─────────────────────────────────────────────────────────────

    [Theory]
    [InlineData(1, 2, 3)]
    [InlineData(0, 0, 0)]
    [InlineData(-5, 5, 0)]
    [InlineData(-3, -7, -10)]
    [InlineData(1.5, 2.5, 4.0)]
    public void Calculate_Add_ReturnsCorrectResult(double a, double b, double expected)
    {
        var request = new CalculationRequest(a, b, Operation.Add);
        var result = _sut.Calculate(request);
        Assert.Equal(expected, result.Result, precision: 10);
    }

    [Fact]
    public void Calculate_Add_ReturnsCorrectSymbol()
    {
        var result = _sut.Calculate(new CalculationRequest(1, 2, Operation.Add));
        Assert.Equal("+", result.Symbol);
    }

    // ── Subtraction ──────────────────────────────────────────────────────────

    [Theory]
    [InlineData(5, 3, 2)]
    [InlineData(0, 0, 0)]
    [InlineData(3, 5, -2)]
    [InlineData(-3, -7, 4)]
    [InlineData(1.5, 0.5, 1.0)]
    public void Calculate_Subtract_ReturnsCorrectResult(double a, double b, double expected)
    {
        var request = new CalculationRequest(a, b, Operation.Subtract);
        var result = _sut.Calculate(request);
        Assert.Equal(expected, result.Result, precision: 10);
    }

    [Fact]
    public void Calculate_Subtract_ReturnsCorrectSymbol()
    {
        var result = _sut.Calculate(new CalculationRequest(5, 3, Operation.Subtract));
        Assert.Equal("−", result.Symbol);
    }

    // ── Multiplication ───────────────────────────────────────────────────────

    [Theory]
    [InlineData(3, 4, 12)]
    [InlineData(0, 100, 0)]
    [InlineData(-2, 5, -10)]
    [InlineData(-3, -4, 12)]
    [InlineData(2.5, 4, 10.0)]
    public void Calculate_Multiply_ReturnsCorrectResult(double a, double b, double expected)
    {
        var request = new CalculationRequest(a, b, Operation.Multiply);
        var result = _sut.Calculate(request);
        Assert.Equal(expected, result.Result, precision: 10);
    }

    [Fact]
    public void Calculate_Multiply_ReturnsCorrectSymbol()
    {
        var result = _sut.Calculate(new CalculationRequest(3, 4, Operation.Multiply));
        Assert.Equal("×", result.Symbol);
    }

    // ── Division ─────────────────────────────────────────────────────────────

    [Theory]
    [InlineData(10, 2, 5)]
    [InlineData(1, 3, 0.3333333333)]
    [InlineData(-9, 3, -3)]
    [InlineData(-6, -2, 3)]
    [InlineData(7.5, 2.5, 3.0)]
    public void Calculate_Divide_ReturnsCorrectResult(double a, double b, double expected)
    {
        var request = new CalculationRequest(a, b, Operation.Divide);
        var result = _sut.Calculate(request);
        Assert.Equal(expected, result.Result, precision: 9);
    }

    [Fact]
    public void Calculate_Divide_ReturnsCorrectSymbol()
    {
        var result = _sut.Calculate(new CalculationRequest(10, 2, Operation.Divide));
        Assert.Equal("÷", result.Symbol);
    }

    [Fact]
    public void Calculate_DivideByZero_ThrowsDivideByZeroException()
    {
        var request = new CalculationRequest(5, 0, Operation.Divide);
        Assert.Throws<DivideByZeroException>(() => _sut.Calculate(request));
    }

    [Fact]
    public void Calculate_DivideByZero_ExceptionMessageIsDescriptive()
    {
        var request = new CalculationRequest(5, 0, Operation.Divide);
        var ex = Assert.Throws<DivideByZeroException>(() => _sut.Calculate(request));
        Assert.Contains("zero", ex.Message, StringComparison.OrdinalIgnoreCase);
    }

    // ── Result record integrity ───────────────────────────────────────────────

    [Fact]
    public void Calculate_Result_ContainsInputOperands()
    {
        var request = new CalculationRequest(7, 3, Operation.Add);
        var result = _sut.Calculate(request);

        Assert.Equal(7, result.FirstNumber);
        Assert.Equal(3, result.SecondNumber);
        Assert.Equal(Operation.Add, result.Operation);
    }

    // ── Null guard ────────────────────────────────────────────────────────────

    [Fact]
    public void Calculate_NullRequest_ThrowsArgumentNullException()
    {
        Assert.Throws<ArgumentNullException>(() => _sut.Calculate(null!));
    }

    // ── Large and edge-case values ────────────────────────────────────────────

    [Fact]
    public void Calculate_Add_LargeNumbers_ReturnsCorrectResult()
    {
        var request = new CalculationRequest(double.MaxValue / 2, double.MaxValue / 2, Operation.Add);
        var result = _sut.Calculate(request);
        Assert.Equal(double.MaxValue, result.Result, precision: 0);
    }

    [Fact]
    public void Calculate_Divide_ZeroByNonZero_ReturnsZero()
    {
        var result = _sut.Calculate(new CalculationRequest(0, 99, Operation.Divide));
        Assert.Equal(0, result.Result);
    }
}
