namespace CalculatorApi.Models;

/// <summary>
/// Result returned by the calculator API after a successful computation.
/// </summary>
/// <param name="Number1">The first operand used in the calculation.</param>
/// <param name="Number2">The second operand used in the calculation.</param>
/// <param name="Operation">The operation that was performed.</param>
/// <param name="Symbol">Human-readable operator symbol (e.g. "+", "−", "×", "÷").</param>
/// <param name="Result">The computed result.</param>
/// <param name="Expression">A formatted expression string, e.g. "3 + 4 = 7".</param>
public record CalculationResponse(
    double Number1,
    double Number2,
    OperationType Operation,
    string Symbol,
    double Result,
    string Expression
);
