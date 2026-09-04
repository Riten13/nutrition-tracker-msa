package com.example.mealplanning.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the food object returned by the Food Database Service.
 * Only fields needed by this service are declared here.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodDto {

    private String id;

    private String name;

    private NutritionDto nutrition;
}
