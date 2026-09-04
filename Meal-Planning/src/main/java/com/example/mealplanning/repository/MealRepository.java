package com.example.mealplanning.repository;

import com.example.mealplanning.entity.Meal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepository extends MongoRepository<Meal, String> {

    List<Meal> findByMealPlanId(String mealPlanId);

    void deleteByMealPlanId(String mealPlanId);
}
