# Food Database API — Testing Guide

Base URL: `http://localhost:8081`

---

## 1. Add Food

**POST** `/api/foods`

### Request

```
POST http://localhost:8081/api/foods
Content-Type: application/json
```

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

### Response — `201 Created`

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

> Copy the `id` from this response to use in the requests below.

---

## 2. Get All Foods

**GET** `/api/foods`

### Request

```
GET http://localhost:8081/api/foods
```

### Response — `200 OK`

```json
[
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
  },
  {
    "id": "66f1a2b3c4d5e6f7a8b9c0d2",
    "name": "Brown Rice",
    "category": "Grains",
    "description": "Cooked brown rice",
    "nutrition": {
      "servingSize": 100.0,
      "servingUnit": "g",
      "calories": 216.0,
      "protein": 4.5,
      "carbs": 44.0,
      "fat": 1.8,
      "fiber": 3.5
    }
  }
]
```

---

## 3. Search Foods by Name

**GET** `/api/foods?name=chicken`

### Request

```
GET http://localhost:8081/api/foods?name=chicken
```

### Response — `200 OK`

```json
[
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
]
```

---

## 4. Filter Foods by Category

**GET** `/api/foods?category=meat`

### Request

```
GET http://localhost:8081/api/foods?category=meat
```

### Response — `200 OK`

```json
[
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
]
```

---

## 5. Get Food by ID

**GET** `/api/foods/{foodId}`

### Request

```
GET http://localhost:8081/api/foods/66f1a2b3c4d5e6f7a8b9c0d1
```

### Response — `200 OK`

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

### Response — `404 Not Found` (wrong ID)

```json
{
  "timestamp": "2026-09-03T14:42:00.000",
  "status": 404,
  "error": "Not Found",
  "message": "Food not found with id: abc999"
}
```

---

## 6. Get Nutrition by Food ID

**GET** `/api/foods/{foodId}/nutrition`

### Request

```
GET http://localhost:8081/api/foods/66f1a2b3c4d5e6f7a8b9c0d1/nutrition
```

### Response — `200 OK`

```json
{
  "servingSize": 100.0,
  "servingUnit": "g",
  "calories": 165.0,
  "protein": 31.0,
  "carbs": 0.0,
  "fat": 3.6,
  "fiber": 0.0
}
```

---

## 7. Update Food

**PUT** `/api/foods/{foodId}`

### Request

```
PUT http://localhost:8081/api/foods/66f1a2b3c4d5e6f7a8b9c0d1
Content-Type: application/json
```

```json
{
  "name": "Chicken Breast (Grilled)",
  "category": "Protein",
  "description": "Grilled skinless chicken breast",
  "nutrition": {
    "servingSize": 150,
    "servingUnit": "g",
    "calories": 248,
    "protein": 46,
    "carbs": 0,
    "fat": 5.4,
    "fiber": 0
  }
}
```

### Response — `200 OK`

```json
{
  "id": "66f1a2b3c4d5e6f7a8b9c0d1",
  "name": "Chicken Breast (Grilled)",
  "category": "Protein",
  "description": "Grilled skinless chicken breast",
  "nutrition": {
    "servingSize": 150.0,
    "servingUnit": "g",
    "calories": 248.0,
    "protein": 46.0,
    "carbs": 0.0,
    "fat": 5.4,
    "fiber": 0.0
  }
}
```

---

## 8. Delete Food

**DELETE** `/api/foods/{foodId}`

### Request

```
DELETE http://localhost:8081/api/foods/66f1a2b3c4d5e6f7a8b9c0d1
```

### Response — `204 No Content`

```
(empty body)
```

### Response — `404 Not Found` (wrong ID)

```json
{
  "timestamp": "2026-09-03T14:42:00.000",
  "status": 404,
  "error": "Not Found",
  "message": "Food not found with id: abc999"
}
```

---

## Sample Foods to Add

Use these POST requests to populate your database quickly.

### Banana

```json
{
  "name": "Banana",
  "category": "Fruit",
  "description": "Fresh ripe banana",
  "nutrition": {
    "servingSize": 100,
    "servingUnit": "g",
    "calories": 89,
    "protein": 1.1,
    "carbs": 23,
    "fat": 0.3,
    "fiber": 2.6
  }
}
```

### Brown Rice

```json
{
  "name": "Brown Rice",
  "category": "Grains",
  "description": "Cooked brown rice",
  "nutrition": {
    "servingSize": 100,
    "servingUnit": "g",
    "calories": 216,
    "protein": 4.5,
    "carbs": 44,
    "fat": 1.8,
    "fiber": 3.5
  }
}
```

