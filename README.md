# Sporty Group - Bet Settlement Service

This is a Spring Boot application that simulates a simple sports betting settlement workflow.

The application receives sports event outcomes through a REST endpoint, publishes them to Kafka, consumes the messages, settles matching bets stored in an H2 database, and finally sends the settlement result to a mocked RocketMQ producer.

The goal was to keep the solution simple while still demonstrating event-driven communication and bet settlement logic.

---

## How It Works

The flow is:

```text
POST /api/events/outcome
        |
        v
Kafka Producer
        |
        v
Kafka Topic (event-outcomes)
        |
        v
Kafka Consumer
        |
        v
Bet Settlement Service
        |
        +--> Find pending bets
        +--> Compare prediction with actual result
        +--> Mark WON or LOST
        +--> Save updated status
        |
        v
RocketMQ Producer (mocked)
        |
        v
Log settlement event
```

### Settlement Process

1. An event outcome is submitted through the REST API.
2. The outcome is published to Kafka.
3. A Kafka consumer receives the message.
4. The settlement service looks up all pending bets for that event.
5. Each bet is evaluated against the actual outcome.
6. Bets are marked as either WON or LOST.
7. A settlement message is sent to the mocked RocketMQ producer.

---

## Tech Stack

* Java 17
* Spring Boot 3.2.5
* Apache Kafka
* H2 Database
* Maven
* Mocked RocketMQ integration

---

## Running the Application

### Prerequisites

* Java 17+
* Maven 3.8+
* Docker / Docker Compose

### Start Kafka

```bash
docker-compose up -d
```

This starts Kafka and Zookeeper locally.

### Run the Application

```bash
mvn spring-boot:run
```

The application will start on:

```text
http://localhost:8080
```

H2 Console:

```text
http://localhost:8080/h2-console
```

Connection details:

```text
JDBC URL: jdbc:h2:mem:settlementdb
Username: sa
Password:
```

---

## API

### Publish Event Outcome

```bash
curl -X POST http://localhost:8080/api/events/outcome \
-H "Content-Type: application/json" \
-d '{
  "eventId":"EVT-001",
  "outcome":"TEAM_A_WIN",
  "resolvedAt":"2024-06-10T15:00:00"
}'
```

Response:

```text
202 Accepted
```

Example logs:

```text
Settling 4 bets for eventId=EVT-001

Bet id=1 settled as WON
Bet id=2 settled as LOST
Bet id=3 settled as LOST
Bet id=4 settled as WON

[RocketMQ MOCK] Sending settlement event...
```

### Health Check

```bash
curl http://localhost:8080/api/events/health
```

---

## Seed Data

A few sample bets are loaded when the application starts.

| Event ID | User     | Prediction   | Amount |
| -------- | -------- | ------------ | ------ |
| EVT-001  | user-101 | TEAM_A_WIN   | 50     |
| EVT-001  | user-102 | DRAW         | 25     |
| EVT-001  | user-103 | TEAM_B_WIN   | 100    |
| EVT-001  | user-104 | TEAM_A_WIN   | 75     |
| EVT-002  | user-105 | PLAYER_A_WIN | 30     |
| EVT-002  | user-106 | PLAYER_B_WIN | 60     |

This makes it easier to test settlement without manually inserting records.

---

## Design Notes

### Why a single application?

For the scope of this exercise, splitting everything into separate microservices felt unnecessary. A modular Spring Boot application keeps things easier to run and understand while still separating responsibilities through packages.

### Why H2?

H2 keeps setup simple and avoids requiring an external database. In a real system I would use PostgreSQL and add indexes around event and settlement lookups.

### Why mock RocketMQ?

The focus of the exercise is the settlement workflow itself. Running a real RocketMQ broker would add more infrastructure than business value, so I mocked the producer and logged the payload instead.

### Retry Handling

I haven't implemented retries or dead-letter queues. In a production system I would use Kafka retry topics and a DLQ strategy for failed processing.

### Idempotency

At the moment duplicate Kafka messages could result in duplicate settlement attempts. Since only pending bets are processed, this can be improved further with event-level idempotency if required.

---

## Tests

Run tests with:

```bash
mvn test
```

Current test coverage includes:

* Bet wins when prediction matches outcome
* Bet loses when prediction does not match outcome
* No settlement when there are no matching pending bets

---

## AI Usage

AI tools were used during development to help with brainstorming, project structure, and generating some boilerplate code.

The overall design, implementation decisions, settlement logic, and final code review were completed by me. Any generated code was reviewed and adjusted where necessary to fit the requirements of the assignment.

---

## Possible Improvements

* Add stronger idempotency protection
* Introduce Kafka dead-letter topics
* Replace H2 with PostgreSQL
* Implement real RocketMQ integration
* Add APIs for querying settled bets
* Add metrics and monitoring
* Support multiple Kafka partitions and consumers

---

## Troubleshooting

### Kafka connection errors

Make sure Docker is running and Kafka has started successfully:

```bash
docker-compose up -d
```

### H2 console unavailable

Check that:

```properties
spring.h2.console.enabled=true
```

is enabled.

### Bets are not settling

Verify that the eventId in the request matches one of the seeded events, such as:

```text
EVT-001
EVT-002
```

and check the application logs for consumer activity.
