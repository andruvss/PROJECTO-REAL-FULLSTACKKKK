package com.redsalud.patientservice.service;

import com.redsalud.patientservice.model.Patient;
import com.redsalud.patientservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
<<<<<<< HEAD
import org.springframework.transaction.annotation.Transactional;
=======
>>>>>>> 69f52dd17fdeeac74ce9e6b427a39624c6af3909

import java.util.List;
import java.util.Optional;

@Service
<<<<<<< HEAD
@Transactional
public class PatientService {
    
    @Autowired
    private PatientRepository patientRepository;
    
    /**
     * Crear nuevo paciente
     */
    public Patient createPatient(Patient patient) {
        if (patient.getFirstName() == null || patient.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (patient.getLastName() == null || patient.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (patient.getRut() == null || patient.getRut().trim().isEmpty()) {
            throw new IllegalArgumentException("RUT is required");
        }
        return patientRepository.save(patient);
    }
    
    /**
     * Obtener paciente por ID
     */
    public Optional<Patient> getPatientById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Patient ID must be positive");
        }
        return patientRepository.findById(id);
    }
    
    /**
     * Obtener todos los pacientes
     */
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
    
    /**
     * Actualizar paciente
     */
    public Patient updatePatient(Long id, Patient patientDetails) {
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
        
        if (patientDetails.getFirstName() != null && !patientDetails.getFirstName().trim().isEmpty()) {
            patient.setFirstName(patientDetails.getFirstName());
        }
        if (patientDetails.getLastName() != null && !patientDetails.getLastName().trim().isEmpty()) {
            patient.setLastName(patientDetails.getLastName());
        }
        if (patientDetails.getRut() != null && !patientDetails.getRut().trim().isEmpty()) {
            patient.setRut(patientDetails.getRut());
        }
        return patientRepository.save(patient);
    }
    
    /**
     * Eliminar paciente
     */
    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new RuntimeException("Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);
    }
}
=======
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    // ESTE ES EL QUE NECESITA EL CONTROLLER
    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    // ESTE TAMBIÉN LO BUSCA EL CONTROLLER
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    public Patient updatePatient(Long id, Patient details) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        
        patient.setFirstName(details.getFirstName());
        patient.setLastName(details.getLastName());
        patient.setEmail(details.getEmail());
        patient.setPhone(details.getPhone());
        
        return patientRepository.save(patient);
    }
}
>>>>>>> 69f52dd17fdeeac74ce9e6b427a39624c6af3909
