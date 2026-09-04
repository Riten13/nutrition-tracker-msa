package com.example.mealplanning.controller;

import com.example.mealplanning.dto.*;
import com.example.mealplanning.service.MealPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meal-plans")
@RequiredArgsConstructor
@Tag(name = "Meal Plans", description = "APIs for managing meal plans and logged meals")
public class MealPlanController {

    private final MealPlanService mealPlanService;

    // ─────────────────────────────────────────────────────────────
    // POST /api/meal-plans
    // ─────────────────────────────────────────────────────────────
    @Operation(
            summary = "Create a meal plan",
            description = "Creates a new meal plan for a user on a specific date."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Meal plan created successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<MealPlanResponse> createMealPlan(
            @RequestBody MealPlanRequest request) {
        MealPlanResponse response = mealPlanService.createMealPlan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ─────────────────────────────────────────────────────────────
    // GET /api/meal-plans/{userId}
    // ─────────────────────────────────────────────────────────────
    @Operation(
            summary = "Get user's meal plan",
            description = "Returns the meal plan (and all its meals) for a given userId."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Meal plan found"),
            @ApiResponse(responseCode = "404", description = "Meal plan not found for this user")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<MealPlanResponse> getMealPlan(
            @Parameter(description = "ID of the user", example = "1")
            @PathVariable Long userId) {
        MealPlanResponse response = mealPlanService.getMealPlanByUserId(userId);
        return ResponseEntity.ok(response);
    }

    // ─────────────────────────────────────────────────────────────
    // POST /api/meal-plans/{planId}/meals
    // ─────────────────────────────────────────────────────────────
    @Operation(
            summary = "Add a meal to a meal plan",
            description = """
                    Adds a meal to an existing meal plan.
                    The Food Database Service is called internally to validate the foodId.
                    Only foodId, mealType, and quantity are stored — no nutrition data is persisted.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Meal added successfully"),
            @ApiResponse(responseCode = "404", description = "Meal plan not found"),
            @ApiResponse(responseCode = "500", description = "Food Database Service unreachable or foodId invalid")
    })
    @PostMapping("/{planId}/meals")
    public ResponseEntity<MealResponse> addMeal(
            @Parameter(description = "ID of the meal plan", example = "64abc123...")
            @PathVariable String planId,
            @RequestBody MealRequest request) {
        MealResponse response = mealPlanService.addMeal(planId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ─────────────────────────────────────────────────────────────
    // GET /api/meal-plans/{planId}/meals
    // ─────────────────────────────────────────────────────────────
    @Operation(
            summary = "Get all meals for a meal plan",
            description = "Returns a list of all meals belonging to the specified meal plan."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of meals returned"),
            @ApiResponse(responseCode = "404", description = "Meal plan not found")
    })
    @GetMapping("/{planId}/meals")
    public ResponseEntity<List<MealResponse>> getMeals(
            @Parameter(description = "ID of the meal plan", example = "64abc123...")
            @PathVariable String planId) {
        List<MealResponse> response = mealPlanService.getMeals(planId);
        return ResponseEntity.ok(response);
    }

    // ─────────────────────────────────────────────────────────────
    // DELETE /api/meal-plans/{planId}/meals/{mealId}
    // ─────────────────────────────────────────────────────────────
    @Operation(
            summary = "Delete a meal",
            description = "Removes a specific meal from a meal plan."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Meal deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Meal plan or meal not found")
    })
    @DeleteMapping("/{planId}/meals/{mealId}")
    public ResponseEntity<Void> deleteMeal(
            @Parameter(description = "ID of the meal plan") @PathVariable String planId,
            @Parameter(description = "ID of the meal to delete") @PathVariable String mealId) {
        mealPlanService.deleteMeal(planId, mealId);
        return ResponseEntity.noContent().build();
    }

    // ─────────────────────────────────────────────────────────────
    // GET /api/meal-plans/{userId}/daily-summary
    // ─────────────────────────────────────────────────────────────
    @Operation(
            summary = "Get daily nutrition summary",
            description = """
                    Returns the total calories, protein, carbohydrates, and fat for a user's current meal plan.
                    Nutrition data is fetched live from the Food Database Service and scaled by each meal's quantity.
                    Nothing extra is persisted.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Daily summary returned"),
            @ApiResponse(responseCode = "404", description = "Meal plan not found for this user"),
            @ApiResponse(responseCode = "500", description = "Food Database Service unreachable")
    })
    @GetMapping("/{userId}/daily-summary")
    public ResponseEntity<DailySummaryResponse> getDailySummary(
            @Parameter(description = "ID of the user", example = "1")
            @PathVariable Long userId) {
        DailySummaryResponse response = mealPlanService.getDailySummary(userId);
        return ResponseEntity.ok(response);
    }
}
