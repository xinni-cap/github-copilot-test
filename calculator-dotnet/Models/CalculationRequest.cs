using System.ComponentModel.DataAnnotations;

namespace CalculatorApp.Models;

/// <summary>Represents a request to perform a calculation.</summary>
/// <param name="FirstNumber">The first operand.</param>
/// <param name="SecondNumber">The second operand.</param>
/// <param name="Operation">The arithmetic operation to perform.</param>
public sealed record CalculationRequest(
    [property: Required] double FirstNumber,
    [property: Required] double SecondNumber,
    [property: Required] Operation Operation
);
