
# Inventory Management Service

Inventory Management Service is a Spring Boot microservice responsible for managing product inventory, producing Kafka events, and providing secure access through JWT-based authentication.

---

## Features

- Manage inventory transactions: CREATE, UPDATE, CANCEL  
- Produce transaction events to Kafka topics (`transactions`)  
- Secure authentication using JWT tokens  
- Password hashing with BCrypt before storing in MongoDB  
- Dockerized for easy deployment  

---

## Prerequisites

- Java 17 or higher  
- Maven 3.9+  
- Docker & Docker Compose  
- MongoDB Atlas or local MongoDB instance  

---

## Setup & Running

1.  **Clone the repository:**

    ```bash
    git clone <your-repo-url>
    cd inventory-management
    ```

2.  **Build the project:**

    ```bash
    mvn clean install
    ```

3.  **Run locally:**

    ```bash
    mvn spring-boot:run
    ```

    The service runs on port 8080 by default.

### Docker Setup

#### Docker Compose configuration example:

```yaml
services:
  kafka:
    image: bitnami/kafka:3.7.0
    container_name: inventory-kafka
    ports:
      - "9092:9092"
      - "9094:9094"
    environment:
      - KAFKA_CFG_NODE_ID=1
      - KAFKA_CFG_PROCESS_ROLES=broker,controller
      - KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093,PLAINTEXT_HOST://:9094
      - KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://inventory-kafka:9092,PLAINTEXT_HOST://localhost:9094
      - KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      - KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=1@inventory-kafka:9093
      - KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER
      - ALLOW_PLAINTEXT_LISTENER=yes
    volumes:
      - kafka_data:/bitnami/kafka
    networks:
      - inventory-net

  kafka-ui:
    image: provectuslabs/kafka-ui:latest
    container_name: inventory-kafka-ui
    ports:
      - "8081:8080"
    environment:
      - KAFKA_CLUSTERS_0_NAME=local
      - KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS=inventory-kafka:9092
    depends_on:
      - kafka
    networks:
      - inventory-net

  inventory-service:
    build: .
    container_name: inventory-service
    ports:
      - "8080:8080"
    environment:
      SPRING_DATA_MONGODB_URI: "<Your MongoDB URI>"
      JWT_SECRET: "<your-secret-key>"
      SPRING_KAFKA_BOOTSTRAP_SERVERS: "inventory-kafka:9092"
    depends_on:
      - kafka
    networks:
      - inventory-net

volumes:
  kafka_data:

networks:
  inventory-net:
    driver: bridge
Run services:
code
Bash
docker-compose up -d
Kafka Testing
Exec into Kafka container:
code
Bash
docker exec -it inventory-kafka bash
Create a topic:
code
Bash
kafka-topics.sh --bootstrap-server localhost:9092 --create --topic transactions --partitions 1 --replication-factor 1
Produce a test message:
code
Bash
kafka-console-producer.sh --broker-list localhost:9092 --topic transactions
Example message:
code
JSON
{"transactionId":"123","productId":"p1","userId":"u1","quantity":5,"status":"CREATED"}
Consume messages (optional):
code
Bash
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic transactions --from-beginning
API Endpoints
Method	Endpoint	Description
POST	/transactions	Create a new inventory transaction
GET	/transactions	List all transactions
GET	/transactions/{id}	Get transaction by ID
PUT	/transactions/{id}	Update a transaction
DELETE	/transactions/{id}	Cancel a transaction
POST	/auth/register	Register a new user
POST	/auth/login	Login and receive JWT token
Security
JWT Authentication
All endpoints (except /auth/register and /auth/login) require a valid JWT token.
JWT token should be included in the Authorization header:
code
Code
Authorization: Bearer <token>
Password Hashing
User passwords are never stored in plain text.
BCrypt hashing is used:
code
Java
BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
String hashedPassword = encoder.encode(plainPassword);
Hashed password is stored in MongoDB for secure authentication.
Configuration
application.properties or environment variables:
code
Properties
spring.data.mongodb.uri=<MongoDB URI>
spring.kafka.bootstrap-servers=inventory-kafka:9092
jwt.secret=<your-secret-key>
code
Code
