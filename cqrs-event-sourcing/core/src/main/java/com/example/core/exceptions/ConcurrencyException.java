package com.example.core.exceptions;

public class ConcurrencyException extends RuntimeException{
    public ConcurrencyException() {
    }

    public ConcurrencyException(String message) {
        super(message);
    }
}
