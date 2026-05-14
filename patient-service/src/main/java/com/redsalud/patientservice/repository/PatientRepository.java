package com.redsalud.patientservice.repository;

import com.redsalud.patientservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    // Esto nos permitirá buscar pacientes por RUT más adelante
    Optional<Patient> findByRut(String rut);
}