package com.example.groceryplanningservice.exception;

/**
 * Thrown when a GroceryList with the given ID or userId is not found.
 */
public class GroceryListNotFoundException extends RuntimeException {

    public GroceryListNotFoundException(String message) {
        super(message);
    }
}
