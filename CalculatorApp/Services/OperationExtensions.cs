namespace CalculatorApp.Services;

/// <summary>Extension methods for the <see cref="Operation"/> enum.</summary>
public static class OperationExtensions
{
    /// <summary>Returns a human-readable label for an operation (e.g. "Add (+)").</summary>
    public static string ToDisplayString(this Operation operation) =>
        operation switch
        {
            Operation.Add      => "Add (+)",
            Operation.Subtract => "Subtract (−)",
            Operation.Multiply => "Multiply (×)",
            Operation.Divide   => "Divide (÷)",
            _                  => operation.ToString()
        };
}
