# Grocery Planning Service — API Documentation

**Base URL:** `http://localhost:3002`  
**Swagger UI:** `http://localhost:3002/swagger-ui.html`  
**API Docs (JSON):** `http://localhost:3002/v3/api-docs`

---

## Table of Contents

1. [Create Grocery List](#1-create-grocery-list)
2. [Get User's Active Grocery List](#2-get-users-active-grocery-list)
3. [Add Grocery Item](#3-add-grocery-item)
4. [Update Grocery Item](#4-update-grocery-item)
5. [Delete Grocery Item](#5-delete-grocery-item)
6. [Get Estimated Cost](#6-get-estimated-cost)
7. [Error Responses](#7-error-responses)

---

## 1. Create Grocery List

Creates a new **ACTIVE** grocery list for a user.

| Property      | Value                    |
|---------------|--------------------------|
| **Method**    | `POST`                   |
| **URL**       | `/api/grocery-lists`     |
| **Auth**      | None                     |

### Request Headers

```
Content-Type: application/json
```

### Request Body

```json
{
  "userId": 1
}
```

| Field    | Type    | Required | Description                         |
|----------|---------|----------|-------------------------------------|
| `userId` | Long    | ✅ Yes   | The ID of the user owning this list |

### Response — `201 Created`

```json
{
  "id": "66d8f3a2b4e5c1a2d3e4f5a6",
  "userId": 1,
  "status": "ACTIVE",
  "createdAt": "2026-09-04T21:00:00",
  "items": null
}
```

| Field       | Type          | Description                              |
|-------------|---------------|------------------------------------------|
| `id`        | String        | Auto-generated MongoDB ObjectId          |
| `userId`    | Long          | The user who owns this list              |
| `status`    | String        | Always `ACTIVE` on creation              |
| `createdAt` | LocalDateTime | ISO 8601 timestamp of creation           |
| `items`     | null          | Not populated on create — use GET to see items |

### Response — `400 Bad Request`

Returned when `userId` is missing.

```json
{
  "timestamp": "2026-09-04T21:00:00",
  "status": 400,
  "error": "Validation failed",
  "details": {
    "userId": "userId is required"
  }
}
```

---

## 2. Get User's Active Grocery List

Returns the **ACTIVE** grocery list for a user, including all its grocery items.

| Property      | Value                             |
|---------------|-----------------------------------|
| **Method**    | `GET`                             |
| **URL**       | `/api/grocery-lists/{userId}`     |
| **Auth**      | None                              |

### Path Parameters

| Parameter | Type | Required | Description         |
|-----------|------|----------|---------------------|
| `userId`  | Long | ✅ Yes   | ID of the user      |

### Request Body

None.

### Response — `200 OK`

```json
{
  "id": "66d8f3a2b4e5c1a2d3e4f5a6",
  "userId": 1,
  "status": "ACTIVE",
  "createdAt": "2026-09-04T21:00:00",
  "items": [
    {
      "id": "66d8f4b3c5e6d2b3e4f6a7b8",
      "groceryListId": "66d8f3a2b4e5c1a2d3e4f5a6",
      "foodId": "66b1c2d3e4f5a6b7c8d9e0f1",
      "quantity": 2.0,
      "unit": "kg",
      "checked": false,
      "estimatedPrice": 150.00
    },
    {
      "id": "66d8f5c4d6e7f3c4d5e7b8c9",
      "groceryListId": "66d8f3a2b4e5c1a2d3e4f5a6",
      "foodId": "66b2d3e4f5a6c7d8e9f0a1b2",
      "quantity": 0.5,
      "unit": "litre",
      "checked": true,
      "estimatedPrice": null
    }
  ]
}
```

| Field              | Type          | Description                                            |
|--------------------|---------------|--------------------------------------------------------|
| `id`               | String        | Grocery list ID                                        |
| `userId`           | Long          | Owner of the list                                      |
| `status`           | String        | `ACTIVE` or `COMPLETED`                                |
| `createdAt`        | LocalDateTime | When the list was created                              |
| `items`            | Array         | All grocery items in this list                         |
| `items[].id`       | String        | Grocery item ID                                        |
| `items[].groceryListId` | String   | Parent list ID                                         |
| `items[].foodId`   | String        | Reference to a food in the Food Database Service       |
| `items[].quantity` | Double        | How many units are needed                              |
| `items[].unit`     | String        | Unit of measurement (kg, litre, piece, etc.)           |
| `items[].checked`  | Boolean       | Whether the item has been picked up                    |
| `items[].estimatedPrice` | BigDecimal | Optional price per unit                          |

### Response — `404 Not Found`

```json
{
  "timestamp": "2026-09-04T21:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "No active grocery list found for userId: 1"
}
```

---

## 3. Add Grocery Item

Adds a new item to an existing grocery list.

> **Important:** This endpoint calls the **Food Database Service** (`GET /api/foods/{foodId}`) to validate that the food exists. The Food Database Service must be running on port `3000`. Only the `foodId` is stored — food name and nutrition are **NOT** duplicated.

| Property      | Value                                    |
|---------------|------------------------------------------|
| **Method**    | `POST`                                   |
| **URL**       | `/api/grocery-lists/{listId}/items`      |
| **Auth**      | None                                     |

### Path Parameters

| Parameter | Type   | Required | Description              |
|-----------|--------|----------|--------------------------|
| `listId`  | String | ✅ Yes   | ID of the grocery list   |

### Request Headers

```
Content-Type: application/json
```

### Request Body

```json
{
  "foodId": "66b1c2d3e4f5a6b7c8d9e0f1",
  "quantity": 2,
  "unit": "kg",
  "estimatedPrice": 150.00
}
```

| Field            | Type       | Required | Description                                           |
|------------------|------------|----------|-------------------------------------------------------|
| `foodId`         | String     | ✅ Yes   | ID of the food in the Food Database Service           |
| `quantity`       | Double     | ✅ Yes   | Must be a positive number                             |
| `unit`           | String     | ✅ Yes   | e.g. `kg`, `litre`, `piece`, `pack`                  |
| `estimatedPrice` | BigDecimal | ❌ No    | Optional price per unit for cost estimation           |

### Response — `201 Created`

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

| Field            | Type       | Description                                      |
|------------------|------------|--------------------------------------------------|
| `id`             | String     | Auto-generated item ID                           |
| `groceryListId`  | String     | Parent grocery list ID                           |
| `foodId`         | String     | Food reference (only ID stored, not food data)   |
| `quantity`       | Double     | Quantity of the item                             |
| `unit`           | String     | Unit of measurement                              |
| `checked`        | Boolean    | Always `false` on creation                       |
| `estimatedPrice` | BigDecimal | Price per unit (null if not provided)            |

### Response — `400 Bad Request`

```json
{
  "timestamp": "2026-09-04T21:00:00",
  "status": 400,
  "error": "Validation failed",
  "details": {
    "foodId": "foodId is required",
    "quantity": "quantity is required",
    "unit": "unit is required"
  }
}
```

### Response — `404 Not Found`

Returned when the grocery list does not exist, or the food does not exist in the Food Database Service.

```json
{
  "timestamp": "2026-09-04T21:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Food not found in the Food Database Service."
}
```

### Response — `503 Service Unavailable`

Returned when the Food Database Service is not reachable.

```json
{
  "timestamp": "2026-09-04T21:00:00",
  "status": 503,
  "error": "Service Unavailable",
  "message": "Food Database Service is currently unavailable. Please try again later."
}
```

---

## 4. Update Grocery Item

Updates an existing grocery item in a list.

> **Partial update:** Only fields provided in the request body are updated. Any field set to `null` or omitted is left unchanged.

| Property      | Value                                               |
|---------------|-----------------------------------------------------|
| **Method**    | `PUT`                                               |
| **URL**       | `/api/grocery-lists/{listId}/items/{itemId}`        |
| **Auth**      | None                                                |

### Path Parameters

| Parameter | Type   | Required | Description              |
|-----------|--------|----------|--------------------------|
| `listId`  | String | ✅ Yes   | ID of the grocery list   |
| `itemId`  | String | ✅ Yes   | ID of the grocery item   |

### Request Headers

```
Content-Type: application/json
```

### Request Body

All fields are optional. Only send the fields you want to update.

```json
{
  "quantity": 3,
  "unit": "kg",
  "checked": true,
  "estimatedPrice": 200.00
}
```

| Field            | Type       | Required | Description                          |
|------------------|------------|----------|--------------------------------------|
| `quantity`       | Double     | ❌ No    | New quantity (must be positive)      |
| `unit`           | String     | ❌ No    | New unit of measurement              |
| `checked`        | Boolean    | ❌ No    | `true` = picked up, `false` = pending |
| `estimatedPrice` | BigDecimal | ❌ No    | New estimated price per unit         |

### Example: Mark item as checked only

```json
{
  "checked": true
}
```

### Response — `200 OK`

```json
{
  "id": "66d8f4b3c5e6d2b3e4f6a7b8",
  "groceryListId": "66d8f3a2b4e5c1a2d3e4f5a6",
  "foodId": "66b1c2d3e4f5a6b7c8d9e0f1",
  "quantity": 3.0,
  "unit": "kg",
  "checked": true,
  "estimatedPrice": 200.00
}
```

### Response — `404 Not Found`

```json
{
  "timestamp": "2026-09-04T21:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Grocery item not found with id: 66d8f4b3c5e6d2b3e4f6a7b8 in list: 66d8f3a2b4e5c1a2d3e4f5a6"
}
```

---

## 5. Delete Grocery Item

Removes a grocery item from a list permanently.

| Property      | Value                                               |
|---------------|-----------------------------------------------------|
| **Method**    | `DELETE`                                            |
| **URL**       | `/api/grocery-lists/{listId}/items/{itemId}`        |
| **Auth**      | None                                                |

### Path Parameters

| Parameter | Type   | Required | Description              |
|-----------|--------|----------|--------------------------|
| `listId`  | String | ✅ Yes   | ID of the grocery list   |
| `itemId`  | String | ✅ Yes   | ID of the grocery item   |

### Request Body

None.

### Response — `204 No Content`

Empty body. The item was successfully deleted.

### Response — `404 Not Found`

```json
{
  "timestamp": "2026-09-04T21:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Grocery item not found with id: 66d8f4b3c5e6d2b3e4f6a7b8 in list: 66d8f3a2b4e5c1a2d3e4f5a6"
}
```

---

## 6. Get Estimated Cost

Calculates the estimated total cost of all items in a grocery list.

**Formula:**
```
estimatedCost = Σ (estimatedPrice × quantity) for each item
```
Items without an `estimatedPrice` contribute `0` to the total.

| Property      | Value                                            |
|---------------|--------------------------------------------------|
| **Method**    | `GET`                                            |
| **URL**       | `/api/grocery-lists/{listId}/estimated-cost`     |
| **Auth**      | None                                             |

### Path Parameters

| Parameter | Type   | Required | Description            |
|-----------|--------|----------|------------------------|
| `listId`  | String | ✅ Yes   | ID of the grocery list |

### Request Body

None.

### Response — `200 OK`

```json
{
  "listId": "66d8f3a2b4e5c1a2d3e4f5a6",
  "estimatedCost": 450.00
}
```

| Field           | Type       | Description                                         |
|-----------------|------------|-----------------------------------------------------|
| `listId`        | String     | The grocery list ID                                 |
| `estimatedCost` | BigDecimal | Sum of (estimatedPrice × quantity) for all items    |

### Calculation Example

| Item       | estimatedPrice | quantity | Subtotal   |
|------------|----------------|----------|------------|
| Brown Rice | 150.00         | 2 kg     | 300.00     |
| Olive Oil  | 200.00         | 0.5 L    | 100.00     |
| Spinach    | *(none)*       | 1 bunch  | 0.00       |
| **Total**  |                |          | **400.00** |

### Response — `404 Not Found`

```json
{
  "timestamp": "2026-09-04T21:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Grocery list not found with id: 66d8f3a2b4e5c1a2d3e4f5a6"
}
```

---

## 7. Error Responses

All error responses follow this consistent structure:

### Standard Error Body

```json
{
  "timestamp": "2026-09-04T21:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Human-readable error message"
}
```

### Validation Error Body

```json
{
  "timestamp": "2026-09-04T21:00:00",
  "status": 400,
  "error": "Validation failed",
  "details": {
    "fieldName": "validation error message"
  }
}
```

### HTTP Status Code Reference

| Status Code | Meaning                  | When it occurs                                              |
|-------------|--------------------------|-------------------------------------------------------------|
| `200 OK`          | Success              | GET requests succeed                                        |
| `201 Created`     | Resource created     | POST requests succeed                                       |
| `204 No Content`  | Deleted successfully | DELETE requests succeed                                     |
| `400 Bad Request` | Invalid input        | Missing required fields, invalid values                     |
| `404 Not Found`   | Resource missing     | List/item/food not found                                    |
| `503 Service Unavailable` | Downstream down | Food Database Service is not reachable               |
| `500 Internal Server Error` | Unexpected | Unhandled exceptions                                  |

---

## Quick Reference — All Endpoints

| Method     | Endpoint                                          | Description                         |
|------------|---------------------------------------------------|-------------------------------------|
| `POST`     | `/api/grocery-lists`                              | Create a new grocery list           |
| `GET`      | `/api/grocery-lists/{userId}`                     | Get user's active list with items   |
| `POST`     | `/api/grocery-lists/{listId}/items`               | Add an item to a list               |
| `PUT`      | `/api/grocery-lists/{listId}/items/{itemId}`      | Update a grocery item               |
| `DELETE`   | `/api/grocery-lists/{listId}/items/{itemId}`      | Delete a grocery item               |
| `GET`      | `/api/grocery-lists/{listId}/estimated-cost`      | Get estimated total cost            |
