package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {BadRequestException.class, CurrencyNotFound.class})
    public ResponseEntity<Object> runtimeExceptionHandler(RuntimeException e) {
        HttpStatus statusCode = HttpStatus.BAD_REQUEST;
        ApiExceptionResponse apiException = new ApiExceptionResponse(
                e.getMessage(),
                statusCode
        );
        return new ResponseEntity<>(apiException, statusCode);
    }
}