### Whole Egg

```json
{
  "name": "Whole Egg",
  "category": "Dairy & Eggs",
  "description": "Large boiled egg",
  "nutrition": {
    "servingSize": 50,
    "servingUnit": "g",
    "calories": 78,
    "protein": 6,
    "carbs": 0.6,
    "fat": 5,
    "fiber": 0
  }
}
```

### Spinach

```json
{
  "name": "Spinach",
  "category": "Vegetables",
  "description": "Fresh raw spinach leaves",
  "nutrition": {
    "servingSize": 100,
    "servingUnit": "g",
    "calories": 23,
    "protein": 2.9,
    "carbs": 3.6,
    "fat": 0.4,
    "fiber": 2.2
  }
}
```

---

## Swagger UI Testing

Swagger UI is built into the app. No extra tool is needed — just open a browser.

### Open Swagger UI

```
http://localhost:3000/swagger-ui/index.html
```

### Open Raw OpenAPI JSON Spec

```
http://localhost:3000/v3/api-docs
```

---

### How to Test APIs Using Swagger UI

#### Step 1 — Open Swagger UI

Go to `http://localhost:3000/swagger-ui/index.html` in your browser.

You will see the **Food API** section with all 6 endpoints listed:

```
GET    /api/foods                   ← Get / Search Foods
GET    /api/foods/{foodId}          ← Get Food by ID
POST   /api/foods                   ← Add Food
PUT    /api/foods/{foodId}          ← Update Food
DELETE /api/foods/{foodId}          ← Delete Food
GET    /api/foods/{foodId}/nutrition ← Get Nutrition by Food ID
```

---

#### Step 2 — Add a Food (POST)

1. Click **POST /api/foods**
2. Click **"Try it out"** (top right of the endpoint box)
3. In the **Request body** box, paste:

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

4. Click **"Execute"**
5. Scroll down to see the **Response body** — copy the `id` from the response

**Expected Response Code:** `201`

---

#### Step 3 — Get All Foods (GET)

1. Click **GET /api/foods**
2. Click **"Try it out"**
3. Leave `name` and `category` blank to get all foods
4. Click **"Execute"**

**Expected Response Code:** `200`

To search by name:
- Enter `chicken` in the `name` field, then click Execute

To filter by category:
- Enter `meat` in the `category` field, then click Execute

---

#### Step 4 — Get Food by ID (GET)

1. Click **GET /api/foods/{foodId}**
2. Click **"Try it out"**
3. Paste the `id` copied from Step 2 into the `foodId` field
4. Click **"Execute"**

**Expected Response Code:** `200`

If the ID does not exist:

**Expected Response Code:** `404`

---

#### Step 5 — Get Nutrition Only (GET)

1. Click **GET /api/foods/{foodId}/nutrition**
2. Click **"Try it out"**
3. Paste the food `id` into the `foodId` field
4. Click **"Execute"**

**Expected Response Code:** `200`

Response will contain **only the nutrition object**, not the full food.

---

#### Step 6 — Update Food (PUT)

1. Click **PUT /api/foods/{foodId}**
2. Click **"Try it out"**
3. Enter the food `id` in the `foodId` field
4. In the request body, paste:

```json
{
  "name": "Chicken Breast (Grilled)",
  "category": "Protein",
  "description": "Grilled skinless chicken breast",
  "nutrition": {
    "servingSize": 150,
    "servingUnit": "g",
    "calories": 248,
    "protein": 46,
    "carbs": 0,
    "fat": 5.4,
    "fiber": 0
  }
}
```

5. Click **"Execute"**

**Expected Response Code:** `200`

---

#### Step 7 — Delete Food (DELETE)

1. Click **DELETE /api/foods/{foodId}**
2. Click **"Try it out"**
3. Enter the food `id` in the `foodId` field
4. Click **"Execute"**

**Expected Response Code:** `204` (empty body — this is correct)

If the food does not exist:

**Expected Response Code:** `404`

---

### Swagger UI Quick Reference

| Action | Where to click |
|--------|---------------|
| Expand an endpoint | Click on the endpoint row |
| Enable editing | Click **"Try it out"** |
| Send the request | Click **"Execute"** |
| See the response | Scroll down to **"Server response"** |
| Collapse an endpoint | Click the endpoint row again |
| View all schemas | Scroll to the bottom — **"Schemas"** section |
