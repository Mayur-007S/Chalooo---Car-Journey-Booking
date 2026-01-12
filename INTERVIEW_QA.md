# Interview Questions & Answers - Chaloo Project

> **Project**: Chaloo - Car Journey Booking System  
> **Tech Stack**: Spring Boot, JWT, MySQL, JPA, Email Service  
> **Purpose**: Common interview questions with simple step-by-step explanations

---

## Table of Contents

1. [Project Overview Questions](#project-overview-questions)
2. [Technology Stack Questions](#technology-stack-questions)
3. [JWT & Security Questions](#jwt--security-questions)
4. [Database & JPA Questions](#database--jpa-questions)
5. [Architecture & Design Patterns](#architecture--design-patterns)
6. [Booking Logic & Concurrency](#booking-logic--concurrency)
7. [Email & Notification Questions](#email--notification-questions)
8. [Caching & Performance](#caching--performance)
9. [Exception Handling](#exception-handling)
10. [Testing & Deployment](#testing--deployment)

---

## Project Overview Questions

### Q1: Can you explain what the Chaloo project is about?

**Answer:**

Chaloo is a car journey booking system, similar to online bus booking platforms.

**Step-by-step:**

1. **Purpose**: Users can book seats in cars/buses for inter-city travel
2. **Key Features**:
   - Users can search for trips by source, destination, and date
   - Users can book seats and make payments
   - Admins can create and manage trips
   - Automated email notifications
3. **Technology**: Built using Spring Boot as a REST API backend
4. **Database**: MySQL stores all user, trip, and booking information

---

### Q2: What problem does this project solve?

**Answer:**

It solves the problem of coordinating shared car journeys between cities.

**Step-by-step:**

1. **Problem**: People need to travel between cities but don't have direct transport
2. **Solution**: Operators can list available car/bus journeys
3. **Benefit for users**: They can search, compare, and book seats online
4. **Benefit for operators**: They can manage bookings and track revenue
5. **Automated process**: Email confirmations and payment tracking reduce manual work

---

## Technology Stack Questions

### Q3: Why did you choose Spring Boot for this project?

**Answer:**

Spring Boot simplifies Java application development significantly.

**Step-by-step:**

1. **Convention over Configuration**: No need to manually configure servlets or XML files
2. **Built-in Features**: Security, database connection, email service - all ready to use
3. **Dependency Management**: All compatible libraries work together automatically
4. **Production Ready**: Built-in health checks and monitoring through Actuator
5. **Industry Standard**: Most companies use Spring Boot, making it easier to collaborate

---

### Q4: What is the difference between Spring and Spring Boot?

**Answer:**

Spring Boot is Spring with automatic configuration.

**Step-by-step:**

1. **Spring Framework**:
   - Requires manual XML or Java configuration
   - You configure every bean, database connection, security rule
   - More control but more setup time
   
2. **Spring Boot**:
   - Auto-configuration based on dependencies in your project
   - Embedded server (Tomcat) - no need to deploy WAR files
   - Just add dependencies, and it automatically configures them
   
3. **Example**: If you add `spring-boot-starter-mail`, it automatically:
   - Configures the email sender
   - Reads properties from `application.properties`
   - Makes it ready to use with just `@Autowired`

---

### Q5: Why use MySQL instead of MongoDB?

**Answer:**

MySQL is better for structured, relational data with strict rules.

**Step-by-step:**

1. **Relationships**: Our data has clear relationships:
   - One user has many bookings
   - One trip has many bookings
   
2. **ACID Guarantees**: We need strong consistency:
   - When two people book the last seat, only one should succeed
   - MySQL ensures this with transactions
   
3. **SQL Queries**: We need complex queries:
   - Search trips by source, destination, and date
   - Join user data with booking data
   
4. **Data Integrity**: MySQL enforces foreign keys and constraints
   - Can't book a trip that doesn't exist
   - Can't delete a user with active bookings

---

## JWT & Security Questions

### Q6: How does JWT authentication work in your project?

**Answer:**

JWT allows users to prove their identity without storing session data on the server.

**Step-by-step:**

1. **User Login**:
   - User sends username and password
   - Server verifies credentials against database
   - If valid, server creates a JWT token
   
2. **Token Creation**:
   - Token contains: username, issued time, expiry time (6 months)
   - Token is digitally signed with a secret key
   - Token is sent back to the user
   
3. **Using the Token**:
   - User stores token in browser/app
   - Every request includes: `Authorization: Bearer <token>`
   
4. **Token Validation**:
   - JWT Filter intercepts every request
   - Extracts token and verifies signature
   - If valid, user is authenticated
   - If invalid or expired, request is rejected

---

### Q7: Why use JWT instead of sessions?

**Answer:**

JWT is stateless, making the application scalable.

**Step-by-step:**

1. **Session-Based Authentication** (Traditional):
   - Server stores logged-in users in memory
   - Each server has its own session storage
   - Problem: If you have 10 servers, sessions don't sync
   
2. **JWT-Based Authentication**:
   - No server-side session storage
   - Token carries all needed information
   - Any server can validate the token
   
3. **Scalability**:
   - Easy to add more servers
   - Load balancer can send requests to any server
   - Each server independently validates tokens
   
4. **Mobile Friendly**:
   - Mobile apps can store tokens locally
   - Works perfectly with REST APIs

---

### Q8: How do you secure passwords in the database?

**Answer:**

Passwords are encrypted using BCrypt before storing.

**Step-by-step:**

1. **User Registration**:
   - User provides password (e.g., "MyPass123")
   - Server encrypts using BCrypt with strength 12
   - Encrypted password saved: "$2a$12$XYZ..." (looks random)
   
2. **Why BCrypt?**:
   - One-way encryption - cannot be reversed
   - Built-in salt - even same passwords look different
   - Slow by design - makes brute force attacks difficult
   
3. **User Login**:
   - User sends password "MyPass123"
   - Server encrypts the input and compares with stored hash
   - If they match, login successful
   
4. **Security Benefit**:
   - Even if database is hacked, passwords are safe
   - Hackers cannot reverse-engineer the original password

---

### Q9: What happens if JWT token is expired?

**Answer:**

Server rejects the request and user must login again.

**Step-by-step:**

1. **Token Expiry**: Tokens are valid for 6 months from creation

2. **When User Tries to Access**:
   - JWT Filter extracts the token
   - Checks expiry time from token payload
   - Compares with current time
   
3. **If Expired**:
   - Token validation fails
   - Request is rejected with 401 Unauthorized
   - User sees "Session expired" message
   
4. **User Action**:
   - User must login again
   - New token is generated
   - User can continue using the app

---

## Database & JPA Questions

### Q10: What is JPA and why use it?

**Answer:**

JPA (Java Persistence API) converts Java objects to database tables automatically.

**Step-by-step:**

1. **Without JPA**:
   - Write SQL queries manually: "INSERT INTO users..."
   - Handle database connections and closing
   - Parse results manually into Java objects
   
2. **With JPA**:
   - Create a Java class with `@Entity`
   - JPA automatically creates the table
   - Save objects: `repository.save(user)` - no SQL needed
   
3. **Benefits**:
   - Less code to write and maintain
   - Database-independent (works with MySQL, PostgreSQL, etc.)
   - Protection against SQL injection
   
4. **Example**:
   ```
   Instead of: "INSERT INTO users (name, email) VALUES (?, ?)"
   Just do: userRepository.save(newUser)
   ```

---

### Q11: Explain the relationships in your database.

**Answer:**

The database has three main entities with relationships.

**Step-by-step:**

1. **User Entity**:
   - Stores user information and login credentials
   - One user can have multiple bookings
   - Relationship: `@OneToMany` with Booking
   
2. **Trip Entity**:
   - Stores journey details (source, destination, date, price)
   - One trip can have multiple bookings
   - Relationship: `@OneToMany` with Booking
   
3. **Booking Entity**:
   - Links users to trips
   - Each booking belongs to one user
   - Each booking belongs to one trip
   - Relationship: `@ManyToOne` with both User and Trip
   
4. **Foreign Keys**:
   - Booking table has `user_id` pointing to User
   - Booking table has `trip_id` pointing to Trip

---

### Q12: What is the difference between @OneToMany and @ManyToOne?

**Answer:**

They define opposite sides of the same relationship.

**Step-by-step:**

1. **Real-World Example**:
   - One user can have many bookings
   - Many bookings belong to one user
   
2. **In User Entity**:
   - `@OneToMany`: One user → Many bookings
   - User class has a list of bookings
   
3. **In Booking Entity**:
   - `@ManyToOne`: Many bookings → One user
   - Booking class has a reference to one user
   
4. **Database**:
   - Foreign key is stored in the "Many" side (Booking table)
   - `bookings` table has `user_id` column

---

## Architecture & Design Patterns

### Q13: Explain the layered architecture of your project.

**Answer:**

The project follows a 3-layer architecture for separation of concerns.

**Step-by-step:**

1. **Controller Layer** (`@RestController`):
   - Handles HTTP requests and responses
   - Validates input data
   - Calls service layer
   - Returns JSON responses
   - Example: `UserController.java`
   
2. **Service Layer** (`@Service`):
   - Contains business logic
   - Handles transactions
   - Coordinates between multiple repositories
   - Example: Checking seat availability before booking
   
3. **Repository Layer** (`@Repository`):
   - Interacts with database
   - Executes queries
   - No business logic
   - Example: `userRepository.findByUsername()`
   
4. **Why This Architecture?**:
   - Clear separation: Each layer has one responsibility
   - Testable: Can test service logic without database
   - Maintainable: Changes in one layer don't affect others

---

### Q14: What is the DTO pattern and why use it?

**Answer:**

DTO (Data Transfer Object) protects internal data and controls what data is exposed.

**Step-by-step:**

1. **Problem**:
   - Entity classes have all database fields
   - Some fields are sensitive (password, internal IDs)
   - Don't want to expose everything to frontend
   
2. **Solution - DTO**:
   - Create separate classes for data transfer
   - Include only fields needed for that specific operation
   
3. **Example**:
   - User Entity: id, username, password, email, role, created_date
   - LoginUserDTO: only username and password
   - UserResponseDTO: id, username, email (no password!)
   
4. **Benefits**:
   - Security: Password never sent to frontend
   - Flexibility: Different DTOs for different use cases
   - API Versioning: Can change DTO without changing entity

---

### Q15: What design patterns are used in this project?

**Answer:**

Several design patterns make the code maintainable and scalable.

**Step-by-step:**

1. **Dependency Injection Pattern**:
   - Spring automatically creates and injects dependencies
   - Using `@Autowired`
   - Benefit: Loose coupling, easy testing
   
2. **Repository Pattern**:
   - Abstracts database operations
   - Using `JpaRepository` interface
   - Benefit: Can change database without changing service code
   
3. **DTO Pattern**:
   - Separate objects for data transfer
   - Benefit: Security and API flexibility
   
4. **MVC Pattern**:
   - Model (Entity), View (JSON response), Controller
   - Benefit: Clear separation of concerns
   
5. **Exception Translation Pattern**:
   - `@ControllerAdvice` catches exceptions globally
   - Converts Java exceptions to HTTP error codes
   - Benefit: Consistent error responses

---

## Booking Logic & Concurrency

### Q16: How do you prevent double booking when only 1 seat is available?

**Answer:**

Using database transactions and locking mechanisms.

**Step-by-step:**

1. **The Problem**:
   - Trip has 1 seat available
   - User A and User B try to book at the same time
   - Both check: "1 seat available" ✓
   - Both try to book - overbooking!
   
2. **Solution - Transactional Locking**:
   - Mark the method with `@Transactional`
   - Use database row-level locking
   
3. **How It Works**:
   - User A's request locks the Trip row
   - User B's request waits
   - User A's booking completes: seats = 0
   - Database commits and releases lock
   - User B's request reads: seats = 0
   - User B gets "No seats available" error
   
4. **Code-Level Protection**:
   - Check available seats inside transaction
   - Decrement seats only if available
   - Save booking and updated trip in one transaction

---

### Q17: What is @Transactional and why is it important?

**Answer:**

@Transactional ensures all database operations succeed or fail together.

**Step-by-step:**

1. **What is a Transaction?**:
   - A group of database operations treated as one unit
   - All must succeed, or all must rollback
   
2. **Example Scenario**:
   - Creating a booking involves:
     - Step 1: Reduce available seats in Trip
     - Step 2: Create Booking record
     - Step 3: Create Payment record
   
3. **Without @Transactional**:
   - Step 1 succeeds
   - Step 2 fails (database error)
   - Result: Seat is deducted but no booking created!
   
4. **With @Transactional**:
   - All steps execute
   - If any step fails, everything rolls back
   - Database returns to original state
   - Ensures data consistency

---

### Q18: How do you handle booking cancellations?

**Answer:**

Cancellation returns the seat to available seats and updates booking status.

**Step-by-step:**

1. **User Requests Cancellation**:
   - User sends booking ID to cancel
   - System validates: Is this user's booking?
   
2. **Database Operations**:
   - Find the booking by ID
   - Find the associated trip
   - Increment available seats in trip
   - Update booking status to CANCELLED
   
3. **Transaction**:
   - All operations wrapped in `@Transactional`
   - If any operation fails, everything rolls back
   
4. **Notifications**:
   - Send cancellation email to user
   - Update payment status (if refund applicable)

---

## Email & Notification Questions

### Q19: How does the email notification system work?

**Answer:**

Using Spring's JavaMailSender to send automated emails.

**Step-by-step:**

1. **Configuration**:
   - Email credentials stored in `.env` file
   - SMTP server configured (Gmail/Outlook)
   - Mail service reads these properties
   
2. **When Emails Are Sent**:
   - User registers → Welcome email
   - User logs in → Login notification
   - Booking confirmed → Booking confirmation with details
   - Password reset → Reset link email
   
3. **Email Service**:
   - Takes: recipient email, subject, message
   - Builds email template (can be HTML)
   - Connects to SMTP server
   - Sends email
   
4. **Error Handling**:
   - If email fails, log the error
   - Don't stop the main operation (booking still succeeds)
   - Ideally run email sending asynchronously

---

### Q20: Why send emails asynchronously?

**Answer:**

Email sending is slow; we don't want users to wait.

**Step-by-step:**

1. **Problem with Synchronous Sending**:
   - User clicks "Book Now"
   - Server processes booking (fast, < 100ms)
   - Server sends email (slow, 2-3 seconds)
   - User waits 3 seconds for response
   
2. **Solution - Asynchronous**:
   - User clicks "Book Now"
   - Server processes booking
   - Server returns response immediately: "Booking Confirmed!"
   - Background thread sends email
   
3. **Implementation**:
   - Mark email method with `@Async`
   - Spring manages thread pool
   - Email sending happens in separate thread
   
4. **User Experience**:
   - Better: User gets instant feedback
   - Email arrives a few seconds later
   - No waiting, smoother experience

---

## Caching & Performance

### Q21: Where do you use caching in your project?

**Answer:**

Caching is used for frequently accessed, rarely changed data.

**Step-by-step:**

1. **What is Caching?**:
   - Storing data in memory for quick access
   - Avoids repeated database queries
   
2. **Use Cases in Chaloo**:
   - Trip search results
   - User profile data
   - List of cities (static data)
   
3. **Example - getTripById**:
   - First call: Fetch from database, store in cache
   - Next calls: Return from cache (much faster)
   - Cache cleared: When trip is updated or deleted
   
4. **Benefits**:
   - Faster response times (80% improvement)
   - Reduced database load
   - Better scalability

---

### Q22: What happens if cached data becomes stale?

**Answer:**

Cache eviction ensures users always see updated data.

**Step-by-step:**

1. **The Problem**:
   - Trip price: $100 (cached)
   - Admin updates price to $80
   - Users still see $100 from cache
   
2. **Solution - Cache Eviction**:
   - Use `@CacheEvict` on update methods
   - When trip is updated, clear the cache
   
3. **How It Works**:
   - User calls: `updateTrip(tripId, newPrice)`
   - Method marked with `@CacheEvict(value="trips", key="#tripId")`
   - Cache entry for that trip is removed
   - Next request fetches fresh data from database
   
4. **Cache Strategies**:
   - Manual eviction: On update/delete
   - Time-based: Expire after X minutes
   - Size-based: Remove old entries when cache is full

---

## Exception Handling

### Q23: How do you handle errors in your application?

**Answer:**

Using global exception handling with @ControllerAdvice.

**Step-by-step:**

1. **Problem**:
   - Different controllers throw different exceptions
   - Need consistent error responses
   - Don't want to expose stack traces to users
   
2. **Solution - GlobalExceptionHandler**:
   - Single class marked with `@ControllerAdvice`
   - Catches exceptions from all controllers
   
3. **Exception Types**:
   - `ResourceNotFoundException` → 404 Not Found
   - `BadCredentialsException` → 401 Unauthorized
   - `ValidationException` → 400 Bad Request
   - `Exception` (generic) → 500 Internal Server Error
   
4. **Response Format**:
   - Consistent JSON: `{error: "message", status: 404, timestamp: "..."}`
   - User-friendly messages
   - Stack trace logged for developers only

---

### Q24: Give an example of a custom exception in your project.

**Answer:**

ResourceNotFoundException when a requested entity doesn't exist.

**Step-by-step:**

1. **Scenario**:
   - User requests: GET /api/trips/999
   - Trip with ID 999 doesn't exist
   
2. **Service Layer**:
   - `findById(999)` returns empty
   - Service throws: `throw new ResourceNotFoundException("Trip not found")`
   
3. **Exception Handler Catches It**:
   - `@ExceptionHandler(ResourceNotFoundException.class)`
   - Returns: 404 with message "Trip not found"
   
4. **Benefits**:
   - Clear, specific exception names
   - Better error tracking
   - User gets meaningful message, not generic error

---

## Testing & Deployment

### Q25: What types of tests have you written for this project?

**Answer:**

Unit tests for service logic and integration tests for API endpoints.

**Step-by-step:**

1. **Unit Tests**:
   - Test individual methods in isolation
   - Mock dependencies (repositories)
   - Example: Test if BookingService throws error when no seats available
   - Tools: JUnit 5, Mockito
   
2. **Integration Tests**:
   - Test entire flow: Controller → Service → Database
   - Use H2 in-memory database for testing
   - Example: POST /api/bookings/book and verify database entry
   - Tools: SpringBootTest, TestRestTemplate
   
3. **Why Both?**:
   - Unit tests: Fast, test logic
   - Integration tests: Ensure components work together
   
4. **Coverage Goal**:
   - Critical paths: 80%+ coverage
   - Service layer: High priority for testing

---

### Q26: How would you deploy this application to production?

**Answer:**

Using JAR file deployment on a cloud server.

**Step-by-step:**

1. **Build the Application**:
   - Run: `mvn clean package`
   - Creates executable JAR file in `target/` folder
   
2. **Server Setup**:
   - Cloud server (AWS EC2, DigitalOcean, etc.)
   - Install Java 21
   - Install MySQL and create database
   
3. **Configure Environment**:
   - Upload `.env` file with production credentials
   - Set database URL, email credentials
   - Configure server port
   
4. **Run Application**:
   - Upload JAR file to server
   - Run: `java -jar chaloo.jar`
   - Application starts on configured port
   
5. **Additional Steps**:
   - Use Nginx as reverse proxy
   - Configure SSL/HTTPS
   - Set up automatic restart with systemd
   - Configure logging and monitoring

---

### Q27: What is the purpose of application.properties?

**Answer:**

Central configuration file for all application settings.

**Step-by-step:**

1. **Database Configuration**:
   - Connection URL, username, password
   - `spring.datasource.url=jdbc:mysql://localhost:3306/chaloo_db`
   
2. **JPA Configuration**:
   - Auto-create tables: `spring.jpa.hibernate.ddl-auto=update`
   - Show SQL queries: `spring.jpa.show-sql=true`
   
3. **Server Configuration**:
   - Port: `server.port=8080`
   - Context path: `server.servlet.context-path=/api`
   
4. **Email Configuration**:
   - SMTP host, port, username, password
   
5. **Environment-Specific**:
   - Different files for dev, test, production
   - `application-dev.properties`, `application-prod.properties`

---

## Advanced & Scenario-Based Questions

### Q28: Two users try to book the last seat at exactly the same time. Walk me through what happens.

**Answer:**

Database locking ensures only one booking succeeds.

**Step-by-step:**

1. **Initial State**:
   - Trip ID 101 has 1 seat available
   - User A and User B both click "Book" simultaneously
   
2. **Request Processing**:
   - Both requests hit the server
   - Both enter the `BookingService.createBooking()` method
   
3. **Database Transaction**:
   - User A's transaction starts first (by microseconds)
   - User A's transaction locks Trip row 101
   - User A checks: available_seats = 1 ✓
   - User A creates booking, updates seats to 0
   - User A's transaction commits and releases lock
   
4. **User B's Transaction**:
   - Waits for lock to be released
   - Acquires lock, reads Trip 101
   - Checks: available_seats = 0 ✗
   - Throws: `SeatUnavailableException`
   - Returns 400 Bad Request: "No seats available"
   
5. **Result**:
   - User A: Booking successful
   - User B: Error message, must search for another trip

---

### Q29: How would you optimize database queries for searching trips?

**Answer:**

Using indexing and query optimization techniques.

**Step-by-step:**

1. **Add Database Indexes**:
   - Index on `source_city` column
   - Index on `destination_city` column
   - Index on `journey_date` column
   - Composite index on (source, destination, date)
   
2. **Query Optimization**:
   - Use specific fields in SELECT, not SELECT *
   - Add pagination: LIMIT and OFFSET
   - Use JPQL for efficient queries
   
3. **Caching**:
   - Cache popular routes
   - Cached for 5-10 minutes (trips don't change often)
   
4. **Result**:
   - Search time: From 500ms to 50ms
   - Can handle more concurrent searches

---

### Q30: Explain the complete flow from user registration to booking a trip.

**Answer:**

End-to-end user journey with all system interactions.

**Step-by-step:**

1. **Registration**:
   - User sends: username, email, password
   - Server encrypts password with BCrypt
   - Save user in database
   - Send welcome email
   
2. **Login**:
   - User sends credentials
   - Server verifies password
   - Generate JWT token (valid 6 months)
   - Return token to user
   - Send login notification email
   
3. **Search Trip**:
   - User sends: source, destination, date
   - Server queries database (with caching)
   - Returns list of available trips
   
4. **Book Trip**:
   - User selects trip, sends trip ID
   - Request includes JWT token in header
   - JWT Filter validates token
   - Service checks seat availability
   - Creates booking (transactional)
   - Reduces available seats
   - Processes payment
   - All operations in one transaction
   
5. **Confirmation**:
   - Return booking confirmation
   - Send email with trip details
   - User can view in "My Bookings"

---

## Best Practices & Improvements

### Q31: What security measures have you implemented?

**Answer:**

Multiple layers of security protect user data and prevent attacks.

**Step-by-step:**

1. **Authentication**:
   - JWT-based stateless authentication
   - Tokens expire after 6 months
   
2. **Password Security**:
   - BCrypt encryption (strength 12)
   - Never store plain text passwords
   
3. **Input Validation**:
   - Jakarta Validation API
   - Validate email format, password strength
   - Prevent SQL injection via parameterized queries
   
4. **Authorization**:
   - Role-based access control (USER vs ADMIN)
   - Admin-only endpoints protected
   
5. **HTTPS**:
   - All production traffic over SSL
   - Environment variables for sensitive data

---

### Q32: What improvements would you make to this project?

**Answer:**

Several enhancements for production readiness.

**Step-by-step:**

1. **Advanced Caching**:
   - Replace in-memory cache with Redis
   - Distributed caching across servers
   
2. **Async Email Processing**:
   - Use message queue (RabbitMQ, Kafka)
   - Retry failed emails automatically
   
3. **Payment Gateway Integration**:
   - Integrate real payment providers (Stripe, Razorpay)
   - Handle payment webhooks
   
4. **Monitoring & Logging**:
   - Add ELK stack (Elasticsearch, Logstash, Kibana)
   - Real-time error tracking
   
5. **API Rate Limiting**:
   - Prevent abuse with rate limiters
   - Protect against DDoS attacks
   
6. **Containerization**:
   - Docker for consistent deployment
   - Kubernetes for orchestration

---

## Conclusion

This document covers the most common interview questions about the Chaloo project. For each question, understand the problem, solution, and reasoning. Practice explaining these concepts in your own words, focusing on the step-by-step logic rather than memorizing answers.

**Key Takeaways:**
- Understand WHY each technology is used
- Know the flow of data through the system
- Be able to explain trade-offs and alternatives
- Relate concepts to real-world scenarios

Good luck with your interviews! 🚀
