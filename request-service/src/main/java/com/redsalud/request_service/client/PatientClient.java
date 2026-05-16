package com.redsalud.request_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// El nombre debe coincidir con el nombre del microservicio
@FeignClient(name = "patient-service", url = "http://localhost:8081")
public interface PatientClient {

    // IMPORTANTE: Debe incluir /api/patients/ porque así lo definiste en el Controller
    @GetMapping("/api/patients/{id}")
    Object getPatientById(@PathVariable("id") Long id);
}