# Grocery Planning Service — Project Structure & Flow

## Overview

The **Grocery Planning Service** is a Spring Boot microservice that manages users' grocery lists and grocery items. It communicates with the **Food Database Service** (via OpenFeign) to validate food references when items are added.

---

## Project Structure

```
Grocery-Planning/
│
├── pom.xml                                          ← Maven build file
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/groceryplanningservice/
│       │       │
│       │       ├── GroceryPlanningServiceApplication.java   ← Entry point
│       │       │
│       │       ├── controller/
│       │       │   └── GroceryListController.java           ← REST layer
│       │       │
│       │       ├── service/
│       │       │   └── GroceryListService.java              ← Business logic
│       │       │
│       │       ├── repository/
│       │       │   ├── GroceryListRepository.java           ← MongoDB queries
│       │       │   └── GroceryItemRepository.java
│       │       │
│       │       ├── entity/
│       │       │   ├── GroceryList.java                     ← MongoDB document
│       │       │   ├── GroceryItem.java                     ← MongoDB document
│       │       │   └── GroceryListStatus.java               ← Enum: ACTIVE / COMPLETED
│       │       │
│       │       ├── dto/
│       │       │   ├── GroceryListRequest.java              ← Create list input
│       │       │   ├── GroceryListResponse.java             ← List output
│       │       │   ├── GroceryItemRequest.java              ← Add item input
│       │       │   ├── GroceryItemResponse.java             ← Item output
│       │       │   ├── GroceryItemUpdateRequest.java        ← Update item input
│       │       │   └── EstimatedCostResponse.java           ← Cost output
│       │       │
│       │       ├── client/
│       │       │   ├── FoodServiceClient.java               ← OpenFeign interface
│       │       │   └── FoodResponse.java                    ← Minimal food DTO
│       │       │
│       │       ├── exception/
│       │       │   ├── GroceryListNotFoundException.java
│       │       │   ├── GroceryItemNotFoundException.java
│       │       │   └── GlobalExceptionHandler.java          ← @RestControllerAdvice
│       │       │
│       │       └── config/
│       │           └── OpenApiConfig.java                   ← Swagger configuration
│       │
│       └── resources/
│           └── application.yml                              ← App configuration
```

---

## Layer Responsibilities

### `controller/`
- Receives HTTP requests from clients (Postman, frontend, etc.)
- Validates request bodies using Bean Validation (`@Valid`)
- Delegates all logic to the **Service layer**
- Returns HTTP responses with appropriate status codes (`200`, `201`, `204`, `404`, etc.)
- Contains full **Swagger / OpenAPI** annotations for documentation

### `service/`
- Contains all **business logic**
- Orchestrates calls between repositories and external clients
- Maps between entities and DTOs (no raw entities returned to the controller)
- Throws domain exceptions (`GroceryListNotFoundException`, etc.) when data is not found

### `repository/`
- Extends `MongoRepository<Entity, String>` — Spring Data MongoDB
- Provides CRUD operations automatically
- Contains custom query methods:
  - `findByUserIdAndStatus()` — find a user's active list
  - `findByGroceryListId()` — find all items in a list
  - `findByIdAndGroceryListId()` — find a specific item within a specific list

### `entity/`
- Plain Java classes annotated with `@Document(collection = "...")` for MongoDB
- Lombok `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor` used throughout
- **`GroceryList`** maps to the `grocery_lists` collection
- **`GroceryItem`** maps to the `grocery_items` collection

### `dto/`
- **Request DTOs** — accepted from the HTTP request body, include validation annotations
- **Response DTOs** — returned in the HTTP response body, include Swagger annotations
- DTOs act as a contract between the API and the internal model (entity is never exposed directly)

### `client/`
- `FoodServiceClient` — an **OpenFeign** interface that calls the Food Database Service
- `FoodResponse` — a minimal DTO that maps only `id`, `name`, and `category` from the food API response (nutrition is intentionally excluded to avoid data duplication)

### `exception/`
- Domain-specific exceptions extend `RuntimeException`
- `GlobalExceptionHandler` catches all exceptions and returns consistent JSON error responses

### `config/`
- `OpenApiConfig` — configures the Swagger UI title and description

---

## MongoDB Collections

### `grocery_lists`
| Field       | Type          | Description                            |
|-------------|---------------|----------------------------------------|
| `_id`       | String (ObjectId) | Auto-generated MongoDB ID          |
| `userId`    | Long          | ID of the user who owns the list       |
| `status`    | String        | `ACTIVE` or `COMPLETED`                |
| `createdAt` | LocalDateTime | Timestamp when the list was created    |

