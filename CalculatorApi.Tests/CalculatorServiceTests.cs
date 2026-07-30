using CalculatorApi.Models;
using CalculatorApi.Services;
using FluentAssertions;

namespace CalculatorApi.Tests;

/// <summary>Unit tests for <see cref="CalculatorService"/>.</summary>
public sealed class CalculatorServiceTests
{
    private readonly CalculatorService _sut = new();

    // ── Add ───────────────────────────────────────────────────────────────────

    [Theory]
    [InlineData(3,  4,   7)]
    [InlineData(0,  0,   0)]
    [InlineData(-1, 1,   0)]
    [InlineData(1.5, 2.5, 4.0)]
    public void Add_ReturnsCorrectResult(double a, double b, double expected)
    {
        var result = _sut.Calculate(new CalculationRequest(a, b, OperationType.Add));

        result.Result.Should().Be(expected);
        result.Operation.Should().Be(OperationType.Add);
        result.Symbol.Should().Be("+");
        result.Expression.Should().Contain("=");
    }

    // ── Subtract ──────────────────────────────────────────────────────────────

    [Theory]
    [InlineData(10, 3,   7)]
    [InlineData(0,  5,  -5)]
    [InlineData(-2, -3,  1)]
    public void Subtract_ReturnsCorrectResult(double a, double b, double expected)
    {
        var result = _sut.Calculate(new CalculationRequest(a, b, OperationType.Subtract));

        result.Result.Should().Be(expected);
        result.Operation.Should().Be(OperationType.Subtract);
        result.Symbol.Should().Be("−");
    }

    // ── Multiply ──────────────────────────────────────────────────────────────

    [Theory]
    [InlineData(6,   7,  42)]
    [InlineData(0,   9,   0)]
    [InlineData(-3,  4, -12)]
    [InlineData(1.5, 2,   3)]
    public void Multiply_ReturnsCorrectResult(double a, double b, double expected)
    {
        var result = _sut.Calculate(new CalculationRequest(a, b, OperationType.Multiply));

        result.Result.Should().Be(expected);
        result.Operation.Should().Be(OperationType.Multiply);
        result.Symbol.Should().Be("×");
    }

    // ── Divide ────────────────────────────────────────────────────────────────

    [Theory]
    [InlineData(10, 2,   5)]
    [InlineData(7,  2,   3.5)]
    [InlineData(-9, 3,  -3)]
    [InlineData(0,  5,   0)]
    public void Divide_ReturnsCorrectResult(double a, double b, double expected)
    {
        var result = _sut.Calculate(new CalculationRequest(a, b, OperationType.Divide));

        result.Result.Should().Be(expected);
        result.Operation.Should().Be(OperationType.Divide);
        result.Symbol.Should().Be("÷");
    }

    [Fact]
    public void Divide_ByZero_ThrowsDivideByZeroException()
    {
        var act = () => _sut.Calculate(new CalculationRequest(5, 0, OperationType.Divide));

        act.Should().Throw<DivideByZeroException>()
           .WithMessage("*Division by zero*");
    }

    // ── Expression string ─────────────────────────────────────────────────────

    [Fact]
    public void Calculate_ExpressionContainsAllParts()
    {
        var result = _sut.Calculate(new CalculationRequest(3, 4, OperationType.Add));

        result.Expression.Should().Contain("3");
        result.Expression.Should().Contain("4");
        result.Expression.Should().Contain("+");
        result.Expression.Should().Contain("7");
    }

    // ── Response properties ───────────────────────────────────────────────────

    [Fact]
    public void Calculate_ResponsePreservesInputNumbers()
    {
        var result = _sut.Calculate(new CalculationRequest(12.5, 7.3, OperationType.Subtract));

        result.Number1.Should().Be(12.5);
        result.Number2.Should().Be(7.3);
    }
}
