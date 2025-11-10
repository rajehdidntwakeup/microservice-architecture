package com.example.microservice.architecture.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            // Access sensor-service via a friendly base path, keeping the same downstream paths
            // Example: GET http://localhost:8080/sensors -> forwards to lb://sensor-service/sensors
            //          GET http://localhost:8080/sensors/1 -> forwards to lb://sensor-service/sensors/1
            .route("sensor-service-sensors", r -> r
                .path("/sensors/**")
                .uri("lb://sensor-service"))

            // Also support the classic service-id style path with prefix stripping
            // Example: GET http://localhost:8080/sensor-service/sensors -> forwards to lb://sensor-service/sensors
            .route("sensor-service-by-id", r -> r
                .path("/sensor-service/**")
                .filters(f -> f.stripPrefix(1))
                .uri("lb://sensor-service"))

            // Proxy OpenAPI docs of sensor-service through the gateway so Swagger UI can use it
            // GET http://localhost:8080/v3/api-docs/sensor-service -> forwards to lb://sensor-service/v3/api-docs
            .route("sensor-service-openapi", r -> r
                .path("/v3/api-docs/sensor-service")
                .filters(f -> f.setPath("/v3/api-docs"))
                .uri("lb://sensor-service"))

            // Forward auth endpoints through the gateway so Swagger/UI can call them without prefix
            .route("sensor-service-auth", r -> r
                .path("/auth/**")
                .uri("lb://sensor-service"))

            .route("measurement-service", r -> r
                .path("/measurements/**")
                .uri("lb://sensor-service"))

            .route("user-service", r -> r
                .path("/users/**")
                .uri("lb://sensor-service"))

            .build();
    }
}
