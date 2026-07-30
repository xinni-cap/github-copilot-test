using CalculatorApp.Services;

namespace CalculatorApp.Tests;

/// <summary>Unit tests for <see cref="OperationExtensions"/>.</summary>
public sealed class OperationExtensionsTests
{
    [Theory]
    [InlineData(Operation.Add,      "Add (+)")]
    [InlineData(Operation.Subtract, "Subtract (−)")]
    [InlineData(Operation.Multiply, "Multiply (×)")]
    [InlineData(Operation.Divide,   "Divide (÷)")]
    public void ToDisplayString_ReturnsExpectedLabel(Operation op, string expected)
    {
        Assert.Equal(expected, op.ToDisplayString());
    }

    [Fact]
    public void ToDisplayString_AllOperations_AreHandled()
    {
        // Ensure no operation falls through to the default ToString() branch.
        foreach (var op in Enum.GetValues<Operation>())
        {
            var display = op.ToDisplayString();
            Assert.False(string.IsNullOrWhiteSpace(display));
            // All known operations have parenthesised symbols.
            Assert.Contains("(", display);
        }
    }
}
