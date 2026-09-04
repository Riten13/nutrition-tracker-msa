package com.example.groceryplanningservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * Represents a single grocery item inside a GroceryList.
 * foodId is a reference to the Food Database Service – it is NOT stored here.
 * Nutrition, food name, and category are NOT duplicated in this service.
 * Maps to the "grocery_items" collection in MongoDB.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "grocery_items")
public class GroceryItem {

    @Id
    private String id;

    /** ID of the parent GroceryList. */
    private String groceryListId;

    /**
     * Reference to the food in the Food Database Service.
     * This is only a foreign key – food data is never duplicated here.
     */
    private String foodId;

    /** How many units of this food are needed. */
    private Double quantity;

    /** Unit of measurement, e.g. kg, litre, piece. */
    private String unit;

    /** Whether the item has been picked up / checked off. */
    private Boolean checked;

    /**
     * Optional estimated price per unit.
     * Used to calculate the total estimated cost of the grocery list.
     */
    private BigDecimal estimatedPrice;
}
