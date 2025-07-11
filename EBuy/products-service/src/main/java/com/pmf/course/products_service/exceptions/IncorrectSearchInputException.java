package com.pmf.course.products_service.exceptions;

public class IncorrectSearchInputException extends RuntimeException {
    public IncorrectSearchInputException() {
        super("The input was empty");
    }
}
