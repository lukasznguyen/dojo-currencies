package com.example.demo.exceptions;

public class CurrencyNotFound extends RuntimeException {
    public CurrencyNotFound(String message) {
        super(message);
    }

    public CurrencyNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
