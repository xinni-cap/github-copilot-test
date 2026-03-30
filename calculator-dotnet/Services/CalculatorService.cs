using CalculatorApp.Models;

namespace CalculatorApp.Services;

/// <inheritdoc cref="ICalculatorService"/>
public sealed class CalculatorService : ICalculatorService
{
    /// <inheritdoc/>
    public CalculationResult? Calculate(CalculationRequest request)
    {
        var (first, second, operation) = request;

        var (result, symbol) = operation switch
        {
            Operation.Add      => (first + second,  "+"),
            Operation.Subtract => (first - second,  "−"),
            Operation.Multiply => (first * second,  "×"),
            Operation.Divide when second == 0 => (double.NaN, "÷"),
            Operation.Divide   => (first / second,  "÷"),
            _ => throw new ArgumentOutOfRangeException(nameof(operation), operation, "Unknown operation.")
        };

        if (double.IsNaN(result))
        {
            // Division by zero – signal the caller to handle the error.
            return null;
        }

        return new CalculationResult(first, second, operation, symbol, result);
    }
}
