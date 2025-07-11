package com.pmf.course.api_gateway.exceptions;

public class UnauthenticatedException extends RuntimeException {
    public UnauthenticatedException() {
        super("Missing or invalid authorization header");
    }
}
