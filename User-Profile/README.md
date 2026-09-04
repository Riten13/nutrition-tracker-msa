# User Profile Microservice

A clean, beginner-friendly **User Profile Microservice** built with **Java 21**, **Spring Boot 3.3.3**, **Spring Data JPA**, **MySQL**, **Spring Security**, **JWT**, **Maven**, and **Lombok**.

---

## 🛠️ Technology Stack
* **Java 21**
* **Spring Boot 3.3.3**
* **Spring Data JPA (Hibernate)**
* **MySQL** (`user_profile_db`)
* **Spring Security & JJWT (`0.12.6`)**
* **Swagger/OpenAPI 3 (`springdoc-openapi`)**
* **Maven**
* **Lombok**

---

## 📁 Project Structure

```text
src/main/java/com/example/userprofile/
├── UserProfileApplication.java    # Main Entry Point
├── controller/                    # REST API Controllers
│   ├── AuthController.java        # Login & Registration endpoints
│   └── UserController.java        # Profile, Allergy, Preference endpoints
├── service/                       # Business Logic Layer
│   ├── AuthService.java           # Authentication & User registration logic
│   └── UserService.java           # Profile, Allergy, Preference business logic
├── repository/                    # Data Access Layer (Spring Data JPA)
│   ├── UserRepository.java
│   ├── ProfileRepository.java
│   ├── AllergyRepository.java
│   └── DietaryPreferenceRepository.java
├── entity/                        # JPA Entities
│   ├── User.java                  # User core table
│   ├── Profile.java               # One-to-One with User
│   ├── Allergy.java               # One-to-Many with User
│   └── DietaryPreference.java     # One-to-One with User
├── dto/                           # Data Transfer Objects
│   ├── RegisterRequest.java
│   ├── LoginRequest.java
│   ├── AuthResponse.java
│   ├── UserProfileResponse.java
│   ├── ProfileDto.java
│   ├── AllergyDto.java
│   └── DietaryPreferenceDto.java
├── config/                        # OpenAPI & Other Configs
│   └── OpenApiConfig.java         # Swagger OpenAPI 3 configuration
├── security/                      # Spring Security & JWT Configuration
│   ├── SecurityConfig.java        # Security filter chain & password encoder
│   ├── JwtUtils.java              # JWT generation, extraction & validation
│   ├── JwtAuthenticationFilter.java# Requests JWT interceptor
│   └── UserDetailsServiceImpl.java# UserDetails loader
└── exception/                     # Exception Handling
    ├── ResourceNotFoundException.java
    ├── UnauthorizedAccessException.java
    ├── BadRequestException.java
    └── GlobalExceptionHandler.java
```

---

## 🛢️ Database Configuration (`application.yml`)

The application automatically connects to MySQL and creates the `user_profile_db` database if it does not exist:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/user_profile_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    username: root
    password: "1234"
```

---

## 🚀 Running the Application

Ensure MySQL is running locally on port `3306` with user `root` and password `1234`.

Run the application using Maven:

```bash
mvn spring-boot:run
```

Or using Java JAR directly:

```bash
java -jar target/user-profile-service-0.0.1-SNAPSHOT.jar
```

The application will start on **http://localhost:8080**.

---

## 🔒 Security & Data Access Rules

1. **Public APIs**:
   - `POST /api/users/register`
   - `POST /api/users/login`
2. **Secured APIs**: All `/api/users/{userId}/**` endpoints require a Bearer token in the header:
   ```text
   Authorization: Bearer <your_jwt_token>
   ```
3. **Data Isolation**: Users are restricted to accessing and modifying **only their own** profile data. Accessing another user's `{userId}` returns a `403 Forbidden` response.

---

## 📖 API Documentation (Swagger UI)

This project uses `springdoc-openapi` to automatically generate Swagger documentation.

When the application is running, open your browser and go to:
👉 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

You can use the **Authorize** button in Swagger UI to test protected endpoints directly from the browser using a JWT token generated from `/api/users/login`.
