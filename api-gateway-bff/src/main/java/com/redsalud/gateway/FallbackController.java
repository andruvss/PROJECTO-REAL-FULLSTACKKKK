package com.redsalud.gateway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    // 1. Cuando el frontend pide la lista de solicitudes (GET) y el servicio está caído
    @GetMapping("/requests")
    public ResponseEntity<List<Object>> getRequestsFallback() {
        // Devolvemos una lista vacía de inmediato. 
        // Así la tabla central se queda limpia en vez de romper la pantalla.
        return ResponseEntity.ok(Collections.emptyList());
    }

    // 2. Cuando el frontend intenta CREAR una solicitud (POST) y el servicio está caído
    @PostMapping("/requests")
    public ResponseEntity<Map<String, String>> postRequestsFallback() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "CIRCUIT_BREAKER_OPEN");
        response.put("message", "El servicio de solicitudes no está disponible temporalmente. Sistema operando en modo de contingencia.");
        
        // Devolvemos una estructura JSON amigable que el frontend sepa leer
        return ResponseEntity.ok(response);
    }
}