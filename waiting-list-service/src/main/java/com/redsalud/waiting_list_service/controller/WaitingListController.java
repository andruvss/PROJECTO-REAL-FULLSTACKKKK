package com.redsalud.waiting_list_service.controller;

import com.redsalud.waiting_list_service.model.WaitingList;
import com.redsalud.waiting_list_service.service.WaitingListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/waiting-lists")
@Tag(name = "Waiting List", description = "Gestión de la lista de espera")
public class WaitingListController {

    private final WaitingListService service;

    public WaitingListController(WaitingListService service) {
        this.service = service;
    }

    @Operation(summary = "Obtener todas las entradas")
    @GetMapping
    public ResponseEntity<List<WaitingList>> getAll() {
        return ResponseEntity.ok(service.getAllWaitingLists());
    }

    @Operation(summary = "Obtener entrada por ID")
    @GetMapping("/{id}")
    public ResponseEntity<WaitingList> getById(@PathVariable Long id) {
        return service.getWaitingListById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Agregar a la lista de espera")
    @PostMapping
    public ResponseEntity<WaitingList> create(@RequestBody WaitingList w) {
        try {
            return new ResponseEntity<>(service.createWaitingList(w), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar entrada")
    @PutMapping("/{id}")
    public ResponseEntity<WaitingList> update(@PathVariable Long id, @RequestBody WaitingList details) {
        try {
            return ResponseEntity.ok(service.updateWaitingList(id, details));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar de la lista de espera")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.deleteWaitingList(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
