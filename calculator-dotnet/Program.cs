using CalculatorApp.Models;
using CalculatorApp.Services;
using Microsoft.AspNetCore.Mvc;
using Microsoft.OpenApi.Models;

var builder = WebApplication.CreateBuilder(args);

// ── Services ────────────────────────────────────────────────────────────────
builder.Services.AddSingleton<ICalculatorService, CalculatorService>();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen(options =>
{
    options.SwaggerDoc("v1", new OpenApiInfo
    {
        Title   = "Calculator API",
        Version = "v1",
        Description = "A simple arithmetic calculator built with .NET 8 Minimal APIs."
    });
});

var app = builder.Build();

// ── Middleware ───────────────────────────────────────────────────────────────
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI(options => options.SwaggerEndpoint("/swagger/v1/swagger.json", "Calculator API v1"));
}

app.UseDefaultFiles();   // serves index.html automatically
app.UseStaticFiles();    // serves wwwroot/**

// ── API Endpoints ────────────────────────────────────────────────────────────

/// <summary>
/// POST /api/calculate
/// Accepts a JSON body with firstNumber, secondNumber, and operation.
/// Returns the calculation result or a 400 error for invalid input.
/// </summary>
app.MapPost("/api/calculate", (
    [FromBody] CalculationRequest request,
    ICalculatorService calculator) =>
{
    var result = calculator.Calculate(request);

    return result is null
        ? Results.BadRequest(new { error = "Division by zero is not allowed." })
        : Results.Ok(result);
})
.WithName("Calculate")
.WithSummary("Perform an arithmetic calculation")
.WithDescription("Accepts two numbers and an operation (Add, Subtract, Multiply, Divide) and returns the result.")
.Produces<CalculationResult>()
.Produces(StatusCodes.Status400BadRequest);

app.Run();

// Make the generated Program class visible to the integration test project.
public partial class Program { }
