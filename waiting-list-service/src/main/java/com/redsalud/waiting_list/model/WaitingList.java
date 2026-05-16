package com.redsalud.waiting_list.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "waiting_list")
@Data
public class WaitingList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patientId;
    private String medicalSpecialty;
    private Integer priority; // 1: Alta, 2: Media, 3: Baja
    private LocalDateTime entryDate;

    @PrePersist
    protected void onCreate() {
        this.entryDate = LocalDateTime.now();
    }
}