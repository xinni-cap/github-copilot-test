using System.Net;
using System.Net.Http.Json;
using CalculatorApp.Models;
using Microsoft.AspNetCore.Mvc.Testing;

namespace CalculatorApp.Tests;

public sealed class CalculateEndpointTests : IClassFixture<WebApplicationFactory<Program>>
{
    private readonly HttpClient _client;

    public CalculateEndpointTests(WebApplicationFactory<Program> factory)
    {
        _client = factory.CreateClient();
    }

    [Theory]
    [InlineData(10.0,  5.0,  "Add",      15.0)]
    [InlineData(10.0,  5.0,  "Subtract",  5.0)]
    [InlineData(10.0,  5.0,  "Multiply", 50.0)]
    [InlineData(10.0,  5.0,  "Divide",    2.0)]
    public async Task Post_Calculate_ReturnsOkWithResult(
        double first, double second, string operation, double expected)
    {
        var payload = new { firstNumber = first, secondNumber = second, operation };
        var response = await _client.PostAsJsonAsync("/api/calculate", payload);

        response.EnsureSuccessStatusCode();

        var result = await response.Content.ReadFromJsonAsync<CalculationResult>();
        Assert.NotNull(result);
        Assert.Equal(expected, result.Result, precision: 10);
    }

    [Fact]
    public async Task Post_Calculate_DivideByZero_ReturnsBadRequest()
    {
        var payload = new { firstNumber = 5.0, secondNumber = 0.0, operation = "Divide" };
        var response = await _client.PostAsJsonAsync("/api/calculate", payload);

        Assert.Equal(HttpStatusCode.BadRequest, response.StatusCode);
    }

    [Fact]
    public async Task Get_Root_ServesIndexHtml()
    {
        var response = await _client.GetAsync("/");

        response.EnsureSuccessStatusCode();
        var contentType = response.Content.Headers.ContentType?.MediaType;
        Assert.Equal("text/html", contentType);
    }
}
