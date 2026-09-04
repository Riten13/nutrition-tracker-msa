package com.example.groceryplanningservice.repository;

import com.example.groceryplanningservice.entity.GroceryList;
import com.example.groceryplanningservice.entity.GroceryListStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * MongoDB repository for GroceryList documents.
 */
@Repository
public interface GroceryListRepository extends MongoRepository<GroceryList, String> {

    /**
     * Find the active grocery list for a given user.
     *
     * @param userId the user ID
     * @param status the list status (ACTIVE)
     * @return the matching grocery list, if present
     */
    Optional<GroceryList> findByUserIdAndStatus(Long userId, GroceryListStatus status);
}
