package com.example.core.exceptions;

public class AggregateNotFoundException extends RuntimeException {
    public AggregateNotFoundException() {
    }

    public AggregateNotFoundException(String message) {
        super(message);
    }
}
