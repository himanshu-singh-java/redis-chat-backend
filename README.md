# Redis Chat Application (Backend)

A RESTful Chat Application backend built using Spring Boot and Redis. 
This project implements chat rooms, user joining, messaging, and real-time message
broadcasting using Redis Pub/Sub as per the provided assignment requirements.

## Tech Stack
- Java: 21
- Framework: Spring Boot 4.1.2
- Database / Message Broker: Redis (Lettuce Client)
- Testing: JUnit 5, Mockito
- Build Tool: Maven
- Utilities: Lombok, Jakarta Validation

## Features Implemented
1. Redis Data Structures Used:
    - Hash: To store chat room metadata.
    - Set: To manage unique participants in a room.
    - List: To store the chat history chronologically.
2. Real-time Messaging: Redis Pub/Sub (RedisMessagePublisher & RedisMessageSubscriber) is used to broadcast new messages instantly across the room channels.
3. Robust Error Handling: A global @RestControllerAdvice handles custom ChatException and @Valid validation errors, returning clean JSON responses.
4. Clean Architecture: Proper separation of concerns using Controllers, Services, Repositories, and Mappers.
5. Testing: Comprehensive unit test coverage for the Service layer using Mockito and JUnit 5.

## How to Run the Project

### 1. Start Redis Server (via Docker)
The application requires a local Redis server running on the default port 6379. If you have Docker installed, simply run:
docker run --name my-redis -p 6379:6379 -d redis

### 2. Run the Spring Boot Application
*(Note for Linux/Mac users: If you get a permission denied error for maven wrapper, make it executable first by running: `chmod +x mvnw`)*

You can run the project using your IDE by executing RedischatApplication.java, or via Maven in the terminal:
./mvnw spring-boot:run
The application will start on http://localhost:8080.

## API Documentation (Testable via Postman)

1. Create a Chat Room
- Endpoint: POST /api/chatapp/chatrooms
- Body: {"roomName": "general"}

2. Join a Chat Room
- Endpoint: POST /api/chatapp/chatrooms/{roomId}/join
- Body: {"participant": "guest_user"}

3. Send a Message
- Endpoint: POST /api/chatapp/chatrooms/{roomId}/messages
- Body: {"participant": "guest_user", "message": "Hello, everyone!"}
  (Note: Sending a message will also trigger the Redis Pub/Sub and print a [REAL-TIME ALERT] in the application console).

4. Retrieve Chat History
- Endpoint: GET /api/chatapp/chatrooms/{roomId}/messages?limit=10
- Response: Returns a JSON array containing the last N messages in chronological order.

## Running Unit Tests
To execute the comprehensive JUnit test suite:
./mvnw test