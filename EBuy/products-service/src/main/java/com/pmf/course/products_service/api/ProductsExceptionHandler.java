package com.pmf.course.products_service.api;

import com.pmf.course.products_service.exceptions.IncorrectSearchInputException;
import com.pmf.course.products_service.exceptions.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;

@ControllerAdvice
public class ProductsExceptionHandler {

    @ExceptionHandler(IncorrectSearchInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HashMap<String, String> handleIncorrectSearchInputException(IncorrectSearchInputException e) {
        var map = new HashMap<String, String>();
        map.put("error", e.getMessage());
        return map;
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public HashMap<String, String> handleProductNotFoundException(ProductNotFoundException e) {
        var map = new HashMap<String, String>();
        map.put("error", e.getMessage());
        return map;
    }
}
