# Spring Boot Backend Zero to Hero — Revision Notes

# Project Overview

This project is a real-world backend microservices learning journey using:

* Spring Boot
* MongoDB
* Kafka
* Redis
* JWT Authentication
* Spring Security
* Docker
* Kubernetes
* Logging & Monitoring

The goal is to learn industry-level backend engineering step by step.

---

# 1. What Is Backend Development?

Backend development is responsible for:

* business logic
* APIs
* authentication
* database communication
* security
* scalability
* asynchronous processing

Frontend talks to backend using APIs.

---

# 2. What Is Spring Boot?

Spring Boot is a Java framework used for building enterprise backend applications quickly.

It provides:

* dependency injection
* embedded server
* auto configuration
* production-ready features
* security support
* database integration

---

# Why Companies Use Spring Boot

* scalable
* secure
* microservice-friendly
* huge ecosystem
* industry standard

Used heavily in:

* fintech
* banking
* e-commerce
* healthcare
* enterprise software

---

# 3. What Is Microservices Architecture?

Microservices architecture means:

breaking one huge application into multiple small independent services.

---

# Example

```text
Auth Service
Transaction Service
Notification Service
Analytics Service
Gateway Service
```

---

# Advantages

* independent deployment
* scalability
* fault isolation
* easier maintenance
* technology flexibility

---

# Monolith vs Microservices

| Monolith           | Microservices            |
| ------------------ | ------------------------ |
| Single application | Multiple services        |
| Single deployment  | Independent deployment   |
| Hard scaling       | Easy scaling             |
| Tight coupling     | Loose coupling           |
| Easier initially   | Better for large systems |

---

# 4. Layered Architecture

Industry applications usually follow:

```text
Controller
   ↓
Service
   ↓
Repository
   ↓
Database
```

---

# Controller Layer

Responsible for:

* receiving HTTP requests
* returning responses
* request mapping

Example:

```java
@RestController
@RequestMapping("/api/auth")
```

---

# Why Controller Should Be Thin

Controller should NOT contain:

* business logic
* DB logic
* JWT generation

Because:

* difficult maintenance
* poor readability
* hard testing

---

# Service Layer

Contains:

* business logic
* validations
* password encoding
* JWT generation
* orchestration

Example:

```java
@Service
public class AuthServiceImpl
```

---

# Why Service Layer Important

* separation of concerns
* reusable logic
* scalability
* easier testing

---

# Repository Layer

Responsible for database communication.

Example:

```java
@Repository
public interface UserRepository
        extends MongoRepository<User, String>
```

---

# Why Repository Layer Needed

Keeps database logic separate from business logic.

---

# 5. DTO (Data Transfer Object)

DTO is used to transfer data between layers.

Examples:

```java
RegisterRequest
LoginRequest
AuthResponse
```

---

# Why DTO Used Instead Of Entity

Entity represents database structure.

DTO represents API contract.

Using Entity directly can:

* expose sensitive data
* tightly couple APIs with DB
* create security issues

---

# DTO vs Entity

| DTO                     | Entity                  |
| ----------------------- | ----------------------- |
| Request/response object | Database object         |
| API layer               | Persistence layer       |
| Not mapped to DB        | Mapped to DB            |
| Safer for APIs          | Contains full DB fields |

---

# 6. Entity

Entity represents database document/table.

MongoDB Example:

```java
@Document(collection = "users")
public class User
```

---

# Important Annotations

## @Document

Maps class → MongoDB collection.

---

## @Id

Primary key for MongoDB document.

---

# 7. MongoDB

MongoDB is a NoSQL document database.

Stores:

```text
JSON-like documents
```

instead of relational tables.

---

# Why MongoDB Used

* flexible schema
* microservice-friendly
* scalable
* fast development

---

# SQL vs MongoDB

| SQL           | MongoDB         |
| ------------- | --------------- |
| Tables        | Documents       |
| Strict schema | Flexible schema |
| Complex joins | Easier scaling  |
| Relational    | Document-based  |

---

# 8. Repository Methods

Spring automatically provides:

```java
save()
findById()
findAll()
deleteById()
```

through:

```java
MongoRepository
```

---

# Custom Query Methods

Example:

```java
Optional<User> findByEmail(String email)
```

Spring generates query automatically.

---

# 9. Why Optional Used?

Because user may or may not exist.

Avoids:

```text
NullPointerException
```

---

# Example

```java
.orElseThrow(...)
```

Forces safe handling.

---

# Optional vs Null

| Null                           | Optional        |
| ------------------------------ | --------------- |
| Unsafe                         | Safer           |
| Can cause NullPointerException | Forces handling |
| Legacy approach                | Modern approach |

---

# 10. Dependency Injection

