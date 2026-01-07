# Chaloo - Advanced Car Journey Booking System

**Version:** 1.0.0  
**Status:** Active Development  
**Author:** Mayur & Team  
**License:** Private / Proprietary

---

## 📖 Table of Contents

1.  [Project Overview](#project-overview)
2.  [Strategic Use Cases](#strategic-use-cases)
3.  [Technology Stack & Versions](#technology-stack--versions)
4.  [Software Architecture](#software-architecture)
5.  [Key Design Patterns Used](#key-design-patterns-used)
6.  [Project Directory Structure](#project-directory-structure)
7.  [Deep Dive: Core Modules](#deep-dive-core-modules)
    *   [Authentication & Security](#authentication--security)
    *   [Trip & Journey Management](#trip--journey-management)
    *   [Booking Engine](#booking-engine)
    *   [Payment Processing](#payment-processing)
    *   [Notification Service](#notification-service)
8.  [Database Schema & Relationships](#database-schema--relationships)
9.  [API Documentation (End-to-End)](#api-documentation-end-to-end)
10. [Exception Handling Strategy](#exception-handling-strategy)
11. [Performance Optimization & Caching](#performance-optimization--caching)
12. [Testing Strategy](#testing-strategy)
13. [Installation & Setup Guide](#installation--setup-guide)
14. [Troubleshooting Common Issues](#troubleshooting-common-issues)

---

## 🚀 Project Overview

**Chaloo** is an enterprise-grade backend system designed to facilitate inter-city car journey bookings. It serves as the backbone for a platform connecting travelers with drivers/operators. The system is engineered to handle complex scheduling, real-time availability checks, secure financial transactions, and automated communication.

The core philosophy of Chaloo is **Reliability and Scalability**. We assume that at any given moment, hundreds of users might be searching for trips or confirming bookings; thus, the system uses stateless authentication and optimistic locking mechanisms to ensure data integrity.

### Why this project exists?
In the crowded space of travel aggregators, Chaloo distinguishes itself by focusing on:
1.  **Granular Trip Control**: Operators can manage stops, pick-up points, and dynamic pricing.
2.  **Stateless Scalability**: Designed to run in containerized environments (Docker/Kubernetes).
3.  **Security First**: Implementing industry-standard JWT protocols from day one.

---

## 🎯 Strategic Use Cases

### 1. User Application (B2C)
*   **Search**: Users can search for journeys based on Source, Destination, and Date.
*   **Booking**: Real-time seat locking and booking confirmation.
*   **History**: View past and upcoming trips.
*   **Profile**: Manage personal details and payment methods.

### 2. Admin/Operator Dashboard (B2B)
*   **Fleet Management**: Add and remove vehicles (Cars/Buses).
*   **Trip Scheduling**: Schedule recurring or one-time trips.
*   **Analytics**: View booking density and revenue reports.

---

## 💻 Technology Stack & Versions

We have carefully selected a "Boring Technical Stack" - meaning technologies that are proven, stable, and have massive community support.

| Component | Technology | Version | Justification |
| :--- | :--- | :--- | :--- |
| **Language** | Java | 21 (LTS) | Utilizing the latest LTS for Virtual Threads (Project Loom) and record patterns. |
| **Framework** | Spring Boot | 3.x | Configuring Spring manually is error-prone. Boot provides "convention over configuration". |
| **Database** | MySQL | 8.0 | Reliable ACID compilance. Json column support if needed for dynamic attributes. |
| **ORM** | Spring Data JPA / Hibernate | Latest | Eliminates boilerplate JDBC code. Protects against SQL Injection via Parameter Binding. |
| **Security** | Spring Security + JWT | 6.x | Stateful sessions don't scale. JWTs allow us to be purely stateless. |
| **Build Tool** | Maven | 3.8+ | Dependency management and rigorous lifecycle phases (validate, compile, test, package). |
| **Utility** | Lombok | 1.18.x | Reduces code noise (getters/setters) allowing developers to focus on business logic. |
| **Testing** | JUnit 5 + Mockito | 5.x | Modern testing framework with better parameterized tests and mocking capabilities. |
| **API Docs** | Swagger / OpenAPI | 3.0 | Automated documentation to keep frontend and backend in sync. |
| **Caching** | Spring Cache | 3.x | simple abstraction to plug in providers like Redis or Caffeine later. |

---

## 🏗️ Software Architecture

We follow the strictly layered **Service-Oriented Architecture (SOA)** within a Monolithic deployment unit. 

### Layer 1: The Presentation Layer (Controllers)
*   **Location**: `com.api.controller`
*   **Role**: The "Face" of the application. It speaks HTTP (JSON).
*   **Rule**: NO business logic here. It only accepts DTOs, validates them, calls the Service, and returns `ResponseEntity`.

### Layer 2: The Logic Layer (Services)
*   **Location**: `com.api.service`
*   **Role**: The "Brain". It handles transactions, complex calculations, and orchestration.
*   **Rule**: It never touches the `HttpServletRequest`. It interacts with Repositories and models. Transaction boundaries (`@Transactional`) start here.

### Layer 3: The Data Access Layer (Repositories)
*   **Location**: `com.api.repository`
*   **Role**: The "Librarian". It knows how to talk to the Database.
*   **Rule**: All SQL (HQL/JPQL) lives here. No busines logic allowed.

### Layer 4: The Domain Layer (Models/Entities)
*   **Location**: `com.api.model`
*   **Role**: The "Soul". These classes represent the database tables.

---

## 🎨 Key Design Patterns Used

### 1. Data Transfer Object (DTO) Pattern
*   **Problem**: Exposing your database entities (`@Entity`) to the frontend is dangerous. It exposes schema details and can lead to accidental data modification (Mass Assignment Vulnerability).
*   **Solution**: We map `User` entity to `UserDto`. The Controller receives `dto`, converts to `entity`, saves it, converts back to `dto`, and returns it.
*   **Benefit**: Decoupling. API contract complements DB schema.

### 2. Repository Pattern
*   **Problem**: Writing raw JDBC statements (Connection, PreparedStatement) is verbose and fragile.
*   **Solution**: Interfaces extending `JpaRepository`.
*   **Benefit**: We can swap the underlying generic DAO implementation without changing business logic.

### 3. Dependency Injection (Inversion of Control)
*   **Problem**: If `UserService` creates `new UserRepository()`, it is tightly coupled. You cannot test `UserService` effectively because you can't mock the repository.
*   **Solution**: `@Autowired`. Spring creates the repository bean and injects it into the service.
*   **Benefit**: Testability and loose coupling.

### 4. Strategy Pattern (via Interfaces)
*   **Usage**: In Authentication (e.g., `AuthenticationProvider`). If we want to add OAuth2 (Google Login) later, we implement a new strategy without breaking existing form login.

### 5. Exception Translation Pattern (`@ControllerAdvice`)
*   **Problem**: Java throws generic exceptions (`NullPointerException`, `EntityNotFoundException`). Users need HTTP codes (404, 500).
*   **Solution**: A global handler catches Java exceptions and translates them into a standard `ErrorResponse` JSON.

---

## 📂 Project Directory Structure

Understanding the layout is crucial for contribution.

```text
src/main/java/com/api
├── Chaloo.java                 // Entry point (public static void main)
├── authservice/                // JWT generation and Validation logic
│   ├── JwtService.java         // Token creation, signing, parsing
│   └── UserDetailsImpl.java    // Bridge between our User entity and Spring Security
├── config/                     // Application-wide configurations
│   ├── SecurityConfig.java     // URL protection rules (permitAll vs authenticated)
│   ├── AppConfig.java          // Bean definitions (PasswordEncoder, ModelMapper)
│   └── CacheConfig.java        // Caching strategies
├── controller/                 // REST Endpoints
│   ├── AuthController.java     // Login, Register, Refresh Token
│   ├── TripController.java     // Search, Create, Update Trips
│   ├── BookingController.java  // Book, Cancel, View Bookings
│   └── ...
├── dto/                        // Data Transfer Objects
│   ├── LoginRequest.java
│   ├── TripDto.java
│   └── ...
├── model/                      // Database Entities (JPA)
│   ├── User.java
│   ├── Trip.java
│   ├── Booking.java
│   └── ...
├── repository/                 // Database Access Interfaces
│   ├── UserRepository.java
│   ├── TripRepository.java
│   └── ...
├── service/                    // Business Logic
│   ├── UserService.java
│   ├── BookingService.java     // Complex booking logic (availability check)
│   └── ...
├── customeexceptions/          // Custom Exception classes
│   ├── ResourceNotFoundException.java
│   └── BadRequestException.java
├── exceptionhandler/           // Global Error interception
│   └── GlobalExceptionHandler.java
└── mail/                       // Email sending logic
    └── EmailService.java       // JavaMailSender logic
```

---

## 🔍 Deep Dive: Core Modules

### 🔐 Authentication & Security

We do not use Sessions. We use **JWT (JSON Web Tokens)**.

1.  **Login Flow**:
    *   User sends `username` + `password`.
    *   `AuthenticationManager` verifies credentials against DB.
    *   If valid, `JwtService` creates a signed token (HMAC-SHA256).
    *   Token contains `sub` (username), `iat` (issued at), `exp` (expiration).
    *   Server returns Token.

2.  **Forgot Password Flow**:
    *   User requests a reset link via email.
    *   System generates a unique `PasswordResetToken` (expires in 1 hour).
    *   User resets password using the token.
    *   Passwords are encrypted using `BCryptPasswordEncoder(12)`.

**Why?** This allows the server to scale horizontally. No session state is verified on the server RAM.

### 🚗 Trip & Journey Management

The `Trip` entity is the heart of the system.
*   **Attributes**: Source, Destination, Date, Time, Price, VehicleType, AvailableSeats.
*   **Search Optimization**: We use JPQL queries to efficiently filter trips by Date and Route.
*   **Caching**: `TripService.searchTrips` results are cached (`@Cacheable`). Why? because users search far more often than operators add new trips. Caching reduces database load by ~80%.

### 🎫 Booking Engine

This is the most critical transaction. The `BookingService` must ensure consistency.

**The "Double Booking" Problem**:
*   *Scenario*: Only 1 seat is left. User A and User B click "Book" at the exact same millisecond.
*   *Solution*: Database Locking. We use `@Transactional(isolation = Isolation.SERIALIZABLE)` or Optimistic Locking (`@Version` field) to ensure that only one write wins. The second user receives a strictly typed `SeatUnavailableException`.

**Booking States**:
*   `PENDING`: User clicked book, payment not yet verified.
*   `CONFIRMED`: Payment success, seat permanently deducted.
*   `CANCELLED`: Seat returned to pool.

### 💳 Payment Processing

*   **Current State**: Checks are mocked or integrated with a basic provider.
*   **Concept**: We never store Credit Card numbers (PCI-DSS compliance).
*   **Flow**:
    1.  Frontend sends card token (from Stripe/Razorpay) to Backend.
    2.  Backend talks to Payment Gateway API to verify transaction.
    3.  On success, we update Booking Status.

### 🔔 Notification Service

*   **Tech**: `JavaMailSender` (part of `spring-boot-starter-mail`).
*   **Workflow**:
    *   This is an **Asynchronous** operation (ideally usually `@Async`).
    *   When a booking is confirmed, the main thread returns HTTP 200 OK immediately.
    *   A background thread builds the HTML email template and talks to the SMTP server (Gmail/Outlook) to send the confirmation.
    *   **Why Async?** SMTP is slow (1-3 seconds). We don't want the user to stare at a spinner while waiting for an email.

---

## 🗄️ Database Schema & Relationships

### 1. `users` Table
*   `id` (PK)
*   `email` (Unique)
*   `password` (BCrypted String)
*   `role` (ENUM: USER, ADMIN)

### 2. `trips` Table
*   `id` (PK)
*   `source_city`
*   `destination_city`
*   `journey_date`
*   `price`
*   `total_seats`
*   `available_seats` (Crucial for concurrency)

### 3. `bookings` Table
*   `id` (PK)
*   `user_id` (FK -> users)
*   `trip_id` (FK -> trips)
*   `booking_date`
*   `status` (CONFIRMED, PENDING, CANCELLED)
*   `seat_number`

**Relationships**:
*   One User -> Many Bookings (`@OneToMany`)
*   One Trip -> Many Bookings (`@OneToMany`)

---

## 📡 API Documentation (End-to-End)

*(Authentication Headers required for most endpoints except Auth)*

### Auth Endpoints
*   `POST /api/v1/user/register` : Create new account.
*   `POST /api/v1/user/login` : Get JWT.
*   `POST /api/v1/user/forgot-password` : Request password reset email.
*   `POST /api/v1/user/reset-password` : Reset password using token.

### User Operations
*   `GET /api/users/profile` : Get specific user details.
*   `PUT /api/users/update/{id}` : Update address/phone.

### Trip Operations
*   `GET /api/trips/search?from=Pune&to=Mumbai&date=2024-01-01`
*   `POST /api/trips/create` (Admin Only) : Add new journey.

### Booking Operations
*   `POST /api/bookings/book`
    *   *Body*: `{tripId, passengerCount}`
*   `DELETE /api/bookings/cancel/{id}`

---

## 🛡️ Exception Handling Strategy

We don't let the user see stack traces.

**GlobalExceptionHandler.java**:
*   catches `MethodArgumentNotValidException` -> Returns **400 Bad Request** with list of invalid fields.
*   catches `ResourceNotFoundException` -> Returns **404 Not Found**.
*   catches `BadCredentialsException` -> Returns **401 Unauthorized**.
*   catches `Exception` (catch-all) -> Returns **500 Internal Server Error** (and logs the stack trace for devs).

---

## ⚡ Performance Optimization & Caching

### Where do we use Caching?
We use `spring-boot-starter-cache` with `ConcurrentMapCacheManager` (default) or Redis (production).

**Target Methods**:
*   `getTripById(Long id)`: Trips don't change often.
*   `getAllCities()`: Static data.

**Eviction Policy**:
*   When `updateTrip()` is called, we MUST run `@CacheEvict` to clear the old data. If we don't, users will see old prices or seat counts.

---

## 🧪 Testing Strategy

We believe in the Testing Pyramid.

### 1. Unit Tests (`src/test/java`)
*   **Tools**: JUnit 5, Mockito.
*   **Focus**: Testing specific methods in `UserService` or `BookingService`.
*   **Mocking**: We use `@Mock` for Repositories. We don't hit the database. We assume the database works. We test *our logic*.
    *   *Example*: "If repository returns empty, does service throw NotFoundException?"

### 2. Integration Tests
*   **Tools**: `@SpringBootTest`, `TestRestTemplate`.
*   **Focus**: Testing the whole flow. Controller -> Service -> H2 Database.
*   **Goal**: Ensure wiring is correct and HTTP responses are valid.

---

## 🛠️ Installation & Setup Guide

### Prerequisites
*   Java Development Kit (JDK) 21
*   Maven 3.x
*   MySQL Server running on port 3306

### Step 1: Database Setup
Open your MySQL workbench or terminal and run:
```sql
CREATE DATABASE chaloo_db;
```

### Step 2: Configuration
Navigate to `src/main/resources/application.properties`.
Update these lines:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/chaloo_db
spring.datasource.username=YOUR_ROOT
spring.datasource.password=YOUR_PASSWORD

# Email Config (optional for dev)
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

### Step 3: Run the Application
Open a terminal in the root folder:
```bash
# Windows
./mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

### Step 4: Verify
Open your browser to: `http://localhost:8080/actuator/health`.
You should see `{"status": "UP"}`.

---

## ❓ Troubleshooting Common Issues

### 1. "Access Denied" or 403 Forbidden
*   **Cause**: You forgot the JWT header or the token expired.
*   **Fix**: Login again, copy the new token, and add header `Authorization: Bearer <token>`.

### 2. "Communications link failure" (Database)
*   **Cause**: MySQL is not running or credentials are wrong.
*   **Fix**: Check if MySQL service is started (services.msc on Windows) and verify `application.properties`.

### 3. SMTP Send Failed
*   **Cause**: Google blocks "less secure apps".
*   **Fix**: Use an "App Password" generated from your Google Account Security settings, not your actual password.

### 4. Port 8080 already in use
*   **Cause**: Another instance is running.
*   **Fix**: Change `server.port=8081` in `application.properties` or kill the old process.

---

**Made with ❤️ by the Chaloo Dev Team.**
For internal use and authorized contributors only.
