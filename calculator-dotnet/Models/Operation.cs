using System.Text.Json.Serialization;

namespace CalculatorApp.Models;

/// <summary>Supported arithmetic operations.</summary>
[JsonConverter(typeof(JsonStringEnumConverter))]
public enum Operation
{
    Add,
    Subtract,
    Multiply,
    Divide
}
