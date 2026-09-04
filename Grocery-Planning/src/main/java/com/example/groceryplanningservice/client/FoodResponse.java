package com.example.groceryplanningservice.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Minimal representation of a Food returned by the Food Database Service.
 * Only the fields needed by the Grocery Planning Service are mapped here.
 * Nutrition data is intentionally omitted.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Minimal food information received from the Food Database Service")
public class FoodResponse {

    @Schema(description = "Unique food ID", example = "food456")
    private String id;

    @Schema(description = "Name of the food", example = "Brown Rice")
    private String name;

    @Schema(description = "Category of the food", example = "Grains")
    private String category;
}
