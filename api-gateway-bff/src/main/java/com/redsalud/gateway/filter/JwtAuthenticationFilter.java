package com.redsalud.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {
    
    public JwtAuthenticationFilter() {
        super(Config.class);
    }
    
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Obtener el token del header Authorization
            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            
            // Si no hay token, rechazar
            if (authHeader == null || authHeader.isEmpty()) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            
            // Validar formato: "Bearer <token>"
            if (!authHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            
            String token = authHeader.substring(7);
            
            // Validación básica del token (en producción usar JWT validation library)
            if (token == null || token.isEmpty() || token.length() < 10) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            
            // Token válido, continuar
            return chain.filter(exchange);
        };
    }
    
    public static class Config {
        // Configuración del filtro
    }
}
