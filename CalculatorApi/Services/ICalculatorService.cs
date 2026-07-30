using CalculatorApi.Models;

namespace CalculatorApi.Services;

/// <summary>Performs basic arithmetic calculations.</summary>
public interface ICalculatorService
{
    /// <summary>
    /// Performs the arithmetic operation specified in <paramref name="request"/>.
    /// </summary>
    /// <param name="request">The calculation request containing both operands and the operation.</param>
    /// <returns>A <see cref="CalculationResponse"/> with the result and a formatted expression.</returns>
    /// <exception cref="DivideByZeroException">
    /// Thrown when <see cref="OperationType.Divide"/> is requested and
    /// <paramref name="request"/>.Number2 equals zero.
    /// </exception>
    CalculationResponse Calculate(CalculationRequest request);
}
