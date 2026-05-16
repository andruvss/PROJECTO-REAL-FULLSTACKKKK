package com.redsalud.waiting_list.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "waiting_list")
@Data
@NoArgsConstructor // Constructor vacío que Hibernate necesita obligatoriamente
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

    @PrePersist
    protected void onCreate() {
        this.entryDate = LocalDateTime.now();
    }
}