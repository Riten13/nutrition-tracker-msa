package com.example.groceryplanningservice.service;

import com.example.groceryplanningservice.client.FoodServiceClient;
import com.example.groceryplanningservice.dto.*;
import com.example.groceryplanningservice.entity.GroceryItem;
import com.example.groceryplanningservice.entity.GroceryList;
import com.example.groceryplanningservice.entity.GroceryListStatus;
import com.example.groceryplanningservice.exception.GroceryItemNotFoundException;
import com.example.groceryplanningservice.exception.GroceryListNotFoundException;
import com.example.groceryplanningservice.repository.GroceryItemRepository;
import com.example.groceryplanningservice.repository.GroceryListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for all grocery list and grocery item operations.
 *
 * <p>Responsibility chain:
 * <pre>
 * Controller
 *     └─ GroceryListService
 *           ├─ GroceryListRepository  (MongoDB – grocery_lists)
 *           ├─ GroceryItemRepository  (MongoDB – grocery_items)
 *           └─ FoodServiceClient      (OpenFeign → Food Database Service)
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class GroceryListService {

    private final GroceryListRepository groceryListRepository;
    private final GroceryItemRepository groceryItemRepository;
    private final FoodServiceClient foodServiceClient;

    // =========================================================================
    // Grocery List operations
    // =========================================================================

    /**
     * Create a new ACTIVE grocery list for a user.
     *
     * @param request the request containing the userId
     * @return the newly created grocery list (without items)
     */
    public GroceryListResponse createGroceryList(GroceryListRequest request) {
        GroceryList list = GroceryList.builder()
                .userId(request.getUserId())
                .status(GroceryListStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        GroceryList saved = groceryListRepository.save(list);
        return toListResponseWithoutItems(saved);
    }

    /**
     * Get the user's ACTIVE grocery list together with all its items.
     *
     * @param userId the user ID
     * @return the active grocery list with items
     * @throws GroceryListNotFoundException if no active list exists for the user
     */
    public GroceryListResponse getActiveGroceryList(Long userId) {
        GroceryList list = groceryListRepository
                .findByUserIdAndStatus(userId, GroceryListStatus.ACTIVE)
                .orElseThrow(() -> new GroceryListNotFoundException(
                        "No active grocery list found for userId: " + userId));

        List<GroceryItem> items = groceryItemRepository.findByGroceryListId(list.getId());
        List<GroceryItemResponse> itemResponses = items.stream()
                .map(this::toItemResponse)
                .collect(Collectors.toList());

        GroceryListResponse response = toListResponseWithoutItems(list);
        response.setItems(itemResponses);
        return response;
    }

    // =========================================================================
    // Grocery Item operations
    // =========================================================================

    /**
     * Add a grocery item to an existing list.
     *
     * <p>Before saving, this method calls the Food Database Service via OpenFeign
     * to validate that the food exists. Only the {@code foodId} is stored locally –
     * no food name, category, or nutrition data is duplicated.
     *
     * @param listId  the grocery list ID
     * @param request the item to add
     * @return the saved grocery item
     * @throws GroceryListNotFoundException if the list is not found
     */
    public GroceryItemResponse addGroceryItem(String listId, GroceryItemRequest request) {
        // Ensure the list exists
        GroceryList list = groceryListRepository.findById(listId)
                .orElseThrow(() -> new GroceryListNotFoundException(
                        "Grocery list not found with id: " + listId));

        // Validate that the food exists in the Food Database Service.
        // We call the service but intentionally discard all data except
        // confirming the food is real. foodId is the only thing we keep.
        foodServiceClient.getFoodById(request.getFoodId());

        GroceryItem item = GroceryItem.builder()
                .groceryListId(list.getId())
                .foodId(request.getFoodId())
                .quantity(request.getQuantity())
                .unit(request.getUnit())
                .checked(false)
                .estimatedPrice(request.getEstimatedPrice())
                .build();

        GroceryItem saved = groceryItemRepository.save(item);
        return toItemResponse(saved);
    }

    /**
     * Update an existing grocery item.
     * Only non-null fields in the request are applied (partial update).
     *
     * @param listId  the grocery list ID
     * @param itemId  the grocery item ID
     * @param request the fields to update
     * @return the updated grocery item
     * @throws GroceryListNotFoundException  if the list is not found
     * @throws GroceryItemNotFoundException if the item is not found in the list
     */
    public GroceryItemResponse updateGroceryItem(String listId, String itemId,
                                                  GroceryItemUpdateRequest request) {
        // Ensure the list exists
        groceryListRepository.findById(listId)
                .orElseThrow(() -> new GroceryListNotFoundException(
                        "Grocery list not found with id: " + listId));

        GroceryItem item = groceryItemRepository
                .findByIdAndGroceryListId(itemId, listId)
                .orElseThrow(() -> new GroceryItemNotFoundException(
                        "Grocery item not found with id: " + itemId + " in list: " + listId));

        // Apply only the fields that were provided
        if (request.getQuantity() != null) {
            item.setQuantity(request.getQuantity());
        }
        if (request.getUnit() != null) {
            item.setUnit(request.getUnit());
        }
        if (request.getChecked() != null) {
            item.setChecked(request.getChecked());
        }
        if (request.getEstimatedPrice() != null) {
            item.setEstimatedPrice(request.getEstimatedPrice());
        }

        GroceryItem updated = groceryItemRepository.save(item);
        return toItemResponse(updated);
    }

    /**
     * Delete a grocery item from a list.
     *
     * @param listId the grocery list ID
     * @param itemId the grocery item ID
     * @throws GroceryListNotFoundException  if the list is not found
     * @throws GroceryItemNotFoundException if the item is not found in the list
     */
    public void deleteGroceryItem(String listId, String itemId) {
        // Ensure the list exists
        groceryListRepository.findById(listId)
                .orElseThrow(() -> new GroceryListNotFoundException(
                        "Grocery list not found with id: " + listId));

        GroceryItem item = groceryItemRepository
                .findByIdAndGroceryListId(itemId, listId)
                .orElseThrow(() -> new GroceryItemNotFoundException(
                        "Grocery item not found with id: " + itemId + " in list: " + listId));

        groceryItemRepository.delete(item);
    }

    // =========================================================================
    // Estimated Cost
    // =========================================================================

    /**
     * Calculate the estimated total cost for all items in a grocery list.
     *
     * <p>Formula: Σ (estimatedPrice × quantity) for each item.
     * Items without an estimated price contribute 0 to the total.
     *
     * @param listId the grocery list ID
     * @return the estimated cost response
     * @throws GroceryListNotFoundException if the list is not found
     */
    public EstimatedCostResponse getEstimatedCost(String listId) {
        groceryListRepository.findById(listId)
                .orElseThrow(() -> new GroceryListNotFoundException(
                        "Grocery list not found with id: " + listId));

        List<GroceryItem> items = groceryItemRepository.findByGroceryListId(listId);

        BigDecimal total = items.stream()
                .filter(item -> item.getEstimatedPrice() != null)
                .map(item -> item.getEstimatedPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return EstimatedCostResponse.builder()
                .listId(listId)
                .estimatedCost(total)
                .build();
    }

    // =========================================================================
    // Mappers
    // =========================================================================

    private GroceryListResponse toListResponseWithoutItems(GroceryList list) {
        return GroceryListResponse.builder()
                .id(list.getId())
                .userId(list.getUserId())
                .status(list.getStatus())
                .createdAt(list.getCreatedAt())
                .build();
    }

    private GroceryItemResponse toItemResponse(GroceryItem item) {
        return GroceryItemResponse.builder()
                .id(item.getId())
                .groceryListId(item.getGroceryListId())
                .foodId(item.getFoodId())
                .quantity(item.getQuantity())
                .unit(item.getUnit())
                .checked(item.getChecked())
                .estimatedPrice(item.getEstimatedPrice())
                .build();
    }
}
