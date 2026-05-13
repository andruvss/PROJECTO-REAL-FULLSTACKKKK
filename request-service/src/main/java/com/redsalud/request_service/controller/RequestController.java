package com.redsalud.request_service.controller;

import com.redsalud.request_service.model.Request;
import com.redsalud.request_service.service.RequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    @Autowired
    private RequestService service;

    @PostMapping
    public ResponseEntity<Request> createRequest(@Valid @RequestBody Request request) {
        return ResponseEntity.ok(service.createRequest(request));
    }

    @GetMapping
    public ResponseEntity<List<Request>> getAllRequests() {
        return ResponseEntity.ok(service.getAllRequests());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Request>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(service.getRequestsByPatient(patientId));
    }
}