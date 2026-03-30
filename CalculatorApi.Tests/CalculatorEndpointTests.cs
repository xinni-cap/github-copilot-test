using System.Net;
using System.Net.Http.Json;
using System.Text.Json;
using System.Text.Json.Serialization;
using CalculatorApi.Models;
using FluentAssertions;
using Microsoft.AspNetCore.Mvc.Testing;

namespace CalculatorApi.Tests;

/// <summary>Integration tests for the calculator endpoints using the real ASP.NET Core pipeline.</summary>
public sealed class CalculatorEndpointTests : IClassFixture<WebApplicationFactory<Program>>
{
    private readonly HttpClient _client;

    private static readonly JsonSerializerOptions _jsonOptions = new()
    {
        PropertyNameCaseInsensitive = true,
        Converters = { new JsonStringEnumConverter() }
    };

    public CalculatorEndpointTests(WebApplicationFactory<Program> factory)
    {
        _client = factory.CreateClient();
    }

    // ── POST /api/calculate ───────────────────────────────────────────────────

    [Theory]
    [InlineData(OperationType.Add,      10, 5,  15)]
    [InlineData(OperationType.Subtract, 10, 5,   5)]
    [InlineData(OperationType.Multiply,  3, 4,  12)]
    [InlineData(OperationType.Divide,   10, 2,   5)]
    public async Task PostCalculate_ReturnsCorrectResult(
        OperationType operation, double num1, double num2, double expected)
    {
        var request = new CalculationRequest(num1, num2, operation);

        var response = await _client.PostAsJsonAsync("/api/calculate", request, _jsonOptions);

        response.StatusCode.Should().Be(HttpStatusCode.OK);

        var content = await response.Content.ReadAsStringAsync();
        var result  = JsonSerializer.Deserialize<CalculationResponse>(content, _jsonOptions);

        result.Should().NotBeNull();
        result!.Result.Should().Be(expected);
        result.Operation.Should().Be(operation);
    }

    [Fact]
    public async Task PostCalculate_DivideByZero_ReturnsBadRequest()
    {
        var request = new CalculationRequest(10, 0, OperationType.Divide);

        var response = await _client.PostAsJsonAsync("/api/calculate", request, _jsonOptions);

        response.StatusCode.Should().Be(HttpStatusCode.BadRequest);
    }

    // ── GET /api/calculate/add ────────────────────────────────────────────────

    [Fact]
    public async Task GetAdd_ReturnsSumOfTwoNumbers()
    {
        var response = await _client.GetAsync("/api/calculate/add?num1=7&num2=3");

        response.StatusCode.Should().Be(HttpStatusCode.OK);
        var result = await DeserializeAsync(response);
        result!.Result.Should().Be(10);
        result.Symbol.Should().Be("+");
    }

    // ── GET /api/calculate/subtract ───────────────────────────────────────────

    [Fact]
    public async Task GetSubtract_ReturnsDifference()
    {
        var response = await _client.GetAsync("/api/calculate/subtract?num1=10&num2=4");

        response.StatusCode.Should().Be(HttpStatusCode.OK);
        var result = await DeserializeAsync(response);
        result!.Result.Should().Be(6);
        result.Symbol.Should().Be("−");
    }

    // ── GET /api/calculate/multiply ───────────────────────────────────────────

    [Fact]
    public async Task GetMultiply_ReturnsProduct()
    {
        var response = await _client.GetAsync("/api/calculate/multiply?num1=6&num2=7");

        response.StatusCode.Should().Be(HttpStatusCode.OK);
        var result = await DeserializeAsync(response);
        result!.Result.Should().Be(42);
        result.Symbol.Should().Be("×");
    }

    // ── GET /api/calculate/divide ─────────────────────────────────────────────

    [Fact]
    public async Task GetDivide_ReturnsQuotient()
    {
        var response = await _client.GetAsync("/api/calculate/divide?num1=15&num2=3");

        response.StatusCode.Should().Be(HttpStatusCode.OK);
        var result = await DeserializeAsync(response);
        result!.Result.Should().Be(5);
        result.Symbol.Should().Be("÷");
    }

    [Fact]
    public async Task GetDivide_ByZero_ReturnsBadRequest()
    {
        var response = await _client.GetAsync("/api/calculate/divide?num1=5&num2=0");

        response.StatusCode.Should().Be(HttpStatusCode.BadRequest);
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private static async Task<CalculationResponse?> DeserializeAsync(HttpResponseMessage response)
    {
        var content = await response.Content.ReadAsStringAsync();
        return JsonSerializer.Deserialize<CalculationResponse>(content, _jsonOptions);
    }
}
