# Meal Planning Microservice — API Documentation

Base URL: `http://localhost:3001`

---

## 1. Create Meal Plan

**`POST http://localhost:3001/api/meal-plans`**

### Request Body
```json
{
  "userId": 1,
  "planDate": "2026-09-03"
}
```

### Response — `201 Created`
```json
{
  "id": "64abc123def456",
  "userId": 1,
  "planDate": "2026-09-03",
  "createdAt": "2026-09-03T10:00:00",
  "meals": []
}
```

---

## 2. Get User's Meal Plan

**`GET http://localhost:3001/api/meal-plans/{userId}`**

### Request
No body. Replace `{userId}` with the user's ID.

**Example:**
```
GET http://localhost:3001/api/meal-plans/1
```

### Response — `200 OK`
```json
{
  "id": "64abc123def456",
  "userId": 1,
  "planDate": "2026-09-03",
  "createdAt": "2026-09-03T10:00:00",
  "meals": [
    {
      "id": "64xyz789",
      "mealPlanId": "64abc123def456",
      "foodId": "123",
      "mealType": "BREAKFAST",
      "quantity": 200.0
    }
  ]
}
```

### Response — `404 Not Found`
```json
{
  "timestamp": "2026-09-03T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Meal plan not found for userId: 1"
}
```

---

## 3. Add Meal

**`POST http://localhost:3001/api/meal-plans/{planId}/meals`**

Replace `{planId}` with the `id` returned from **Create Meal Plan**.

**Example:**
```
POST http://localhost:3001/api/meal-plans/64abc123def456/meals
```

### Request Body
```json
{
  "foodId": "123",
  "mealType": "BREAKFAST",
  "quantity": 200
}
```

> `mealType` must be one of: `BREAKFAST`, `LUNCH`, `DINNER`, `SNACK`  
> `quantity` is in grams  
> `foodId` must exist in the Food Database Service

### Response — `201 Created`
```json
{
  "id": "64xyz789",
  "mealPlanId": "64abc123def456",
  "foodId": "123",
  "mealType": "BREAKFAST",
  "quantity": 200.0
}
```

### Response — `404 Not Found`
```json
{
  "timestamp": "2026-09-03T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Meal plan not found with id: 64abc123def456"
}
```

---

## 4. Get All Meals

**`GET http://localhost:3001/api/meal-plans/{planId}/meals`**

Replace `{planId}` with the meal plan's `id`.

**Example:**
```
GET http://localhost:3001/api/meal-plans/64abc123def456/meals
```

### Request
No body.

### Response — `200 OK`
```json
[
  {
    "id": "64xyz789",
    "mealPlanId": "64abc123def456",
    "foodId": "123",
    "mealType": "BREAKFAST",
    "quantity": 200.0
  },
  {
    "id": "64xyz790",
    "mealPlanId": "64abc123def456",
    "foodId": "456",
    "mealType": "LUNCH",
    "quantity": 150.0
  }
]
```

### Response — `404 Not Found`
```json
{
  "timestamp": "2026-09-03T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Meal plan not found with id: 64abc123def456"
}
```

---

## 5. Delete Meal

**`DELETE http://localhost:3001/api/meal-plans/{planId}/meals/{mealId}`**

Replace `{planId}` with the meal plan's `id` and `{mealId}` with the meal's `id`.

**Example:**
```
DELETE http://localhost:3001/api/meal-plans/64abc123def456/meals/64xyz789
```

### Request
No body.

### Response — `204 No Content`
```
(empty body)
```

### Response — `404 Not Found`
```json
{
  "timestamp": "2026-09-03T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Meal not found with id: 64xyz789"
}
```

---

## 6. Daily Nutrition Summary

**`GET http://localhost:3001/api/meal-plans/{userId}/daily-summary`**

Replace `{userId}` with the user's ID.

**Example:**
```
GET http://localhost:3001/api/meal-plans/1/daily-summary
```

### Request
No body.

### Response — `200 OK`
```json
{
  "date": "2026-09-03",
  "totalCalories": 1850.0,
  "totalProtein": 120.5,
  "totalCarbs": 210.0,
  "totalFat": 55.3
}
```

### Response — `404 Not Found`
```json
{
  "timestamp": "2026-09-03T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Meal plan not found for userId: 1"
}
```

---

## Swagger UI

After starting the service, open:

```
http://localhost:3001/swagger-ui.html
```
