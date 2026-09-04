package com.example.mealplanning.repository;

import com.example.mealplanning.entity.MealPlan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MealPlanRepository extends MongoRepository<MealPlan, String> {

    Optional<MealPlan> findByUserId(Long userId);
}
