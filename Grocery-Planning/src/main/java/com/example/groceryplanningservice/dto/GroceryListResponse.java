package com.example.groceryplanningservice.dto;

import com.example.groceryplanningservice.entity.GroceryListStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response body returned when reading a grocery list.
 * When items are included (e.g. GET /api/grocery-lists/{userId}),
 * the items list is populated; otherwise it is null.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Grocery list response")
public class GroceryListResponse {

    @Schema(description = "Unique grocery list ID", example = "list123")
    private String id;

    @Schema(description = "ID of the user who owns this list", example = "1")
    private Long userId;

    @Schema(description = "Current status of the list", example = "ACTIVE")
    private GroceryListStatus status;

    @Schema(description = "Timestamp when the list was created", example = "2026-09-04T21:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Items in this grocery list (present only when explicitly fetched)")
    private List<GroceryItemResponse> items;
}
