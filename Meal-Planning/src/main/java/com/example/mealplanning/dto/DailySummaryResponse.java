package com.example.mealplanning.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailySummaryResponse {

    private LocalDate date;

    private Double totalCalories;

    private Double totalProtein;

    private Double totalCarbs;

    private Double totalFat;
}
