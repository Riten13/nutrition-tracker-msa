package com.example.groceryplanningservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Response body returned for a single grocery item.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Grocery item response")
public class GroceryItemResponse {

    @Schema(description = "Unique grocery item ID", example = "item123")
    private String id;

    @Schema(description = "ID of the parent grocery list", example = "list123")
    private String groceryListId;

    @Schema(description = "Reference ID of the food in the Food Database Service", example = "food456")
    private String foodId;

    @Schema(description = "Quantity of the food item", example = "2.0")
    private Double quantity;

    @Schema(description = "Unit of measurement", example = "kg")
    private String unit;

    @Schema(description = "Whether the item has been checked off", example = "false")
    private Boolean checked;

    @Schema(description = "Estimated price per unit (optional)", example = "150.00")
    private BigDecimal estimatedPrice;
}
