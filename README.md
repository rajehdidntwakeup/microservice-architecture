# RESTful Microservice with Spring Boot, Netflix Eureka, and H2 (PostgreSQL optional)

This project implements a RESTful web service using a microservice architecture. The service manages sensor data, including sensor information and measurements. It includes features like persistent storage, a RESTful API, Swagger UI documentation, user account management with different permissions, and a complete microservice setup with an API Gateway, Service Registry, and Config Server.

## Project Structure

The project is a multi-module Maven project with the following structure:

- `microservice-architecture`: The parent project.
- `api-gateway`: The API Gateway for routing requests to the microservices.
- `config-server`: The Config Server for centralized configuration management.
- `eureka-server`: The Eureka Server for service registration and discovery.
- `sensor-service`: The core microservice for managing sensor data.

## Prerequisites

- Java 21 or higher
- Maven 3.6 or higher
- PostgreSQL (optional; the project runs with in-memory H2 by default)

## Database Setup

By default, sensor-service uses an in-memory H2 database, so you can run the project without installing any external database.

- H2 console: http://localhost:8082/h2-console
  - JDBC URL: jdbc:h2:mem:testdb-sensor-service
  - Username: sa
  - Password: (leave blank)

Optional: Use PostgreSQL
- Create a database and user:
  - CREATE DATABASE sensors;
  - CREATE USER sensors_user WITH PASSWORD 'strong_password';
  - GRANT ALL PRIVILEGES ON DATABASE sensors TO sensors_user;
- Configure sensor-service to use PostgreSQL. You can do it in one of two ways:
  1) Application YAML (sensor-service/src/main/resources/application.yml):

     ```yaml
     spring:
       datasource:
         url: jdbc:postgresql://localhost:5432/sensors
         username: sensors_user
         password: strong_password
         driver-class-name: org.postgresql.Driver
       jpa:
         hibernate:
           ddl-auto: update
         properties:
           hibernate:
             dialect: org.hibernate.dialect.PostgreSQLDialect
     ```

  2) Environment variables when starting the JAR:

     ```bash
     set SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/sensors
     set SPRING_DATASOURCE_USERNAME=sensors_user
     set SPRING_DATASOURCE_PASSWORD=strong_password
     set SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect
     set SPRING_JPA_HIBERNATE_DDL_AUTO=update
     ```

Note: The sensor-service also imports configuration from the Config Server if available (spring.config.import=optional:configserver:http://localhost:8888). Local application.yml values take effect if the Config Server is not running or does not provide overrides.

## How to Run

1.  **Build the project:**

    Navigate to the project root (`microservice-architecture`) and run:

    ```bash
    mvn clean install
    ```

2.  **Run the services in the following order:**

    *   **Eureka Server:**

        ```bash
        java -jar eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar
        ```

    *   **Config Server:**

        ```bash
        java -jar config-server/target/config-server-0.0.1-SNAPSHOT.jar
        ```

    *   **Sensor Service:**

        ```bash
        java -jar sensor-service/target/sensor-service-0.0.1-SNAPSHOT.jar
        ```

    *   **API Gateway:**

        ```bash
        java -jar api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar
        ```

Note on Config Server repository:
- The Config Server is configured to use a Git-backed repo (see config-server/src/main/resources/application.yml).
- Ensure spring.cloud.config.server.git.uri points to a valid repository on your machine, e.g.:
  - Windows (local folder): file:///C:/path/to/config-repo
  - Linux/macOS (local folder): file:///home/you/config-repo
- If the repository is not available, the services will still work because sensor-service treats the import as optional.

### Service Ports
- Eureka Server: http://localhost:8761
- Config Server: http://localhost:8888
- API Gateway: http://localhost:8080
- Sensor Service: http://localhost:8082

## API Usage

### Authentication

-   **Register a new user:**

    ```bash
    curl -X POST http://localhost:8080/sensor-service/auth/register -H "Content-Type: application/json" -d '{"username":"user","password":"password"}'
    ```

-   **Authenticate and get a JWT token:**

    ```bash
    curl -X POST http://localhost:8080/sensor-service/auth/authenticate -H "Content-Type: application/json" -d '{"username":"user","password":"password"}'
    ```

### Sensors

-   **Get all sensors:**

    ```bash
    curl -X GET http://localhost:8080/sensor-service/sensors -H "Authorization: Bearer <your_jwt_token>"
    ```

-   **Create a new sensor (requires WRITE role):**

    ```bash
    curl -X POST http://localhost:8080/sensor-service/sensors -H "Content-Type: application/json" -H "Authorization: Bearer <your_jwt_token>" -d '{"name":"Living Room Sensor","location":"Living Room","active":true,"type":"indoor"}'
    ```

### Measurements

-   **Get all measurements:**

    ```bash
    curl -X GET http://localhost:8080/sensor-service/measurements -H "Authorization: Bearer <your_jwt_token>"
    ```

-   **Create a new measurement (requires WRITE role):**

    ```bash
    curl -X POST http://localhost:8080/sensor-service/measurements -H "Content-Type: application/json" -H "Authorization: Bearer <your_jwt_token>" -d '{"sensorId":1,"timestamp":"2025-10-22T10:00:00","temperature":25.5,"humidity":60.0}'
    ```

## Swagger UI

Once the `sensor-service` is running, you can access the Swagger UI at:

[http://localhost:8080/sensor-service/swagger-ui/index.html](http://localhost:8080/sensor-service/swagger-ui/index.html)


# Microservice Architecture

This repository contains a sample microservice architecture with the following modules:
- eureka-server
- config-server
- sensor-service
- api-gateway

## Running locally
Ensure you have Java 21 and Maven installed. Start services in this order:
1. eureka-server
2. config-server
3. sensor-service
4. api-gateway

## Troubleshooting

- Error: java: java.lang.ExceptionInInitializerError / com.sun.tools.javac.code.TypeTag :: UNKNOWN
  - Meaning: This happens during compilation when an annotation processor (commonly Lombok) uses internal javac APIs that changed in JDK 21, causing a failure to initialize with TypeTag.UNKNOWN.
  - Fix in this repo: We pin Lombok to a JDK‑21 compatible version (1.18.32) in sensor-service/pom.xml.
  - What you can do if you still see it:
    - Clean and rebuild the project (mvn clean package).
    - Ensure your IDE uses JDK 21 for both project SDK and compiler, and that the Lombok plugin is up to date. Enable annotation processing in IDE settings.
    - If building multi-module, ensure all modules use the same Java version.

- Config Server cannot start/serve config due to invalid Git URI
  - Meaning: spring.cloud.config.server.git.uri in config-server/application.yml points to a non-existent or inaccessible location (e.g., file:///home/ubuntu/... on Windows).
  - Fix: Change it to a valid path or Git URL on your machine, for example:
    - Windows local folder: file:///C:/path/to/config-repo
    - Public Git repo: https://github.com/you/your-config-repo.git
  - Note: sensor-service marks the import as optional; it will still run using its local application.yml if the Config Server is unavailable.

