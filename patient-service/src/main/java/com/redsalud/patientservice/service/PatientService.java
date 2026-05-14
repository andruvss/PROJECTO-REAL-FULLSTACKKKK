package com.redsalud.patientservice.service;

import com.redsalud.patientservice.model.Patient;
import com.redsalud.patientservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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