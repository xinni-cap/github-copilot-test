package com.example.calculator.model;

/**
 * Result model holding all details of a completed calculator operation.
 */
public class CalculatorResult {

    private Double num1;
    private Double num2;
    private String operation;
    private String symbol;
    private Double result;

    public CalculatorResult() {
    }

    public CalculatorResult(Double num1, Double num2, String operation, String symbol, Double result) {
        this.num1 = num1;
        this.num2 = num2;
        this.operation = operation;
        this.symbol = symbol;
        this.result = result;
    }

    /**
     * Returns a human-readable expression string, e.g. "1.5 + 2.0 = 3.5".
     * Numbers are formatted to up to 6 decimal places (trailing zeros stripped).
     */
    public String getExpression() {
        return String.format("%s %s %s = %s",
                formatNumber(num1),
                symbol,
                formatNumber(num2),
                formatNumber(result));
    }

    private String formatNumber(Double value) {
        if (value == null) {
            return "null";
        }
        // Format with up to 6 decimal places, strip trailing zeros
        String formatted = String.format("%.6f", value);
        // Remove trailing zeros after decimal point
        formatted = formatted.replaceAll("0+$", "");
        // Remove trailing decimal point
        formatted = formatted.replaceAll("\\.$", "");
        return formatted;
    }

    public Double getNum1() {
        return num1;
    }

    public void setNum1(Double num1) {
        this.num1 = num1;
    }

    public Double getNum2() {
        return num2;
    }

    public void setNum2(Double num2) {
        this.num2 = num2;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }
}
