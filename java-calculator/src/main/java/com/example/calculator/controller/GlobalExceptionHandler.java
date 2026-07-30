package com.example.calculator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.stream.Collectors;

/**
 * Global exception handler using Spring 6 / RFC-9457 {@link ProblemDetail}.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setType(URI.create("https://example.com/errors/validation"));
        pd.setTitle("Validation Failed");
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> "'%s': %s".formatted(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.joining("; "));
        pd.setDetail(details);
        return pd;
    }

    @ExceptionHandler(ArithmeticException.class)
    public ProblemDetail handleArithmetic(ArithmeticException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        pd.setType(URI.create("https://example.com/errors/arithmetic"));
        pd.setTitle("Arithmetic Error");
        pd.setDetail(ex.getMessage());
        return pd;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setType(URI.create("https://example.com/errors/bad-request"));
        pd.setTitle("Bad Request");
        pd.setDetail(ex.getMessage());
        return pd;
    }
}
