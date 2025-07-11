package com.pmf.course.api_gateway;

import com.pmf.course.api_gateway.exceptions.UnauthenticatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class UnauthenticatedExceptionHandler {

    @ExceptionHandler(UnauthenticatedException.class)
    public Mono<ResponseEntity<String>> handleUnauthenticatedException(UnauthenticatedException ex) {
        return Mono.just(
                ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .contentType(MediaType.TEXT_PLAIN)
                        .body(ex.getMessage())
        );
    }
}