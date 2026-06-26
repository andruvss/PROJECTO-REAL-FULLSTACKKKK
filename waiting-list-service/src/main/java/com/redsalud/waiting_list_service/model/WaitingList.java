package com.redsalud.waiting_list_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "waiting_list")
@Data
@NoArgsConstructor
@Schema(description = "Entrada en la lista de espera")
public class WaitingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "ID del paciente", example = "123")
    private Long patientId;

    @Schema(description = "Especialidad médica", example = "Cardiology")
    private String medicalSpecialty;

    @Schema(description = "Prioridad de la atención", example = "HIGH")
    private String priority; // HIGH, MEDIUM, LOW

    @Schema(description = "Fecha de ingreso en la lista de espera", hidden = true)
    private LocalDateTime entryDate;

    @PrePersist
    protected void onCreate() {
        this.entryDate = LocalDateTime.now();
    }
}
