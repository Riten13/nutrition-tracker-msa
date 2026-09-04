# 🍎 Food Database Microservice

A standalone **Food Database Microservice** built with **Java 21**, **Spring Boot 3**, **Spring Data MongoDB**, and **Swagger/OpenAPI 3**. It serves as the **central master data store** for all food and nutrition information within the Nutrition Tracker MSA platform.

Other microservices (Meal Planning, Grocery Planning) call this service via **OpenFeign** to validate food IDs and fetch nutrition data.

---

## 🛠️ Technology Stack

| Technology | Purpose |
|------------|---------|
| Java 21 | Core language |
| Spring Boot 3.3.2 | Microservice framework |
| Spring Data MongoDB | NoSQL data access |
| MongoDB Atlas | Cloud-hosted database (`food_database`) |
| Springdoc OpenAPI 3 (Swagger UI) | Auto-generated API documentation |
| Lombok | Boilerplate reduction |
| Maven | Build & dependency management |

---

## 📁 Project Structure

```text
src/main/java/com/example/fooddatabase/
├── FoodDatabaseApplication.java       # Main entry point
├── controller/
│   └── FoodController.java            # REST endpoints
├── service/
│   └── FoodService.java               # Business logic
├── repository/
│   └── FoodRepository.java            # MongoDB data access
├── model/
│   ├── Food.java                      # MongoDB document
│   └── Nutrition.java                 # Embedded nutrition data
├── dto/
│   ├── FoodRequest.java               # Incoming request body
│   └── FoodResponse.java              # Outgoing response body
└── exception/
    ├── FoodNotFoundException.java
    └── GlobalExceptionHandler.java
```

---

## 🛢️ Database Configuration (`application.yml`)

```yaml
spring:
  data:
    mongodb:
      uri: mongodb+srv://<user>:<password>@cluster0.xxx.mongodb.net/food_database

server:
  port: 3000
```

> The service runs on **port 3000** and connects to a MongoDB Atlas cluster. The database `food_database` is used to store all food documents.

---

## 🚀 Running the Application

Ensure your MongoDB Atlas connection string is set in `src/main/resources/application.yml`, then run:

```bash
mvn spring-boot:run
```

The application will start on **http://localhost:3000**.

---

## 📖 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/foods` | Add a new food item |
| `GET` | `/api/foods` | Get all foods (supports `?name=` and `?category=` filters) |
| `GET` | `/api/foods/{foodId}` | Get a food by ID |
| `GET` | `/api/foods/{foodId}/nutrition` | Get only the nutrition data for a food |
| `PUT` | `/api/foods/{foodId}` | Update a food item |
| `DELETE` | `/api/foods/{foodId}` | Delete a food item |

### Example — Add a Food (`POST /api/foods`)

**Request:**
```json
{
  "name": "Chicken Breast",
  "category": "Meat",
  "description": "Skinless boneless chicken breast",
  "nutrition": {
    "servingSize": 100,
    "servingUnit": "g",
    "calories": 165,
    "protein": 31,
    "carbs": 0,
    "fat": 3.6,
    "fiber": 0
  }
}
```

**Response `201 Created`:**
```json
{
  "id": "66f1a2b3c4d5e6f7a8b9c0d1",
  "name": "Chicken Breast",
  "category": "Meat",
  "description": "Skinless boneless chicken breast",
  "nutrition": {
    "servingSize": 100.0,
    "servingUnit": "g",
    "calories": 165.0,
    "protein": 31.0,
    "carbs": 0.0,
    "fat": 3.6,
    "fiber": 0.0
  }
}
```

### Example — Search by Name (`GET /api/foods?name=chicken`)

Returns all foods whose name contains "chicken" (case-insensitive).

### Example — Get Nutrition (`GET /api/foods/{foodId}/nutrition`)

Returns only the `Nutrition` sub-object for the given food ID.

---

## 📖 Swagger UI

Once the service is running, open:

👉 **http://localhost:3000/swagger-ui/index.html**

You can try all endpoints directly from the browser. See `API_TESTING.md` for detailed step-by-step testing instructions.

---

## ❌ Error Responses

All errors follow a consistent JSON structure:

```json
{
  "timestamp": "2026-09-03T14:42:00.000",
  "status": 404,
  "error": "Not Found",
  "message": "Food not found with id: abc999"
}
```

| Status | When |
|--------|------|
| `400 Bad Request` | Missing or invalid request fields |
| `404 Not Found` | Food ID does not exist |
| `500 Internal Server Error` | Unexpected server error |

---

## 🔗 Used By

- **Meal Planning Service** (`:3001`) — calls `GET /api/foods/{foodId}` via OpenFeign to validate food IDs
- **Grocery Planning Service** (`:3002`) — calls `GET /api/foods/{foodId}` via OpenFeign before adding grocery items
