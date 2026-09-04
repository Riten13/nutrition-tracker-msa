package com.example.mealplanning.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealPlanResponse {

    private String id;

    private Long userId;

    private LocalDate planDate;

    private LocalDateTime createdAt;

    private List<MealResponse> meals;
}
