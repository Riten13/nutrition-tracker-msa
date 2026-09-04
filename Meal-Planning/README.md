# 🗓️ Meal Planning Microservice

A **Meal Planning Microservice** built with **Java 21**, **Spring Boot 3**, **Spring Data MongoDB**, **Spring Cloud OpenFeign**, and **Swagger/OpenAPI 3**.

This service allows users to create daily meal plans, add meals (mapped to foods in the Food Database Service), and retrieve a daily nutrition summary aggregated across all meals.

---

## 🛠️ Technology Stack

| Technology | Purpose |
|------------|---------|
| Java 21 | Core language |
| Spring Boot 3.3.2 | Microservice framework |
| Spring Data MongoDB | NoSQL data access |
| MongoDB Atlas | Cloud database (`food_database`) |
| Spring Cloud OpenFeign | REST calls to Food Database Service |
| Springdoc OpenAPI 3 (Swagger UI) | Auto-generated API documentation |
| Lombok | Boilerplate reduction |
| Maven | Build & dependency management |

---

## 📁 Project Structure

```text
src/main/java/com/example/mealplanning/
├── MealPlanningApplication.java       # Main entry point (@EnableFeignClients)
├── controller/
│   └── MealPlanController.java        # REST endpoints
├── service/
│   └── MealPlanService.java           # Business logic + nutrition aggregation
├── repository/
│   ├── MealPlanRepository.java
│   └── MealRepository.java
├── model/
│   ├── MealPlan.java                  # MongoDB document (per-user, per-date)
│   └── Meal.java                      # Embedded or referenced meal entry
├── dto/
│   ├── MealPlanRequest.java
│   ├── MealRequest.java
│   ├── MealPlanResponse.java
│   ├── MealResponse.java
│   └── DailySummaryResponse.java
├── client/
│   └── FoodServiceClient.java         # OpenFeign client → Food Database Service
└── exception/
    ├── ResourceNotFoundException.java
    └── GlobalExceptionHandler.java
```

---

## 🛢️ Database & Service Configuration (`application.yml`)

```yaml
spring:
  application:
    name: meal-planning
  data:
    mongodb:
      uri: mongodb+srv://<user>:<password>@cluster0.xxx.mongodb.net/food_database
      database: food_database

server:
  port: 3001

food-service:
  url: http://localhost:3000
```

> The service runs on **port 3001**. The Food Database Service must be running on **port 3000** for food validation to work.

---

## 🚀 Running the Application

> ⚠️ **Start the Food Database Service first** (port 3000) before starting this service.

```bash
mvn spring-boot:run
```

The application starts on **http://localhost:3001**.

---

## 📖 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/meal-plans` | Create a new meal plan for a user + date |
| `GET` | `/api/meal-plans/{userId}` | Get the meal plan for a user |
| `POST` | `/api/meal-plans/{planId}/meals` | Add a meal entry to a plan |
| `GET` | `/api/meal-plans/{planId}/meals` | Get all meals in a plan |
| `DELETE` | `/api/meal-plans/{planId}/meals/{mealId}` | Remove a meal from a plan |
| `GET` | `/api/meal-plans/{userId}/daily-summary` | Get aggregated daily nutrition totals |

### Example — Create Meal Plan (`POST /api/meal-plans`)

**Request:**
```json
{
  "userId": 1,
  "planDate": "2026-09-03"
}
```

**Response `201 Created`:**
```json
{
  "id": "64abc123def456",
  "userId": 1,
  "planDate": "2026-09-03",
  "createdAt": "2026-09-03T10:00:00",
  "meals": []
}
```

### Example — Add Meal (`POST /api/meal-plans/{planId}/meals`)

> `foodId` must exist in the Food Database Service. `mealType` must be one of: `BREAKFAST`, `LUNCH`, `DINNER`, `SNACK`.

**Request:**
```json
{
  "foodId": "66f1a2b3c4d5e6f7a8b9c0d1",
  "mealType": "BREAKFAST",
  "quantity": 200
}
```

**Response `201 Created`:**
```json
{
  "id": "64xyz789",
  "mealPlanId": "64abc123def456",
  "foodId": "66f1a2b3c4d5e6f7a8b9c0d1",
  "mealType": "BREAKFAST",
  "quantity": 200.0
}
```

### Example — Daily Nutrition Summary (`GET /api/meal-plans/{userId}/daily-summary`)

**Response `200 OK`:**
```json
{
  "date": "2026-09-03",
  "totalCalories": 1850.0,
  "totalProtein": 120.5,
  "totalCarbs": 210.0,
  "totalFat": 55.3
}
```

> Nutrition totals are calculated by calling the Food Database Service for each meal's `foodId` and scaling by `quantity`.

---

## 🔗 Inter-Service Communication (OpenFeign)

This service uses **Spring Cloud OpenFeign** to call the Food Database Service:

```java
@FeignClient(name = "food-service", url = "${food-service.url}")
public interface FoodServiceClient {
    @GetMapping("/api/foods/{foodId}")
    FoodResponse getFoodById(@PathVariable String foodId);
}
```

- When adding a meal, the food is validated against the Food Database Service.
- If the Food Database Service is unreachable, the endpoint returns **`503 Service Unavailable`**.

---

## 📖 Swagger UI

Once running, open:

👉 **http://localhost:3001/swagger-ui.html**

---

## ❌ Error Responses

```json
{
  "timestamp": "2026-09-03T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Meal plan not found for userId: 1"
}
```

| Status | When |
|--------|------|
| `400 Bad Request` | Invalid or missing fields |
| `404 Not Found` | Meal plan or meal ID not found |
| `503 Service Unavailable` | Food Database Service is down |

---

## 🔗 Used By

- **Grocery Planning Service** (`:3002`) — can reference meal plans when building grocery lists
