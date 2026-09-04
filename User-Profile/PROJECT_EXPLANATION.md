# User Profile Microservice - Project Explanation

This document provides a detailed explanation of the architecture, workflow, and individual components of the `user-profile-service` project.

---

## 1. Project Overview
This project is a backend microservice built using **Java 21** and **Spring Boot 3.3.3**. It provides a secure API to manage user registrations, logins, and detailed user profiles (including physical attributes, allergies, and dietary preferences).

**Key Technologies:**
- **Spring Web:** For building RESTful APIs.
- **Spring Data JPA & Hibernate:** For ORM and database interactions.
- **MySQL:** The relational database used to store application data.
- **Spring Security & JWT:** For securing the endpoints and authenticating users statelessly.
- **Lombok:** To reduce boilerplate code (Getters, Setters, Builders, etc.).

---

## 2. Architecture Layers
The application follows a standard layered architecture:

1. **Controller Layer (`com.example.userprofile.controller`)**
   - The entry point for HTTP requests. It handles routing, maps request payloads to Data Transfer Objects (DTOs), and delegates business logic to the Service Layer.
2. **Service Layer (`com.example.userprofile.service`)**
   - Contains the core business logic. It processes data, applies rules, and interacts with the Repository Layer.
3. **Repository Layer (`com.example.userprofile.repository`)**
   - Interfaces extending `JpaRepository`. They handle database operations (CRUD) without requiring explicit SQL queries.
4. **Entity Layer (`com.example.userprofile.entity`)**
   - Java classes annotated with `@Entity` that map directly to MySQL database tables.
5. **Security Layer (`com.example.userprofile.security`)**
   - Handles authentication (verifying who the user is) and authorization (what they can access).

---

## 3. Database Entities & Relationships
The data model revolves around the central `User` entity.

- **`User` (Table: `users`)**: Stores core authentication details (`name`, `email`, encrypted `password`).
- **`Profile` (Table: `profiles`)**: Stores physical attributes (`age`, `height`, `weight`, `activityLevel`). It has a **One-to-One** relationship with `User`.
- **`DietaryPreference` (Table: `dietary_preferences`)**: Stores dietary flags (`vegan`, `keto`, `glutenFree`, etc.). It has a **One-to-One** relationship with `User`.
- **`Allergy` (Table: `allergies`)**: Stores specific allergies (e.g., "Peanuts"). It has a **Many-to-One** relationship with `User` (a user can have multiple allergies).

*Note: The relationships are configured with `CascadeType.ALL` and `orphanRemoval = true`, meaning if a user is deleted, all their associated profile data, preferences, and allergies are automatically deleted.*

---

## 4. Security & Authentication Workflow (JWT)
The application uses **JSON Web Tokens (JWT)** to keep API sessions stateless.

### Registration & Login Flow (`AuthController`)
1. **Register**: A user sends their complete details (core authentication, physical profile, dietary preferences, and allergies) in a single request. The `AuthService` handles this as a single **atomic transaction**:
   - Encodes the password using `BCrypt`.
   - Saves the core `User` entity.
   - Populates and saves the associated `Profile` and `DietaryPreference` entities.
   - Creates and links individual `Allergy` entities for the user.
   - Generates a JWT token using `JwtUtils`.
   - Returns a unified `AuthResponse` containing the token and the full user profile.
2. **Login**: A user provides their email and password. Spring Security's `AuthenticationManager` verifies the credentials. If valid, `JwtUtils` generates a JWT token. The service then fetches the full user profile and returns the `AuthResponse` to the client.

### Securing the Endpoints (`SecurityConfig` & `JwtAuthenticationFilter`)
1. **Public vs. Protected**: In `SecurityConfig`, the `/api/users/register` and `/api/users/login` routes are explicitly made public (`permitAll()`). Every other endpoint requires authentication (`authenticated()`).
2. **Stateless Sessions**: Session creation policy is set to `STATELESS`, meaning the server does not remember users between requests.
3. **The Filter**: For every incoming request to a protected endpoint, the `JwtAuthenticationFilter` intercepts it.
   - It checks for the `Authorization: Bearer <token>` header.
   - If present, it validates the token's signature and expiration via `JwtUtils`.
   - It extracts the user's email, loads the user from the database via `UserDetailsServiceImpl`, and manually sets the user as "authenticated" in the `SecurityContextHolder`.

