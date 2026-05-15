package com.redsalud.waiting_list.controller;

import com.redsalud.waiting_list.model.WaitingList;
import com.redsalud.waiting_list.service.WaitingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/waiting-list")
public class WaitingListController {

    @Autowired
    private WaitingListService service;

    @PostMapping
    public ResponseEntity<WaitingList> add(@RequestBody WaitingList entry) {
        return ResponseEntity.ok(service.addToWaitingList(entry));
    }

    @GetMapping
    public ResponseEntity<List<WaitingList>> getAll() {
        return ResponseEntity.ok(service.getAllEntries());
    }
}