### `grocery_items`
| Field            | Type          | Description                                      |
|------------------|---------------|--------------------------------------------------|
| `_id`            | String (ObjectId) | Auto-generated MongoDB ID                    |
| `groceryListId`  | String        | References the parent `GroceryList._id`          |
| `foodId`         | String        | References a food in the Food Database Service   |
| `quantity`       | Double        | How many units are needed                        |
| `unit`           | String        | e.g. `kg`, `litre`, `piece`                     |
| `checked`        | Boolean       | Whether the item has been picked up              |
| `estimatedPrice` | BigDecimal    | Optional price per unit for cost estimation      |

---

## Entity Relationship

```
GroceryList  1 ─────────── * GroceryItem
    id ◄────────────── groceryListId
```

- One **GroceryList** can have many **GroceryItems**
- The relationship is maintained via `GroceryItem.groceryListId` (a manual reference — not an embedded document)
- Items are stored in a **separate collection** (`grocery_items`) to allow independent querying

---

## Architecture & Request Flow

```
Client (Postman / Frontend)
          │
          ▼  HTTP Request
  ┌───────────────────┐
  │  GroceryList      │   ← @RestController
  │  Controller       │   ← Validates input, returns HTTP responses
  └────────┬──────────┘
           │
           ▼
  ┌───────────────────┐
  │  GroceryList      │   ← @Service
  │  Service          │   ← Business logic, mapping, orchestration
  └────────┬──────────┘
           │
     ┌─────┴─────┐
     │           │
     ▼           ▼
┌─────────┐  ┌─────────────────┐
│ Grocery │  │ FoodService     │   ← OpenFeign client
│ List /  │  │ Client          │   calls Food Database
│ Item    │  └────────┬────────┘   Service at :3000
│ Repo    │           │
└────┬────┘           ▼
     │       Food Database Service
     │       GET /api/foods/{foodId}
     │
     ▼
  MongoDB Atlas
  grocery_planning_db
```

---

## Request Flow Examples

### Creating a Grocery List
```
POST /api/grocery-lists
    │
    ▼ GroceryListController.createGroceryList()
    │
    ▼ GroceryListService.createGroceryList()
    │   - Builds GroceryList entity
    │   - Sets status = ACTIVE, createdAt = now()
    │
    ▼ GroceryListRepository.save()
    │
    ▼ MongoDB: inserts into grocery_lists
    │
    ▼ Returns GroceryListResponse (201 CREATED)
```

### Adding a Grocery Item
```
POST /api/grocery-lists/{listId}/items
    │
    ▼ GroceryListController.addGroceryItem()
    │
    ▼ GroceryListService.addGroceryItem()
    │   - Verifies list exists (GroceryListRepository.findById)
    │   - Calls FoodServiceClient.getFoodById(foodId)
    │       └─► GET http://localhost:3000/api/foods/{foodId}
    │           • 404 from Food Service → throws FoodNotFound
    │           • Service down → throws ServiceUnavailable
    │   - Stores ONLY the foodId (no food name or nutrition)
    │   - Sets checked = false by default
    │
    ▼ GroceryItemRepository.save()
    │
    ▼ MongoDB: inserts into grocery_items
    │
    ▼ Returns GroceryItemResponse (201 CREATED)
```

### Getting Estimated Cost
```
GET /api/grocery-lists/{listId}/estimated-cost
    │
    ▼ GroceryListService.getEstimatedCost()
    │   - Verifies list exists
    │   - Fetches all items via GroceryItemRepository.findByGroceryListId()
    │   - Calculates: Σ (estimatedPrice × quantity) per item
    │   - Items with null estimatedPrice → contribute 0
    │
    ▼ Returns EstimatedCostResponse (200 OK)
```

---

## Key Design Decisions

| Decision | Reason |
|----------|--------|
| `foodId` stored as a plain `String` reference | Food data ownership stays with Food Database Service — avoids data duplication |
| Items in a separate collection (not embedded) | Allows independent querying and updating of items without loading the full list |
| OpenFeign for HTTP calls | Declarative, clean, and integrates natively with Spring Boot |
| `BigDecimal` for `estimatedPrice` | Avoids floating-point precision issues with monetary values |
| All-or-nothing partial update in PUT | Only non-null fields from the request body are applied to the entity |
| Environment variable for MongoDB URI | Credentials never hardcoded in source code |

---

## Service Ports

| Service                  | Port   |
|--------------------------|--------|
| Food Database Service    | `3000` |
| Meal Planning Service    | `3001` |
| **Grocery Planning Service** | **`3002`** |

---

## Swagger UI

After starting the service:

```
http://localhost:3002/swagger-ui.html
```

API Docs (JSON):

```
http://localhost:3002/v3/api-docs
```
