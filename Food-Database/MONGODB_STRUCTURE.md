# MongoDB Collection Structure

## Connection Details

| Property | Value |
|----------|-------|
| **Host** | MongoDB Atlas (cloud) |
| **Database** | `food_database` |
| **URI** | `mongodb+srv://chatgpt:***@cluster0.ioo4yf3.mongodb.net/food_database` |

---

## Collections Overview

This microservice uses **only ONE collection**.

| Collection | Purpose |
|------------|---------|
| `foods` | Stores all food documents with embedded nutrition |

> Nutrition is stored as an **embedded sub-document** inside each food document.
> There is **no separate nutrition collection**.

---

## Collection: `foods`

### Document Structure

```json
{
  "_id":         "ObjectId (string)",
  "name":        "string",
  "category":    "string",
  "description": "string",
  "nutrition": {
    "servingSize": "number (double)",
    "servingUnit": "string",
    "calories":    "number (double)",
    "protein":     "number (double)",
    "carbs":       "number (double)",
    "fat":         "number (double)",
    "fiber":       "number (double)"
  }
}
```

---

### Field Reference

#### Top-Level Fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `_id` | `String` | Auto | MongoDB document ID — generated automatically |
| `name` | `String` | Yes | Name of the food item |
| `category` | `String` | Yes | Category (e.g. Meat, Grains, Vegetables) |
| `description` | `String` | No | Short description of the food |
| `nutrition` | `Object` | No | Embedded nutrition sub-document |

#### Embedded `nutrition` Fields

| Field | Type | Unit | Description |
|-------|------|------|-------------|
| `servingSize` | `Double` | — | Quantity of one serving |
| `servingUnit` | `String` | — | Unit of measure (e.g. `g`, `ml`, `cup`) |
| `calories` | `Double` | kcal | Energy per serving |
| `protein` | `Double` | grams | Protein per serving |
| `carbs` | `Double` | grams | Carbohydrates per serving |
| `fat` | `Double` | grams | Total fat per serving |
| `fiber` | `Double` | grams | Dietary fiber per serving |

---

### Example Documents

#### Chicken Breast

```json
{
  "_id": "66f1a2b3c4d5e6f7a8b9c0d1",
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

#### Brown Rice

```json
{
  "_id": "66f1a2b3c4d5e6f7a8b9c0d2",
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

#### Banana

```json
{
  "_id": "66f1a2b3c4d5e6f7a8b9c0d3",
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

#### Spinach

```json
{
  "_id": "66f1a2b3c4d5e6f7a8b9c0d4",
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

#### Whole Egg

```json
{
  "_id": "66f1a2b3c4d5e6f7a8b9c0d5",
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

---

## MongoDB Atlas Setup

### How the Collection is Created

Spring Data MongoDB creates the `foods` collection **automatically** on the first
`save()` call. You do NOT need to create it manually in Atlas.

### Steps to Verify in Atlas

1. Go to [https://cloud.mongodb.com](https://cloud.mongodb.com)
2. Click **Browse Collections** on your cluster
3. Select database: `food_database`
4. Select collection: `foods`
5. You should see your food documents listed there

---

## Java ↔ MongoDB Mapping

| Java Class | MongoDB |
|------------|---------|
| `Food` (annotated `@Document`) | One document in `foods` collection |
| `Food.id` (annotated `@Id`) | `_id` field in MongoDB |
| `Food.name` | `name` field |
| `Food.category` | `category` field |
| `Food.description` | `description` field |
| `Nutrition` (plain class, no `@Document`) | Embedded sub-document `nutrition` |
| `Nutrition.servingSize` | `nutrition.servingSize` |
| `Nutrition.calories` | `nutrition.calories` |
| *(and so on for all fields)* | |

---

## Common Categories

These are example categories — the field accepts any string value:

| Category | Example Foods |
|----------|--------------|
| `Meat` | Chicken Breast, Beef, Pork |
| `Seafood` | Salmon, Tuna, Shrimp |
| `Vegetables` | Spinach, Broccoli, Carrot |
| `Fruit` | Banana, Apple, Mango |
| `Grains` | Brown Rice, Oats, Wheat Bread |
| `Dairy & Eggs` | Whole Egg, Milk, Greek Yogurt |
| `Legumes` | Lentils, Black Beans, Chickpeas |
| `Nuts & Seeds` | Almonds, Peanut Butter, Chia Seeds |
| `Protein` | Whey Protein, Tofu, Tempeh |
| `Beverages` | Orange Juice, Coconut Water |
