using CalculatorApi.Models;

namespace CalculatorApi.Services;

/// <inheritdoc/>
public sealed class CalculatorService : ICalculatorService
{
    /// <inheritdoc/>
    public CalculationResponse Calculate(CalculationRequest request)
    {
        var (num1, num2, operation) = request;

        var (result, symbol) = operation switch
        {
            OperationType.Add      => (num1 + num2,  "+"),
            OperationType.Subtract => (num1 - num2,  "−"),
            OperationType.Multiply => (num1 * num2,  "×"),
            OperationType.Divide   => num2 == 0
                ? throw new DivideByZeroException("Division by zero is not allowed.")
                : (num1 / num2, "÷"),
            _ => throw new ArgumentOutOfRangeException(nameof(request.Operation),
                     $"Unknown operation: {operation}")
        };

        return new CalculationResponse(
            Number1:    num1,
            Number2:    num2,
            Operation:  operation,
            Symbol:     symbol,
            Result:     result,
            Expression: $"{num1} {symbol} {num2} = {result}"
        );
    }
}
