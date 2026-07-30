using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace CalculatorApi.Models;

/// <summary>
/// Input payload for a calculation request.
/// </summary>
/// <param name="Number1">The first operand.</param>
/// <param name="Number2">The second operand.</param>
/// <param name="Operation">The arithmetic operation to perform.</param>
public record CalculationRequest(
    [property: Required] double Number1,
    [property: Required] double Number2,
    [property: Required, JsonConverter(typeof(JsonStringEnumConverter))]
    OperationType Operation
);
