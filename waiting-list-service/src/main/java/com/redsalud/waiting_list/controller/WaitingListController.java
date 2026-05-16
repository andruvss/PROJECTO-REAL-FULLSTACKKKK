package com.redsalud.waiting_list.controller;

import com.redsalud.waiting_list.model.WaitingList;
import com.redsalud.waiting_list.service.WaitingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/waiting-list")
public class WaitingListController {

    @Autowired
    private WaitingListService service;

    // Recibimos los parámetros limpios desde la URL individuales para evitar conflictos de mapeo
    @PostMapping
    public ResponseEntity<WaitingList> add(
            @RequestParam Long patientId,
            @RequestParam String medicalSpecialty,
            @RequestParam Integer priority) {
        
        // Construimos la entidad de forma segura asignando únicamente lo necesario
        WaitingList entry = new WaitingList();
        entry.setPatientId(patientId);
        entry.setMedicalSpecialty(medicalSpecialty);
        entry.setPriority(priority);
        
        // El servicio lo guarda, el @PrePersist genera la fecha y la BD asigna el ID automáticamente
        WaitingList savedEntry = service.addToWaitingList(entry);
        return new ResponseEntity<>(savedEntry, HttpStatus.CREATED); // Retorna 201 Created
    }

    @GetMapping
    public ResponseEntity<List<?>> getAll(
            @RequestParam(required = false) Long id, 
            @RequestParam(required = false) String filter) {
        
        return ResponseEntity.ok(service.getAllEntries(id, filter));    
    }
}