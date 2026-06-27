package com.redsalud.gateway.config;

import com.redsalud.gateway.filter.JwtAuthenticationFilter;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;

@Configuration
public class GatewayConfig {
    
    private final JwtAuthenticationFilter jwtFilter;
    
    public GatewayConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public GlobalFilter removeDuplicateCorsFilter() {
        return (exchange, chain) -> chain.filter(exchange).then(
            reactor.core.publisher.Mono.fromRunnable(() -> {
                HttpHeaders headers = exchange.getResponse().getHeaders();
                if (headers.containsKey(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)) {
                    String first = headers.getFirst(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN);
                    if (first != null && first.contains("*")) {
                        headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:5173");
                    } else if (first != null) {
                        headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, first);
                    }
                }
            })
        );
    }
    
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()

            // ─── Rutas públicas de Swagger/OpenAPI (sin JWT) ───────────────
            .route("patient-service-docs", r -> r
                .path("/api/patients/v3/api-docs")
                .filters(f -> f.rewritePath("/api/patients/v3/api-docs", "/v3/api-docs"))
                .uri("lb://patient-service"))

            .route("request-service-docs", r -> r
                .path("/api/requests/v3/api-docs")
                .filters(f -> f.rewritePath("/api/requests/v3/api-docs", "/v3/api-docs"))
                .uri("lb://request-service"))

            .route("waiting-list-service-docs", r -> r
                .path("/api/waiting-list/v3/api-docs")
                .filters(f -> f.rewritePath("/api/waiting-list/v3/api-docs", "/v3/api-docs"))
                .uri("lb://waiting-list-service"))

            // ─── Rutas protegidas con JWT ───────────────────────────────────
            .route("patient-service", r -> r
                .path("/api/patients/**")
                .filters(f -> f.filter(jwtFilter.apply(new JwtAuthenticationFilter.Config())))
                .uri("lb://patient-service"))
            
            .route("request-service", r -> r
                .path("/api/requests/**")
                .filters(f -> f.filter(jwtFilter.apply(new JwtAuthenticationFilter.Config())))
                .uri("lb://request-service"))
            
            .route("waiting-list-service", r -> r
                .path("/api/waiting-list/**")
                .filters(f -> f.filter(jwtFilter.apply(new JwtAuthenticationFilter.Config())))
                .uri("lb://waiting-list-service"))
            
            .route("auth-service", r -> r
                .path("/api/auth/**")
                .uri("lb://auth-service"))
            
            .build();
    }
}