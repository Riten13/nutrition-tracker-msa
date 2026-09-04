package com.example.mealplanning.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * OpenFeign client for the Food Database Service.
 *
 * Calls: GET http://localhost:8082/api/foods/{foodId}
 *
 * The base URL is read from application.yml -> food-service.url
 */
@FeignClient(name = "food-service", url = "${food-service.url}")
public interface FoodServiceClient {

    @GetMapping("/api/foods/{foodId}")
    FoodDto getFoodById(@PathVariable("foodId") String foodId);
}
