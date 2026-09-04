package com.example.fooddatabase.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO used when creating or updating a food (request body).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for creating or updating a food")
public class FoodRequest {

    @Schema(description = "Name of the food", example = "Chicken Breast")
    private String name;

    @Schema(description = "Category of the food", example = "Meat")
    private String category;

    @Schema(description = "Short description of the food", example = "Skinless boneless chicken breast")
    private String description;

    @Schema(description = "Nutrition information embedded in the food")
    private NutritionDto nutrition;
}
