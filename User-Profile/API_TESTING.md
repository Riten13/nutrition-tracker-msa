# API Testing Guide

The base URL for all endpoints is **`http://localhost:8080`**.

Below are the HTTP requests, their bodies, and the expected responses.

---

## 1. Authentication

### Register a User
- **Method**: `POST`
- **Route**: `http://localhost:8080/api/users/register`
- **Request Body**:
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "password123",
  "age": 25,
  "weight": 70.5,
  "height": 175.0,
  "activityLevel": "ACTIVE",
  "vegan": false,
  "vegetarian": false,
  "keto": false,
  "glutenFree": false,
  "dairyFree": false,
  "allergies": [
    "Peanuts",
    "Shellfish"
  ]
}
```
- **Expected Response (201 Created)**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "profile": {
      "age": 25,
      "weight": 70.5,
      "height": 175.0,
      "activityLevel": "ACTIVE"
    },
    "allergies": [
      {
        "id": 1,
        "name": "Peanuts"
      },
      {
        "id": 2,
        "name": "Shellfish"
      }
    ],
    "dietaryPreference": {
      "vegan": false,
      "vegetarian": false,
      "keto": false,
      "glutenFree": false,
      "dairyFree": false
    }
  }
}
```

### Login
- **Method**: `POST`
- **Route**: `http://localhost:8080/api/users/login`
- **Request Body**:
```json
{
  "email": "john.doe@example.com",
  "password": "password123"
}
```
- **Expected Response (200 OK)**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "user": {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "profile": {
      "age": 25,
      "weight": 70.5,
      "height": 175.0,
      "activityLevel": "ACTIVE"
    },
    "allergies": [
      {
        "id": 1,
        "name": "Peanuts"
      },
      {
        "id": 2,
        "name": "Shellfish"
      }
    ],
    "dietaryPreference": {
      "vegan": false,
      "vegetarian": false,
      "keto": false,
      "glutenFree": false,
      "dairyFree": false
    }
  }
}
```
*Note: Copy the `token` from this response. You will need it in the `Authorization` header (`Bearer <token>`) for all subsequent requests.*

---

## 2. User Profile Management
*Remember to replace `{userId}` in the URL with the actual ID (e.g., `1`).*

### Get User Profile
- **Method**: `GET`
- **Route**: `http://localhost:8080/api/users/{userId}`
- **Headers**: `Authorization: Bearer <your_jwt_token>`
- **Expected Response (200 OK)**:
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "profile": {
    "age": 25,
    "weight": 70.5,
    "height": 175.0,
    "activityLevel": "ACTIVE"
  },
  "allergies": [
    {
      "id": 1,
      "name": "Peanuts"
    }
  ],
  "dietaryPreference": {
    "vegan": false,
    "vegetarian": true,
    "keto": false,
    "glutenFree": false,
    "dairyFree": false
  }
}
```

### Update User Profile
- **Method**: `PUT`
- **Route**: `http://localhost:8080/api/users/{userId}`
- **Headers**: `Authorization: Bearer <your_jwt_token>`
- **Request Body**:
```json
{
  "age": 26,
  "weight": 68.0,
  "height": 175.0,
  "activityLevel": "VERY_ACTIVE"
}
```
- **Expected Response (200 OK)**:
```json
{
  "age": 26,
  "weight": 68.0,
  "height": 175.0,
  "activityLevel": "VERY_ACTIVE"
}
```

---

## 3. Allergies Management

### Add an Allergy
- **Method**: `POST`
- **Route**: `http://localhost:8080/api/users/{userId}/allergies`
- **Headers**: `Authorization: Bearer <your_jwt_token>`
- **Request Body**:
```json
{
  "name": "Shellfish"
}
```
- **Expected Response (201 Created)**:
```json
{
  "id": 2,
  "name": "Shellfish"
}
```

### Get All Allergies
- **Method**: `GET`
- **Route**: `http://localhost:8080/api/users/{userId}/allergies`
- **Headers**: `Authorization: Bearer <your_jwt_token>`
- **Expected Response (200 OK)**:
```json
[
  {
    "id": 1,
    "name": "Peanuts"
  },
  {
    "id": 2,
    "name": "Shellfish"
  }
]
```

### Delete an Allergy
- **Method**: `DELETE`
- **Route**: `http://localhost:8080/api/users/{userId}/allergies/{allergyId}`
- **Headers**: `Authorization: Bearer <your_jwt_token>`
- **Expected Response (204 No Content)**: *(No response body)*

---

## 4. Dietary Preferences

### Update Dietary Preferences
- **Method**: `PUT`
- **Route**: `http://localhost:8080/api/users/{userId}/preferences`
- **Headers**: `Authorization: Bearer <your_jwt_token>`
- **Request Body**:
```json
{
  "vegan": false,
  "vegetarian": false,
  "keto": true,
  "glutenFree": true,
  "dairyFree": false
}
```
- **Expected Response (200 OK)**:
```json
{
  "vegan": false,
  "vegetarian": false,
  "keto": true,
  "glutenFree": true,
  "dairyFree": false
}
```
