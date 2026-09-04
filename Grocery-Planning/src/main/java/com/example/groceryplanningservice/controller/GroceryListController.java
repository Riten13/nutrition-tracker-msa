package com.example.groceryplanningservice.controller;

import com.example.groceryplanningservice.dto.*;
import com.example.groceryplanningservice.service.GroceryListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for all Grocery Planning API endpoints.
 *
 * <p>Base path: {@code /api/grocery-lists}
 */
@RestController
@RequestMapping("/api/grocery-lists")
@RequiredArgsConstructor
@Tag(name = "Grocery Lists", description = "APIs for managing grocery lists and grocery items")
public class GroceryListController {

    private final GroceryListService groceryListService;

    // =========================================================================
    // Grocery List endpoints
    // =========================================================================

    /**
     * POST /api/grocery-lists
     * Create a new ACTIVE grocery list for a user.
     */
    @Operation(
            summary = "Create a grocery list",
            description = "Creates a new ACTIVE grocery list for the specified user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Grocery list created successfully",
                    content = @Content(schema = @Schema(implementation = GroceryListResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping
    public ResponseEntity<GroceryListResponse> createGroceryList(
            @Valid @RequestBody GroceryListRequest request) {
        GroceryListResponse response = groceryListService.createGroceryList(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/grocery-lists/{userId}
     * Get the user's ACTIVE grocery list with all its items.
     */
    @Operation(
            summary = "Get user's active grocery list",
            description = "Returns the active grocery list for the given user, including all grocery items."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Active grocery list found",
                    content = @Content(schema = @Schema(implementation = GroceryListResponse.class))),
            @ApiResponse(responseCode = "404", description = "No active grocery list found for this user")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<GroceryListResponse> getActiveGroceryList(
            @Parameter(description = "ID of the user", example = "1")
            @PathVariable Long userId) {
        GroceryListResponse response = groceryListService.getActiveGroceryList(userId);
        return ResponseEntity.ok(response);
    }

    // =========================================================================
    // Grocery Item endpoints
    // =========================================================================

    /**
     * POST /api/grocery-lists/{listId}/items
     * Add a grocery item to an existing list.
     */
    @Operation(
            summary = "Add a grocery item",
            description = "Adds a new item to the specified grocery list. " +
                    "The Food Database Service is called to validate the foodId. " +
                    "Only foodId is stored – food name and nutrition are NOT duplicated here."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Grocery item added successfully",
                    content = @Content(schema = @Schema(implementation = GroceryItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Grocery list or food not found"),
            @ApiResponse(responseCode = "503", description = "Food Database Service unavailable")
    })
    @PostMapping("/{listId}/items")
    public ResponseEntity<GroceryItemResponse> addGroceryItem(
            @Parameter(description = "ID of the grocery list", example = "list123")
            @PathVariable String listId,
            @Valid @RequestBody GroceryItemRequest request) {
        GroceryItemResponse response = groceryListService.addGroceryItem(listId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/grocery-lists/{listId}/items/{itemId}
     * Update quantity, unit, checked status, or estimated price of an item.
     */
    @Operation(
            summary = "Update a grocery item",
            description = "Updates an existing grocery item. " +
                    "Only non-null fields in the request body are applied (partial update). " +
                    "You can update: quantity, unit, checked status, and estimatedPrice."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grocery item updated successfully",
                    content = @Content(schema = @Schema(implementation = GroceryItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Grocery list or item not found")
    })
    @PutMapping("/{listId}/items/{itemId}")
    public ResponseEntity<GroceryItemResponse> updateGroceryItem(
            @Parameter(description = "ID of the grocery list", example = "list123")
            @PathVariable String listId,
            @Parameter(description = "ID of the grocery item to update", example = "item123")
            @PathVariable String itemId,
            @Valid @RequestBody GroceryItemUpdateRequest request) {
        GroceryItemResponse response = groceryListService.updateGroceryItem(listId, itemId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/grocery-lists/{listId}/items/{itemId}
     * Remove a grocery item from the list.
     */
    @Operation(
            summary = "Delete a grocery item",
            description = "Removes a grocery item from the specified grocery list."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Grocery item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Grocery list or item not found")
    })
    @DeleteMapping("/{listId}/items/{itemId}")
    public ResponseEntity<Void> deleteGroceryItem(
            @Parameter(description = "ID of the grocery list", example = "list123")
            @PathVariable String listId,
            @Parameter(description = "ID of the grocery item to delete", example = "item123")
            @PathVariable String itemId) {
        groceryListService.deleteGroceryItem(listId, itemId);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/grocery-lists/{listId}/estimated-cost
     * Calculate the estimated total cost of all items in the list.
     */
    @Operation(
            summary = "Get estimated cost",
            description = "Calculates the estimated total cost of a grocery list. " +
                    "Formula: Σ (estimatedPrice × quantity) per item. " +
                    "Items without an estimatedPrice contribute 0 to the total."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estimated cost calculated successfully",
                    content = @Content(schema = @Schema(implementation = EstimatedCostResponse.class))),
            @ApiResponse(responseCode = "404", description = "Grocery list not found")
    })
    @GetMapping("/{listId}/estimated-cost")
    public ResponseEntity<EstimatedCostResponse> getEstimatedCost(
            @Parameter(description = "ID of the grocery list", example = "list123")
            @PathVariable String listId) {
        EstimatedCostResponse response = groceryListService.getEstimatedCost(listId);
        return ResponseEntity.ok(response);
    }
}
