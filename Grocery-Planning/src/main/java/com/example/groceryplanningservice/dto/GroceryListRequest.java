package com.example.groceryplanningservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body for creating a new grocery list.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body to create a new grocery list")
public class GroceryListRequest {

    @NotNull(message = "userId is required")
    @Schema(description = "ID of the user who owns this grocery list", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;
}
