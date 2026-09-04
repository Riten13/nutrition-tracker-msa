package com.example.fooddatabase.exception;

/**
 * Thrown when a food with the given ID is not found in the database.
 */
public class FoodNotFoundException extends RuntimeException {

    public FoodNotFoundException(String foodId) {
        super("Food not found with id: " + foodId);
    }
}
