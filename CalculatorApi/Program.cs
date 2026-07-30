using System.Text.Json.Serialization;
using CalculatorApi.Models;
using CalculatorApi.Services;

var builder = WebApplication.CreateBuilder(args);

// ── Services ──────────────────────────────────────────────────────────────────
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen(options =>
{
    options.SwaggerDoc("v1", new()
    {
        Title       = "Calculator API",
        Version     = "v1",
        Description = "A minimal .NET 8 API that performs Add, Subtract, Multiply, and Divide."
    });
    var xmlFile = $"{System.Reflection.Assembly.GetExecutingAssembly().GetName().Name}.xml";
    var xmlPath = Path.Combine(AppContext.BaseDirectory, xmlFile);
    if (File.Exists(xmlPath))
        options.IncludeXmlComments(xmlPath);
});

// Serialise enums as strings (e.g. "Add") rather than integers.
builder.Services.ConfigureHttpJsonOptions(opts =>
    opts.SerializerOptions.Converters.Add(new JsonStringEnumConverter()));

builder.Services.AddSingleton<ICalculatorService, CalculatorService>();

// ── App ───────────────────────────────────────────────────────────────────────
var app = builder.Build();

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

// ── Endpoints ─────────────────────────────────────────────────────────────────

// POST /api/calculate
// Body: { "number1": 10, "number2": 2, "operation": "Divide" }
app.MapPost("/api/calculate", (CalculationRequest request, ICalculatorService calculator) =>
{
    try
    {
        var response = calculator.Calculate(request);
        return Results.Ok(response);
    }
    catch (DivideByZeroException ex)
    {
        return Results.BadRequest(new { error = ex.Message });
    }
})
.WithName("Calculate")
.WithSummary("Perform a calculation")
.WithDescription(
    "Performs Add, Subtract, Multiply, or Divide on two numbers. " +
    "Returns 400 Bad Request when dividing by zero.")
.WithOpenApi()
.Produces<CalculationResponse>(StatusCodes.Status200OK)
.Produces(StatusCodes.Status400BadRequest);

// GET /api/calculate/add?num1=3&num2=4
app.MapGet("/api/calculate/add",
    (double num1, double num2, ICalculatorService calculator) =>
        Results.Ok(calculator.Calculate(new CalculationRequest(num1, num2, OperationType.Add))))
    .WithName("Add")
    .WithSummary("Add two numbers")
    .WithOpenApi()
    .Produces<CalculationResponse>();

// GET /api/calculate/subtract?num1=10&num2=3
app.MapGet("/api/calculate/subtract",
    (double num1, double num2, ICalculatorService calculator) =>
        Results.Ok(calculator.Calculate(new CalculationRequest(num1, num2, OperationType.Subtract))))
    .WithName("Subtract")
    .WithSummary("Subtract num2 from num1")
    .WithOpenApi()
    .Produces<CalculationResponse>();

// GET /api/calculate/multiply?num1=6&num2=7
app.MapGet("/api/calculate/multiply",
    (double num1, double num2, ICalculatorService calculator) =>
        Results.Ok(calculator.Calculate(new CalculationRequest(num1, num2, OperationType.Multiply))))
    .WithName("Multiply")
    .WithSummary("Multiply two numbers")
    .WithOpenApi()
    .Produces<CalculationResponse>();

// GET /api/calculate/divide?num1=10&num2=2
app.MapGet("/api/calculate/divide",
    (double num1, double num2, ICalculatorService calculator) =>
{
    try
    {
        return Results.Ok(calculator.Calculate(new CalculationRequest(num1, num2, OperationType.Divide)));
    }
    catch (DivideByZeroException ex)
    {
        return Results.BadRequest(new { error = ex.Message });
    }
})
.WithName("Divide")
.WithSummary("Divide num1 by num2")
.WithOpenApi()
.Produces<CalculationResponse>()
.Produces(StatusCodes.Status400BadRequest);

app.Run();

// Expose Program for integration tests.
public partial class Program { }
