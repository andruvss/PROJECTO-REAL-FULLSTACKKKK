package com.redsalud.request_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Este es tu "Adapter" para comunicarte con el MS de Pacientes [cite: 62]
@FeignClient(name = "patient-service", url = "localhost:8081") 
public interface PatientClient {

    @GetMapping("/api/patients/{id}")
    Object getPatientById(@PathVariable("id") Long id);
}