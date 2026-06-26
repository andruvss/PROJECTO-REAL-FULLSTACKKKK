package com.redsalud.gateway.config;

import com.redsalud.gateway.filter.JwtAuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    
    private final JwtAuthenticationFilter jwtFilter;
    
    public GatewayConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }
    
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            // Patient Service
            .route("patient-service", r -> r
                .path("/api/patients/**")
                .filters(f -> f.filter(jwtFilter.apply(new JwtAuthenticationFilter.Config())))
                .uri("lb://patient-service"))
            
            // Request Service
            .route("request-service", r -> r
                .path("/api/requests/**")
                .filters(f -> f.filter(jwtFilter.apply(new JwtAuthenticationFilter.Config())))
                .uri("lb://request-service"))
            
            // Waiting List Service
            .route("waiting-list-service", r -> r
                .path("/api/waiting-lists/**")
                .filters(f -> f.filter(jwtFilter.apply(new JwtAuthenticationFilter.Config())))
                .uri("lb://waiting-list-service"))
            
            // Auth Service (sin JWT - es el login)
            .route("auth-service", r -> r
                .path("/api/auth/**")
                .uri("lb://auth-service"))
            
            .build();
    }
}
