package com.redsalud.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // 1. Ruta de Pacientes (patient-service)
                .route("patient-service", r -> r.path("/api/patients/**")
                        .filters(f -> f.dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_FIRST"))
                        .uri("http://localhost:8081"))

                // 2. Ruta de Solicitudes protegida con el Circuit Breaker
                .route("request-service", r -> r.path("/api/requests/**")
                        .filters(f -> f.circuitBreaker(c -> c
                                .setName("requestCircuitBreaker")
                                .setFallbackUri("forward:/fallback/requests"))
                                .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_FIRST"))
                        .uri("http://localhost:8082"))

                // 3. Ruta de Lista de Espera (waiting-list-service)
                .route("waiting-list-service", r -> r.path("/api/waiting-list/**")
                        .filters(f -> f.dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_FIRST"))
                        .uri("http://localhost:8083"))

                // 4. Ruta técnica para la documentación de la Lista de Espera (CORREGIDA)
                .route("waiting-list-docs", r -> r.path("/api/waiting-list/v3/api-docs")
                        .filters(f -> f.rewritePath("/api/waiting-list/v3/api-docs", "/v3/api-docs")
                                .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_FIRST"))
                        .uri("http://localhost:8083"))
                .build();
    }
}