package com.example.groceryplanningservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request body for updating an existing grocery item.
 * All fields are optional – only non-null values are applied.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body to update a grocery item")
public class GroceryItemUpdateRequest {

    @Positive(message = "quantity must be positive")
    @Schema(description = "New quantity (optional)", example = "3.0")
    private Double quantity;

    @Schema(description = "New unit of measurement (optional)", example = "kg")
    private String unit;

    @Schema(description = "New checked status (optional)", example = "true")
    private Boolean checked;

    @Schema(description = "New estimated price per unit (optional)", example = "200.00")
    private BigDecimal estimatedPrice;
}
