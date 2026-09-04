# 🛒 Grocery Planning Microservice

A **Grocery Planning Microservice** built with **Java 21**, **Spring Boot 3**, **Spring Data MongoDB**, **Spring Cloud OpenFeign**, and **Swagger/OpenAPI 3**.

This service manages grocery lists for users — allowing them to create shopping lists, add food items with quantities and prices, mark items as purchased, and calculate the estimated total cost of a grocery list. It validates all food references against the **Food Database Service** via OpenFeign.

---

## 🛠️ Technology Stack

| Technology | Purpose |
|------------|---------|
| Java 21 | Core language |
| Spring Boot 3.3.2 | Microservice framework |
| Spring Data MongoDB | NoSQL data access |
| MongoDB Atlas | Cloud database (`grocery_planning_db`) |
| Spring Cloud OpenFeign | REST calls to Food Database Service |
| Springdoc OpenAPI 3 (Swagger UI) | Auto-generated API documentation |
| Bean Validation (`@Valid`) | Request body validation |
| Lombok | Boilerplate reduction |
| Maven | Build & dependency management |

---

## 📁 Project Structure

```text
src/main/java/com/example/groceryplanning/
├── GroceryPlanningApplication.java       # Main entry point (@EnableFeignClients)
├── controller/
│   └── GroceryListController.java        # REST endpoints
├── service/
│   └── GroceryListService.java           # Business logic + cost calculation
├── repository/
│   ├── GroceryListRepository.java
│   └── GroceryItemRepository.java
├── model/
│   ├── GroceryList.java                  # MongoDB document
│   └── GroceryItem.java                  # MongoDB document
├── dto/
│   ├── CreateGroceryListRequest.java
│   ├── AddGroceryItemRequest.java
│   ├── UpdateGroceryItemRequest.java
│   ├── GroceryListResponse.java
│   ├── GroceryItemResponse.java
│   └── EstimatedCostResponse.java
├── client/
│   └── FoodServiceClient.java            # OpenFeign client → Food Database
└── exception/
    ├── ResourceNotFoundException.java
    ├── ServiceUnavailableException.java
    └── GlobalExceptionHandler.java
```

---

## 🛢️ Database & Service Configuration (`application.yml`)

```yaml
spring:
  application:
    name: grocery-planning-service
  data:
    mongodb:
      uri: mongodb+srv://<user>:<password>@cluster0.xxx.mongodb.net/
      database: grocery_planning_db

server:
  port: 3002

food-service:
  url: http://localhost:3000

meal-planning-service:
  url: http://localhost:3001
```

> The service runs on **port 3002**. The Food Database Service must be running on **port 3000** for food validation to work.

---

## 🚀 Running the Application

> ⚠️ **Start these services first:**
> 1. Food Database Service (port 3000)
> 2. Meal Planning Service (port 3001) *(if using meal import feature)*
> 3. Then start this service (port 3002)

```bash
mvn spring-boot:run
```

The application starts on **http://localhost:3002**.

---

## 📖 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/grocery-lists` | Create a new grocery list for a user |
| `GET` | `/api/grocery-lists/{userId}` | Get the active grocery list for a user |
| `POST` | `/api/grocery-lists/{listId}/items` | Add a food item to a grocery list |
| `PUT` | `/api/grocery-lists/{listId}/items/{itemId}` | Update an item (quantity, price, checked status) |
| `DELETE` | `/api/grocery-lists/{listId}/items/{itemId}` | Remove an item from a list |
| `GET` | `/api/grocery-lists/{listId}/estimated-cost` | Get the estimated total cost of a list |

---

### Example — Create Grocery List (`POST /api/grocery-lists`)

**Request:**
```json
{
  "userId": 1
}
```

**Response `201 Created`:**
```json
{
  "id": "66d8f3a2b4e5c1a2d3e4f5a6",
  "userId": 1,
  "status": "ACTIVE",
  "createdAt": "2026-09-04T21:00:00",
  "items": null
}
```

---

### Example — Add Grocery Item (`POST /api/grocery-lists/{listId}/items`)

> `foodId` is validated by calling the Food Database Service before storing.

**Request:**
```json
{
  "foodId": "66b1c2d3e4f5a6b7c8d9e0f1",
  "quantity": 2,
  "unit": "kg",
  "estimatedPrice": 150.00
}
```

**Response `201 Created`:**
```json
{
  "id": "66d8f4b3c5e6d2b3e4f6a7b8",
  "groceryListId": "66d8f3a2b4e5c1a2d3e4f5a6",
  "foodId": "66b1c2d3e4f5a6b7c8d9e0f1",
  "quantity": 2.0,
  "unit": "kg",
  "checked": false,
  "estimatedPrice": 150.00
}
```

---

### Example — Update Item (`PUT /api/grocery-lists/{listId}/items/{itemId}`)

Only send the fields you want to change (partial update):

```json
{
  "checked": true
}
```

---

### Example — Get Estimated Cost (`GET /api/grocery-lists/{listId}/estimated-cost`)

**Formula:** `Σ (estimatedPrice × quantity)` for items that have a price set.

**Response `200 OK`:**
```json
{
  "listId": "66d8f3a2b4e5c1a2d3e4f5a6",
  "estimatedCost": 450.00
}
```

---

## 🔗 Inter-Service Communication (OpenFeign)

This service validates food IDs before persisting a grocery item:

```java
@FeignClient(name = "food-service", url = "${food-service.url}")
public interface FoodServiceClient {
    @GetMapping("/api/foods/{foodId}")
    FoodResponse getFoodById(@PathVariable String foodId);
}
```

- If the food does not exist → `404 Not Found`
- If the Food Database Service is unreachable → `503 Service Unavailable`

---

## 📖 Swagger UI

Once running, open:

👉 **http://localhost:3002/swagger-ui.html**

Raw OpenAPI spec: **http://localhost:3002/v3/api-docs**

---

## ❌ Error Responses

All errors follow a consistent structure:

```json
{
  "timestamp": "2026-09-04T21:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "No active grocery list found for userId: 1"
}
```

| Status | When |
|--------|------|
| `400 Bad Request` | Missing or invalid request fields |
| `404 Not Found` | List, item, or food not found |
| `503 Service Unavailable` | Food Database Service is unreachable |
| `500 Internal Server Error` | Unexpected error |
