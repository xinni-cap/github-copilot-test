using CalculatorApp.Models;

namespace CalculatorApp.Services;

/// <summary>Performs arithmetic calculations.</summary>
public interface ICalculatorService
{
    /// <summary>
    /// Executes the requested arithmetic operation.
    /// </summary>
    /// <param name="request">The calculation request.</param>
    /// <returns>
    /// A <see cref="CalculationResult"/> on success, or <c>null</c> if the
    /// operation cannot be performed (e.g. division by zero).
    /// </returns>
    CalculationResult? Calculate(CalculationRequest request);
}
