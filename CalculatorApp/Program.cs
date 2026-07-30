using CalculatorApp.Components;
using CalculatorApp.Services;

var builder = WebApplication.CreateBuilder(args);

// ── Razor / Blazor ──────────────────────────────────────────────────────────
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();

// ── Application services ────────────────────────────────────────────────────
builder.Services.AddSingleton<ICalculatorService, CalculatorService>();

// ── Health checks (useful for container/cloud deployments) ──────────────────
builder.Services.AddHealthChecks();

var app = builder.Build();

// ── HTTP pipeline ────────────────────────────────────────────────────────────
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error", createScopeForErrors: true);
}

app.UseStaticFiles();
app.UseAntiforgery();

app.MapHealthChecks("/healthz");

app.MapRazorComponents<App>()
    .AddInteractiveServerRenderMode();

app.Run();
