package com.redsalud.patientservice;

import com.redsalud.patientservice.model.Patient;
import com.redsalud.patientservice.repository.PatientRepository;
import com.redsalud.patientservice.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PatientServiceTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private PatientService patientService;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @BeforeEach
    void setUp() {
        patientRepository.deleteAll();
    }
    
    // ===== TESTS SERVICE =====
    
    @Test
    void testCreatePatient_Success() {
        Patient patient = createPatient("Juan", "Pérez", "12345678");
        Patient created = patientService.createPatient(patient);
        
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("Juan", created.getFirstName());
        assertEquals(1, patientRepository.count());
    }
    
    @Test
    void testCreatePatient_InvalidFirstName() {
        Patient patient = new Patient();
        patient.setFirstName("");
        patient.setLastName("Pérez");
        patient.setRut("12345678");
        
        assertThrows(IllegalArgumentException.class, () -> patientService.createPatient(patient));
    }
    
    @Test
    void testGetPatientById_Success() {
        Patient saved = patientRepository.save(createPatient("María", "García", "87654321"));
        var result = patientService.getPatientById(saved.getId());
        
        assertTrue(result.isPresent());
        assertEquals("María", result.get().getFirstName());
    }
    
    @Test
    void testGetPatientById_NotFound() {
        var result = patientService.getPatientById(999L);
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testGetAllPatients() {
        patientRepository.save(createPatient("Carlos", "López", "11223344"));
        patientRepository.save(createPatient("Ana", "Martínez", "55667788"));
        
        var patients = patientService.getAllPatients();
        assertEquals(2, patients.size());
    }
    
    @Test
    void testUpdatePatient_Success() {
        Patient saved = patientRepository.save(createPatient("Diego", "Sánchez", "99887766"));
        Patient update = new Patient();
        update.setFirstName("Diego Updated");
        
        Patient updated = patientService.updatePatient(saved.getId(), update);
        
        assertEquals("Diego Updated", updated.getFirstName());
        assertEquals("Sánchez", updated.getLastName()); // No cambió
    }
    
    @Test
    void testDeletePatient_Success() {
        Patient saved = patientRepository.save(createPatient("Elena", "Rodríguez", "44556677"));
        patientService.deletePatient(saved.getId());
        
        assertEquals(0, patientRepository.count());
    }
    
    // ===== TESTS CONTROLLER (HTTP) =====
    
    @Test
    void testGetAllPatients_HTTP() throws Exception {
        mockMvc.perform(get("/api/patients"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    
    @Test
    void testCreatePatient_HTTP() throws Exception {
        String json = "{\"firstName\":\"Test\",\"lastName\":\"Patient\",\"rut\":\"12345678\"}";
        
        mockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isCreated());
        
        assertEquals(1, patientRepository.count());
    }
    
    @Test
    void testGetPatientById_HTTP() throws Exception {
        Patient saved = patientRepository.save(createPatient("Felipe", "Gómez", "33445566"));
        
        mockMvc.perform(get("/api/patients/" + saved.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("Felipe"));
    }
    
    @Test
    void testGetPatientById_NotFound_HTTP() throws Exception {
        mockMvc.perform(get("/api/patients/999"))
            .andExpect(status().isNotFound());
    }
    
    @Test
    void testUpdatePatient_HTTP() throws Exception {
        Patient saved = patientRepository.save(createPatient("Gloria", "López", "22334455"));
        String json = "{\"firstName\":\"Gloria Updated\"}";
        
        mockMvc.perform(put("/api/patients/" + saved.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("Gloria Updated"));
    }
    
    @Test
    void testDeletePatient_HTTP() throws Exception {
        Patient saved = patientRepository.save(createPatient("Henry", "Fernández", "77889900"));
        
        mockMvc.perform(delete("/api/patients/" + saved.getId()))
            .andExpect(status().isOk());
        
        assertEquals(0, patientRepository.count());
    }
    
    @Test
    void testCreatePatient_InvalidData_HTTP() throws Exception {
        String json = "{\"firstName\":\"\",\"lastName\":\"Test\",\"rut\":\"12345678\"}";
        
        mockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest());
    }
    
    // Helper
    private Patient createPatient(String firstName, String lastName, String rut) {
        Patient p = new Patient();
        p.setFirstName(firstName);
        p.setLastName(lastName);
        p.setRut(rut);
        return p;
    }
}
