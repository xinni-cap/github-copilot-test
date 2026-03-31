namespace CalculatorApp.Services;

/// <summary>Arithmetic operations supported by the calculator.</summary>
public enum Operation
{
    Add,
    Subtract,
    Multiply,
    Divide
}

/// <summary>Represents a calculation request.</summary>
/// <param name="FirstNumber">The left-hand operand.</param>
/// <param name="SecondNumber">The right-hand operand.</param>
/// <param name="Operation">The arithmetic operation to perform.</param>
public record CalculationRequest(double FirstNumber, double SecondNumber, Operation Operation);

/// <summary>Represents a successful calculation result.</summary>
/// <param name="FirstNumber">The left-hand operand.</param>
/// <param name="SecondNumber">The right-hand operand.</param>
/// <param name="Operation">The operation that was performed.</param>
/// <param name="Result">The computed value.</param>
/// <param name="Symbol">The operator symbol used for display (e.g. "+", "−").</param>
public record CalculationResult(
    double FirstNumber,
    double SecondNumber,
    Operation Operation,
    double Result,
    string Symbol);

/// <summary>Defines the contract for the calculator business logic.</summary>
public interface ICalculatorService
{
    /// <summary>
    /// Performs the arithmetic operation described by <paramref name="request"/>.
    /// </summary>
    /// <param name="request">The calculation request.</param>
    /// <returns>A <see cref="CalculationResult"/> with the computed value and display metadata.</returns>
    /// <exception cref="DivideByZeroException">
    /// Thrown when <see cref="Operation.Divide"/> is requested and
    /// <see cref="CalculationRequest.SecondNumber"/> is zero.
    /// </exception>
    CalculationResult Calculate(CalculationRequest request);
}

/// <summary>Default implementation of <see cref="ICalculatorService"/>.</summary>
public sealed class CalculatorService : ICalculatorService
{
    /// <inheritdoc/>
    public CalculationResult Calculate(CalculationRequest request)
    {
        ArgumentNullException.ThrowIfNull(request);

        if (request.Operation == Operation.Divide && request.SecondNumber == 0)
            throw new DivideByZeroException("Division by zero is not allowed.");

        var (result, symbol) = request.Operation switch
        {
            Operation.Add      => (request.FirstNumber + request.SecondNumber, "+"),
            Operation.Subtract => (request.FirstNumber - request.SecondNumber, "−"),
            Operation.Multiply => (request.FirstNumber * request.SecondNumber, "×"),
            Operation.Divide   => (request.FirstNumber / request.SecondNumber, "÷"),
            _ => throw new ArgumentOutOfRangeException(
                     nameof(request.Operation),
                     request.Operation,
                     "Unknown operation.")
        };

        return new CalculationResult(
            request.FirstNumber,
            request.SecondNumber,
            request.Operation,
            result,
            symbol);
    }
}
