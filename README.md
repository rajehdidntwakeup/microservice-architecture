# Microservice Architecture

A robust, scalable microservice-based system built with Spring Boot and Spring Cloud. This project demonstrates a sensor data management system with centralized configuration, service discovery, and secure API routing.

## 🏗️ Architecture Overview

The system follows a modern microservice architecture:

*   **API Gateway:** The entry point for all client requests, providing routing and CORS management.
*   **Eureka Server:** Handles service registration and discovery, allowing services to find each other dynamically.
*   **Config Server:** Provides centralized configuration management across all environments.
*   **Sensor Service:** The core business service responsible for managing sensors, measurements, and user authentication/authorization.

## 🛠️ Technologies Used

*   **Java 24**
*   **Spring Boot 3.5.6**
*   **Spring Cloud 2025.0.0**
*   **Spring Security & JWT** (Authentication and Authorization)
*   **Spring Data JPA** (Persistence)
*   **PostgreSQL** (Managed via Aiven)
*   **H2 Database** (In-memory for local testing)
*   **Spring Cloud Gateway**
*   **Netflix Eureka**
*   **Springdoc OpenAPI (Swagger)**

## 📂 Project Structure

```text
microservice-architecture/
├── api-gateway/       # API Gateway & Routing
├── config-server/     # Centralized Configuration
├── eureka-server/     # Service Discovery Server
├── sensor-service/    # Core Business Logic & Auth
└── pom.xml            # Parent Maven BOM
```

## 🚀 Getting Started

### Prerequisites

*   **JDK 24** or higher.
*   **Maven 3.9+**.
*   **PostgreSQL** (Optional, if not using the default cloud instance).

### Installation & Setup

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/yourusername/microservice-architecture.git
    cd microservice-architecture
    ```

2.  **Build the project:**
    ```bash
    mvn clean install
    ```

### Running the Services

For the system to function correctly, start the services in the following order:

1.  **Config Server:** `mvn -pl config-server spring-boot:run` (Port 8888)
2.  **Eureka Server:** `mvn -pl eureka-server spring-boot:run` (Port 8761)
3.  **Sensor Service:** `mvn -pl sensor-service spring-boot:run` (Port 8082)
4.  **API Gateway:** `mvn -pl api-gateway spring-boot:run` (Port 8080)

## 📖 API Documentation

Once the services are running, you can access the aggregated Swagger UI through the API Gateway:

*   **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### Key Endpoints

#### Authentication
*   `POST /auth/register` - Register a new user.
*   `POST /auth/authenticate` - Authenticate and receive a JWT.

#### Sensors
*   `GET /sensors` - List all sensors.
*   `POST /sensors` - Create a new sensor.
*   `GET /sensors/{id}` - Get sensor details.

#### Measurements
*   `GET /measurements` - List all measurements.
*   `POST /measurements` - Record a new measurement.
*   `GET /measurements/sensor/{sensorId}` - Get measurements for a specific sensor.

## 🧪 Testing

The project includes unit and integration tests for core services.

To run tests:
```bash
mvn test
```

## 📄 License

This project is licensed under the [MIT License](LICENSE).
