package com.redsalud.request_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del paciente es obligatorio")
    private Long patientId; // [cite: 65]

    @NotBlank(message = "La especialidad médica es obligatoria")
    private String medicalSpecialty; // [cite: 66]

    @NotNull
    private LocalDateTime registrationDate; // [cite: 67]

    @NotBlank
    private String status; // Usamos String: REGISTERED, IN_WAITING_LIST, etc. [cite: 68, 70]
    
    private String description;

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    @PrePersist
    protected void onCreate() {
        this.registrationDate = LocalDateTime.now(); // [cite: 67]
        if (this.status == null) {
            this.status = "REGISTERED"; // Estado inicial por defecto [cite: 70]
        }
    }
}