package com.example.groceryplanningservice.exception;

/**
 * Thrown when a GroceryItem with the given ID is not found in the list.
 */
public class GroceryItemNotFoundException extends RuntimeException {

    public GroceryItemNotFoundException(String message) {
        super(message);
    }
}
