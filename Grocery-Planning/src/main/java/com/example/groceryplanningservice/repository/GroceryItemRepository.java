package com.example.groceryplanningservice.repository;

import com.example.groceryplanningservice.entity.GroceryItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB repository for GroceryItem documents.
 */
@Repository
public interface GroceryItemRepository extends MongoRepository<GroceryItem, String> {

    /**
     * Retrieve all items that belong to a specific grocery list.
     *
     * @param groceryListId the parent list ID
     * @return list of grocery items
     */
    List<GroceryItem> findByGroceryListId(String groceryListId);

    /**
     * Find a specific item within a grocery list.
     *
     * @param id            the item ID
     * @param groceryListId the parent list ID
     * @return the matching grocery item, if present
     */
    Optional<GroceryItem> findByIdAndGroceryListId(String id, String groceryListId);

    /**
     * Delete all items belonging to a grocery list.
     *
     * @param groceryListId the parent list ID
     */
    void deleteByGroceryListId(String groceryListId);
}