---

## 5. Detailed Component Breakdown

### A. Controllers
- **`AuthController`**: Handles `/register` and `/login`.
- **`UserController`**: Handles profile management. It uses `@AuthenticationPrincipal UserDetails` to automatically grab the currently authenticated user's email from the security context. This ensures a user can only view or modify their own data securely.

### B. Services
- **`AuthService`**: Handles encoding passwords, communicating with the `AuthenticationManager`, generating tokens, and saving new users.
- **`UserService`**: 
  - Retrieves the `User` from the database.
  - Maps data between Entities and DTOs (Data Transfer Objects) to ensure database models aren't directly exposed to the API.
  - Handles updating nested entities like `Profile`, `DietaryPreference`, and `Allergy`.

### C. DTOs (Data Transfer Objects)
Located in the `dto` package, these objects dictate the exact JSON structure the client must send and what they will receive. This provides a clean separation of concerns. For example, `RegisterRequest` contains validation annotations (like `@NotBlank` and `@Email`) to reject bad requests before they ever reach the Service Layer.

### D. Application Configuration (`application.yml`)
- Configures the MySQL connection string, username, and password.
- Sets `hibernate.ddl-auto: update`, which tells Hibernate to automatically create or update the database schema based on the Java Entities.
- Holds the JWT secret key and token expiration time.

---

## 6. How a Request Travels (Example: Add Allergy)
1. **Client** sends `POST /api/users/1/allergies` with a Bearer Token and a JSON body `{"name": "Peanuts"}`.
2. **Filter** (`JwtAuthenticationFilter`) verifies the token and authenticates the user context.
3. **Controller** (`UserController.addAllergy`) receives the request. It validates the JSON body and extracts the currently logged-in user's email.
4. **Service** (`UserService.addAllergy`) fetches the user from the DB using the email. It converts the `AllergyDto` into an `Allergy` entity, ties it to the `User`, and saves it via the `AllergyRepository`.
5. **Repository** executes the underlying SQL `INSERT` statement into the MySQL database.
6. **Controller** returns the newly created Allergy as JSON with an HTTP `201 Created` status.

---

## 7. API Endpoints Overview
All API endpoints are prefixed with `/api/users`. Most endpoints are protected and require a Bearer token generated from the login endpoint.

### Authentication Endpoints (Public)
- `POST /register`: Register a new user account.
- `POST /login`: Authenticate and receive a JWT token.

### User Profile Endpoints (Protected)
- `GET /{userId}`: Fetch the complete profile of a user (including physical attributes, allergies, and dietary preferences).
- `PUT /{userId}`: Update a user's physical profile.

### Allergies Endpoints (Protected)
- `POST /{userId}/allergies`: Add a new allergy to the user's profile.
- `GET /{userId}/allergies`: List all allergies for the user.
- `DELETE /{userId}/allergies/{allergyId}`: Remove a specific allergy.

### Dietary Preferences Endpoints (Protected)
- `PUT /{userId}/preferences`: Update the dietary preferences (e.g., vegan, keto) for the user.

---

## 8. Interactive API Documentation (Swagger)

The project includes `springdoc-openapi` which dynamically generates interactive OpenAPI 3 documentation.

### How it Works
1. **Dependency**: Included `springdoc-openapi-starter-webmvc-ui` in the `pom.xml`.
2. **Configuration**: 
   - `OpenApiConfig.java` defines the global API details and configures the **JWT Bearer Authentication** schema.
   - Controllers and DTOs are decorated with Swagger annotations (`@Operation`, `@Tag`, `@Schema`, etc.) to provide detailed metadata without affecting runtime logic.
3. **Public Access**: `SecurityConfig` is configured to allow public access to `/swagger-ui.html` and `/v3/api-docs`.

To use the interactive dashboard, start the application and navigate to `http://localhost:8080/swagger-ui.html`. You can supply your JWT token directly via the green **Authorize** button to test secured endpoints in real-time.