Instead of manually creating objects:

```java
new BCryptPasswordEncoder()
```

Spring injects dependencies automatically.

---

# Benefits

* loose coupling
* centralized management
* easier testing
* reusable components

---

# Example

```java
private final BCryptPasswordEncoder passwordEncoder;
```

---

# 11. Spring Beans

A Bean is an object managed by Spring container.

---

# Example

```java
@Bean
public BCryptPasswordEncoder passwordEncoder()
```

---

# Why Bean Needed

Spring can only inject objects it manages.

---

# 12. Difference Between @Component, @Service, @Repository

All create Beans.

Difference is semantic meaning.

| Annotation  | Purpose              |
| ----------- | -------------------- |
| @Component  | Generic Bean         |
| @Service    | Business logic layer |
| @Repository | Database layer       |

---

# 13. Why Interface + Implementation?

Example:

```java
AuthService
AuthServiceImpl
```

---

# Why Not Direct Implementation?

Interfaces provide:

* abstraction
* loose coupling
* easier testing
* multiple implementations

---

# SOLID Principle

This follows:

```text
Dependency Inversion Principle
```

---

# 14. Authentication vs Authorization

VERY IMPORTANT INTERVIEW QUESTION.

---

# Authentication

```text
Who are you?
```

Example:

* login
* JWT validation

---

# Authorization

```text
What are you allowed to do?
```

Example:

* admin access
* role permissions

---

# 15. JWT (JSON Web Token)

JWT is used for authentication.

After successful login:

backend generates token.

Client sends token in future requests.

---

# JWT Structure

```text
HEADER.PAYLOAD.SIGNATURE
```

---

# Why JWT Used

Because HTTP is stateless.

JWT carries user identity.

---

# Advantages

* stateless
* scalable
* microservice-friendly
* no session storage needed

---

# JWT Flow

```text
Client logs in
      ↓
JWT generated
      ↓
Client stores token
      ↓
Future requests send token
      ↓
Backend validates token
      ↓
Protected APIs accessible
```

---

# Why JWT Better Than Sessions

| Session                     | JWT                     |
| --------------------------- | ----------------------- |
| Server stores session       | Stateless               |
| Difficult scaling           | Easy scaling            |
| Memory usage                | Lightweight             |
| Hard in distributed systems | Great for microservices |

---

# 16. BCrypt Password Encoding

Passwords should NEVER be stored plain.

---

# BAD

```text
123456
```

---

# GOOD

```text
$2a$10$encodedPassword
```

---

# Why BCrypt?

* hashing
* salting
* secure against brute force
* industry standard

---

# Hashing vs Encryption

| Hashing            | Encryption              |
| ------------------ | ----------------------- |
| One-way            | Two-way                 |
| Cannot decrypt     | Can decrypt             |
| Used for passwords | Used for sensitive data |

---

# Password Verification

```java
passwordEncoder.matches(raw, encoded)
```

---

# 17. Spring Security

Used for:

* authentication
* authorization
* endpoint protection
* security filters

---

# Why Spring Security Important

Without it:

developers manually handle:

* login
* authorization
* filters
* session management
* token validation

---

# 401 vs 403

| 401                   | 403           |
| --------------------- | ------------- |
| Not authenticated     | Access denied |
| Invalid/missing token | No permission |

---

# Why CSRF Disabled In JWT Systems

JWT systems are:

* stateless
* token-based

CSRF mainly affects:

* browser cookie authentication

---

# SecurityConfig Purpose

Defines:

* public APIs
* protected APIs
* authentication rules
* security filters

---

# 18. JWT Authentication Filter

JWT filter intercepts every request.

Responsibilities:

```text
Check Authorization header
       ↓
Extract token
       ↓
Validate token
       ↓
Extract user info
       ↓
Authenticate request
```

---

# Why Filter Needed

Without filter:

backend cannot validate incoming JWT.

---

# 19. Security Context

After successful authentication:

Spring stores authenticated user in:

```java
SecurityContextHolder
```

---

# Why Important

Used to:

* get logged-in user
* check roles
* authorize APIs

---

# 20. Environment Variables

Avoid hardcoding secrets.

---

# BAD

```java
private String secret = "abcd";
```

---

# GOOD

```yaml
jwt:
  secret: ${JWT_SECRET}
```

---

# Why Environment Variables Important

Protects:

* JWT secrets
* DB passwords
* API keys

Supports different configs for:

```text
DEV
UAT
PROD
```

---

# 21. Logging

Used for:

* debugging
* production monitoring
* tracing requests
* identifying failures

---

# Logging Example

```java
log.info("User logged in")
```

---

# Logging Levels

```text
INFO
WARN
ERROR
DEBUG
```

