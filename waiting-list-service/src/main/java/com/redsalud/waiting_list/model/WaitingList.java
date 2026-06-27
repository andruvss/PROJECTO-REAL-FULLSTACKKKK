package com.redsalud.waiting_list.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "waiting_list")
@Schema(description = "Entrada en la lista de espera")
public class WaitingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "ID del paciente", example = "123")
    private Long patientId;

    @Schema(description = "Especialidad médica", example = "Cardiology")
    private String medicalSpecialty;

    @Schema(description = "Prioridad de la atención", example = "1")
    private Integer priority; // 1: Alta, 2: Media, 3: Baja

    @Schema(description = "Fecha de ingreso en la lista de espera", hidden = true)
    private LocalDateTime entryDate;

    // Constructor vacío obligatorio para Hibernate
    public WaitingList() {}

    @PrePersist
    protected void onCreate() {
        this.entryDate = LocalDateTime.now();
    }

    // Getters
    public Long getId() { return id; }
    public Long getPatientId() { return patientId; }
    public String getMedicalSpecialty() { return medicalSpecialty; }
    public Integer getPriority() { return priority; }
    public LocalDateTime getEntryDate() { return entryDate; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public void setMedicalSpecialty(String medicalSpecialty) { this.medicalSpecialty = medicalSpecialty; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public void setEntryDate(LocalDateTime entryDate) { this.entryDate = entryDate; }
}