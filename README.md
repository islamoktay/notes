# Notes Management API

A professional Spring Boot REST API for managing users and their personal notes. This project demonstrates modern Java development best practices, including layered architecture, data validation, and performance optimization.

## 🚀 Tech Stack

- **Java 26** (OpenJDK)
- **Spring Boot 4.0.6**
- **Spring Data JPA** (Hibernate)
- **PostgreSQL**
- **Lombok**
- **Gradle**

## 🏗 Architecture

The project follows a **Layered Architecture** pattern to ensure separation of concerns and maintainability:

1.  **Presentation Layer (Controllers):** Handles REST endpoints and HTTP status codes.
2.  **Service Layer (Business Logic):** Manages business rules and database transactions.
3.  **Data Access Layer (Repositories):** Interface-driven JPA repositories for database interaction.
4.  **Mapper Layer:** Dedicated components for converting database Entities to API DTOs.
5.  **Domain Layer (Entities):** JPA entities representing the database schema.

## 🛠 Key Features & Patterns

- **N+1 Query Prevention:** Optimized user fetching using `JOIN FETCH` to reduce database round-trips.
- **Transactional Integrity:** Service methods are annotated with `@Transactional` to ensure data consistency.
- **Robust Validation:** Request payloads are validated using Jakarta Validation (e.g., `@NotBlank`, `@NotNull`).
- **Global Exception Handling:** Centralized `@ControllerAdvice` for consistent API error responses.
- **DTO Pattern:** Decouples API contracts from internal database structure using Java `record` classes.

## 🚦 Getting Started

### Prerequisites

- JDK 26
- PostgreSQL (Running locally on port 5432)
- A database named `notes`

### Running the Application

1. Clone the repository.
2. Configure your database credentials in `src/main/resources/application.yaml`.
3. Run the application using Gradle:
   ```bash
   ./gradlew bootRun
   ```

## 📖 API Endpoints

### Users
- `GET /api/v1/users` - Get all users (and their notes).
- `POST /api/v1/users` - Create a new user.

### Notes
- `GET /api/v1/notes` - Get all notes.
- `GET /api/v1/notes/user/{userId}` - Get all notes for a specific user.
- `POST /api/v1/notes` - Create a new note for a user.

---
*Created as a learning project for modern Spring Boot development.*
