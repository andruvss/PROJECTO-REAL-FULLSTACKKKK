package com.redsalud.gateway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    // Cambiado a Mono<ResponseEntity<...>> para soporte WebFlux nativo en Gateway
    @GetMapping("/requests")
    public Mono<ResponseEntity<List<Object>>> getRequestsFallback() {
        return Mono.just(ResponseEntity.ok(Collections.emptyList()));
    }

    @PostMapping("/requests")
    public Mono<ResponseEntity<Map<String, String>>> postRequestsFallback() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "CIRCUIT_BREAKER_OPEN");
        response.put("message", "El servicio de solicitudes no está disponible temporalmente. Sistema operando en modo de contingencia.");
        
        return Mono.just(ResponseEntity.ok(response));
    }
}