# 🥗 Nutrition Tracker — Microservices Architecture (MSA)

> **Phase 2 Project** | Microservices Architecture | Java 21 + Spring Boot 3

A complete **Nutrition Tracking Platform** built as a suite of independently deployable microservices. Each service is self-contained with its own database, REST API, Swagger documentation, and README.

---

## 📦 Microservices Overview

| # | Service | Port | Database | Communication |
|---|---------|------|----------|---------------|
| 1 | [User Profile Service](./User-Profile/) | `8080` | MySQL | JWT Auth |
| 2 | [Food Database Service](./Food-Database/) | `3000` | MongoDB Atlas | — |
| 3 | [Meal Planning Service](./Meal-Planning/) | `3001` | MongoDB Atlas | OpenFeign → Food DB |
| 4 | [Grocery Planning Service](./Grocery-Planning/) | `3002` | MongoDB Atlas | OpenFeign → Food DB + Meal Planning |

---

## 🏗️ Architecture Diagram

```
┌──────────────────────────────────────────────────────────────────────┐
│                     Nutrition Tracker MSA                            │
│                                                                      │
│  ┌─────────────────┐     JWT Auth     ┌───────────────────────────┐  │
│  │  User Profile   │ ◄──────────────► │       Clients             │  │
│  │  Service :8080  │                  │  (REST / Swagger UI)      │  │
│  │  [MySQL]        │                  └───────────────────────────┘  │
│  └─────────────────┘                                                 │
│                                                                      │
│  ┌─────────────────┐                                                 │
│  │  Food Database  │ ◄── Referenced by Meal Planning & Grocery      │
│  │  Service :3000  │                                                 │
│  │  [MongoDB]      │                                                 │
│  └────────┬────────┘                                                 │
│           │  OpenFeign                                               │
│  ┌────────▼────────┐                                                 │
│  │  Meal Planning  │ ◄── Referenced by Grocery Planning             │
│  │  Service :3001  │                                                 │
│  │  [MongoDB]      │                                                 │
│  └────────┬────────┘                                                 │
│           │  OpenFeign                                               │
│  ┌────────▼────────┐                                                 │
│  │ Grocery Planning│                                                 │
│  │  Service :3002  │                                                 │
│  │  [MongoDB]      │                                                 │
│  └─────────────────┘                                                 │
└──────────────────────────────────────────────────────────────────────┘
```

---

## 🛠️ Technology Stack

| Technology | Purpose |
|------------|---------|
| Java 21 | Core language |
| Spring Boot 3.3.x | Microservice framework |
| Spring Data JPA / Hibernate | ORM for User Profile (MySQL) |
| Spring Data MongoDB | NoSQL data access for Food/Meal/Grocery |
| Spring Security + JWT (JJWT 0.12.6) | Authentication & Authorization |
| Spring Cloud OpenFeign | Inter-service REST calls |
| Springdoc OpenAPI 3 (Swagger UI) | API documentation |
| MySQL | Relational DB for user profiles |
| MongoDB Atlas | Cloud NoSQL for food, meal & grocery data |
| Lombok | Boilerplate reduction |
| Maven | Build & dependency management |

---

## 🚀 Running All Services Locally

### Prerequisites

| Requirement | Notes |
|-------------|-------|
| Java 21 | `java -version` should show 21 |
| Maven 3.9+ | `mvn -version` |
| MySQL 8.x | Running on port `3306`, user `root`, password `1234` |
| MongoDB | Atlas URI already configured in each service's `application.yml` |

### Start Order

> ⚠️ **Important:** Start services in this order because downstream services depend on upstream ones.

```bash
# 1. User Profile Service (Port 8080)
cd User-Profile
mvn spring-boot:run

# 2. Food Database Service (Port 3000)
cd Food-Database
mvn spring-boot:run

# 3. Meal Planning Service (Port 3001) — requires Food Database
cd Meal-Planning
mvn spring-boot:run

# 4. Grocery Planning Service (Port 3002) — requires Food Database + Meal Planning
cd Grocery-Planning
mvn spring-boot:run
```

---

## 📖 Swagger UI — Quick Access

Once running, open these URLs in your browser:

| Service | Swagger UI |
|---------|-----------|
| User Profile | http://localhost:8080/swagger-ui.html |
| Food Database | http://localhost:3000/swagger-ui/index.html |
| Meal Planning | http://localhost:3001/swagger-ui.html |
| Grocery Planning | http://localhost:3002/swagger-ui.html |

---

## 📂 Repository Structure

```
MSA/
├── User-Profile/        ← Microservice 1: Auth + User Profile (MySQL + JWT)
│   ├── src/
│   ├── pom.xml
│   └── README.md
├── Food-Database/       ← Microservice 2: Food & Nutrition Master Data (MongoDB)
│   ├── src/
│   ├── pom.xml
│   └── README.md
├── Meal-Planning/       ← Microservice 3: Meal Plans + Daily Nutrition (MongoDB + Feign)
│   ├── src/
│   ├── pom.xml
│   └── README.md
├── Grocery-Planning/    ← Microservice 4: Grocery Lists + Cost Estimation (MongoDB + Feign)
│   ├── src/
│   ├── pom.xml
│   └── README.md
└── README.md            ← This file
```

---

## 🔗 Inter-Service Communication

This project uses **Spring Cloud OpenFeign** for synchronous REST communication between services:

- **Meal Planning** → calls **Food Database** to validate `foodId` before adding a meal
- **Grocery Planning** → calls **Food Database** to validate `foodId` before adding a grocery item
- **Grocery Planning** → calls **Meal Planning** to optionally import meals into a grocery list

---

## 👤 Author

**Riten** | **Sanika** | **Tamanna** | SEM 3 MSA Project

---

## 📄 License

This project is submitted as part of academic coursework. All rights reserved.
