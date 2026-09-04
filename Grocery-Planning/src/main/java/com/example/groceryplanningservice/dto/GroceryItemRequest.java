package com.example.groceryplanningservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request body for adding a new grocery item to a list.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body to add a grocery item to a list")
public class GroceryItemRequest {

    @NotBlank(message = "foodId is required")
    @Schema(description = "ID of the food in the Food Database Service", example = "food456", requiredMode = Schema.RequiredMode.REQUIRED)
    private String foodId;

    @NotNull(message = "quantity is required")
    @Positive(message = "quantity must be positive")
    @Schema(description = "Quantity of the food item", example = "2.0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double quantity;

    @NotBlank(message = "unit is required")
    @Schema(description = "Unit of measurement (e.g. kg, litre, piece)", example = "kg", requiredMode = Schema.RequiredMode.REQUIRED)
    private String unit;

    @Schema(description = "Optional estimated price per unit", example = "150.00")
    private BigDecimal estimatedPrice;
}
