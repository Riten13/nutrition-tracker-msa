package com.example.groceryplanningservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Represents a grocery list owned by a user.
 * A grocery list can be ACTIVE or COMPLETED.
 * Maps to the "grocery_lists" collection in MongoDB.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "grocery_lists")
public class GroceryList {

    @Id
    private String id;

    /** ID of the user who owns this grocery list. */
    private Long userId;

    /** Current status: ACTIVE or COMPLETED. */
    private GroceryListStatus status;

    /** Timestamp when the list was created. */
    private LocalDateTime createdAt;
}
