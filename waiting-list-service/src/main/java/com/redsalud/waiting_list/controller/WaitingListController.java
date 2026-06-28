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

    // Recibe JSON body desde el frontend React
    @PostMapping
    public ResponseEntity<WaitingList> add(@RequestBody WaitingList entry) {
        WaitingList savedEntry = service.addToWaitingList(entry);
        return new ResponseEntity<>(savedEntry, HttpStatus.CREATED);
    }

    // Filtros opcionales por patientId y medicalSpecialty
    @GetMapping
    public ResponseEntity<List<?>> getAll(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) String medicalSpecialty) {

        return ResponseEntity.ok(service.getAllEntries(patientId, medicalSpecialty));
    }
}