---

# Why Logging Important In Companies

Production systems cannot be debugged manually.

Logs are analyzed using:

* Splunk
* ELK
* Datadog

---

# 22. Kafka

Kafka is an event streaming platform.

Used for asynchronous communication.

---

# Why Kafka Used

Without Kafka:

```text
Service A waits for Service B
```

Tightly coupled.

---

# With Kafka

```text
Service A publishes event
       ↓
Service B consumes later
```

Loose coupling.

---

# Kafka Benefits

* scalability
* retries
* asynchronous systems
* event-driven architecture

---

# Real Example

```text
User Registered
      ↓
Kafka Event Published
      ↓
Notification Service Sends Email
```

---

# 23. Redis

Redis is an in-memory database.

Very fast.

---

# Why Redis Used

* caching
* OTP storage
* session storage
* rate limiting

---

# Redis vs MongoDB

| Redis          | MongoDB            |
| -------------- | ------------------ |
| Memory-based   | Disk-based         |
| Ultra-fast     | Persistent storage |
| Used for cache | Used for main DB   |

---

# 24. API Gateway

Gateway acts as single entry point.

---

# Responsibilities

* routing
* authentication
* rate limiting
* centralized security

---

# Real Architecture

```text
Frontend
    ↓
API Gateway
    ↓
Microservices
```

---

# 25. Docker

Docker containerizes applications.

Ensures:

```text
Works same everywhere
```

---

# Why Docker Used

* portability
* consistent environments
* deployment simplicity

---

# 26. Kubernetes

Kubernetes manages containers.

---

# Responsibilities

* scaling
* self healing
* orchestration
* deployment management

---

# Why Kubernetes Important

Managing containers manually becomes impossible at scale.

---

# 27. Synchronous vs Asynchronous Communication

| Synchronous        | Asynchronous        |
| ------------------ | ------------------- |
| Waits for response | Doesn't wait        |
| REST APIs          | Kafka/Event systems |
| Tight coupling     | Loose coupling      |

---

# 28. Component Scanning

Spring automatically scans packages for:

* @Service
* @Repository
* @Controller
* @Configuration

---

# Why Package Structure Important

If classes are outside scan path:

Spring won't detect them.

---

# Correct Structure

```text
com.finpulse.fin
    ├── config
    ├── controller
    ├── service
    ├── repository
```

---

# 29. Common Errors Learned

# 401 Unauthorized

Cause:

* token missing
* invalid token
* protected endpoint

---

# 403 Forbidden

Cause:

* CSRF enabled
* permission issue

---

# WeakKeyException

Cause:

JWT secret too short.

HS256 requires minimum 256-bit key.

---

# Bean Not Found

Cause:

Spring Bean missing.

Example:

```java
BCryptPasswordEncoder Bean not defined
```

---

# 30. End-To-End Login Flow

MOST IMPORTANT INTERVIEW FLOW.

```text
Client sends login request
        ↓
Controller receives request
        ↓
DTO maps request body
        ↓
Service validates credentials
        ↓
Repository fetches user from MongoDB
        ↓
BCrypt verifies password
        ↓
JWT generated
        ↓
Token returned to client
        ↓
Future requests carry token
        ↓
JWT filter validates token
        ↓
Protected APIs become accessible
```

---

# 31. Real Enterprise Architecture Flow

```text
Frontend
    ↓
API Gateway
    ↓
Auth Service validates JWT
    ↓
Microservices communicate using Kafka
    ↓
Redis used for caching
    ↓
MongoDB stores persistent data
    ↓
Logs monitored in Splunk
    ↓
Docker containers deployed on Kubernetes
```

---

# 32. Most Important Interview Topics

You should confidently explain:

* layered architecture
* DTO vs Entity
* dependency injection
* JWT flow
* Spring Security
* authentication vs authorization
* Kafka basics
* Redis usage
* MongoDB concepts
* logging
* API Gateway
* microservices flow
* why environment variables are important
* why BCrypt is used
* why Optional is used
* why Service layer exists

---

# 33. Backend Engineering Mindset

Always think:

* scalability
* security
* maintainability
* loose coupling
* observability
* production readiness

---

# 34. Current Learning Progress

Completed:

* Spring Boot basics
* REST APIs
* MongoDB integration
* layered architecture
* DTOs
* repositories
* authentication
* JWT generation
* Spring Security basics
* logging
* production configuration basics

Next:

* JWT validation filter
* protected APIs
* role-based access
* Kafka integration
* Redis caching
* Docker
* Kubernetes
* API Gateway
* distributed tracing

---

# Final Goal

Become capable of building:

```text
Production-grade enterprise microservices backend systems from scratch
```

using modern backend architecture and industry standards.
