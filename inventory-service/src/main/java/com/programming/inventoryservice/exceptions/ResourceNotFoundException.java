package com.programming.inventoryservice.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super("Resource not found.");
    }

    public ResourceNotFoundException(Object id) {
        super("Resource not found. Id " + id);
    }
}
