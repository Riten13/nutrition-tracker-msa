package com.example.groceryplanningservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Response body for the estimated cost calculation of a grocery list.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Estimated cost response for a grocery list")
public class EstimatedCostResponse {

    @Schema(description = "ID of the grocery list", example = "list123")
    private String listId;

    @Schema(description = "Total estimated cost of all items (estimatedPrice × quantity per item)", example = "450.00")
    private BigDecimal estimatedCost;
}
