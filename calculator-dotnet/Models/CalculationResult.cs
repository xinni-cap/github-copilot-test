namespace CalculatorApp.Models;

/// <summary>Represents the result of a calculation.</summary>
/// <param name="FirstNumber">The first operand used.</param>
/// <param name="SecondNumber">The second operand used.</param>
/// <param name="Operation">The operation that was performed.</param>
/// <param name="Symbol">The display symbol for the operation.</param>
/// <param name="Result">The computed result.</param>
public sealed record CalculationResult(
    double FirstNumber,
    double SecondNumber,
    Operation Operation,
    string Symbol,
    double Result
);
