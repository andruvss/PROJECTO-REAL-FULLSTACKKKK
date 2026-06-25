package com.redsalud.request_service;

import com.redsalud.request_service.model.Request;
import com.redsalud.request_service.repository.RequestRepository;
import com.redsalud.request_service.service.RequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RequestServiceTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private RequestService requestService;
    
    @Autowired
    private RequestRepository requestRepository;
    
    @BeforeEach
    void setUp() {
        requestRepository.deleteAll();
    }
    
    // ===== HELPER =====
    private Request createRequest(Long patientId, String specialty, String description, String status) {
        Request r = new Request();
        r.setPatientId(patientId);
        r.setMedicalSpecialty(specialty);
        r.setDescription(description);
        r.setStatus(status);
        return r;
    }
    
    // ===== SERVICE TESTS =====
    
    @Test
    void testCreateRequest_Success() {
        Request request = createRequest(1L, "Cardiología", "Consulta de chequeo", "PENDING");
        Request created = requestService.createRequest(request);
        
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals(1L, created.getPatientId());
        assertEquals("Cardiología", created.getMedicalSpecialty());
        assertEquals(1, requestRepository.count());
    }
    
    @Test
    void testGetRequestById_Success() {
        Request saved = requestRepository.save(createRequest(2L, "Oncología", "Evaluación inicial", "PENDING"));
        var result = requestService.getRequestById(saved.getId());
        
        assertTrue(result.isPresent());
        assertEquals(2L, result.get().getPatientId());
    }
    
    @Test
    void testGetAllRequests() {
        requestRepository.save(createRequest(1L, "Cardiología", "Revisión", "PENDING"));
        requestRepository.save(createRequest(2L, "Oncología", "Consulta", "PENDING"));
        
        var requests = requestService.getAllRequests();
        assertEquals(2, requests.size());
    }
    
    @Test
    void testUpdateRequest_Success() {
        Request saved = requestRepository.save(createRequest(1L, "Cardiología", "Chequeo", "PENDING"));
        Request update = new Request();
        update.setStatus("COMPLETED");
        
        Request updated = requestService.updateRequest(saved.getId(), update);
        
        assertEquals("COMPLETED", updated.getStatus());
        assertEquals(1L, updated.getPatientId());
    }
    
    @Test
    void testDeleteRequest_Success() {
        Request saved = requestRepository.save(createRequest(1L, "Neurología", "Evaluación", "PENDING"));
        requestService.deleteRequest(saved.getId());
        
        assertEquals(0, requestRepository.count());
    }
    
    // ===== HTTP TESTS =====
    
    @Test
    void testGetAllRequests_HTTP() throws Exception {
        mockMvc.perform(get("/api/requests"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    
    @Test
    void testCreateRequest_HTTP() throws Exception {
        String json = "{\"patientId\":1,\"medicalSpecialty\":\"Dermatología\",\"description\":\"Revisión de piel\",\"status\":\"PENDING\"}";
        
        mockMvc.perform(post("/api/requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isCreated());
        
        assertEquals(1, requestRepository.count());
    }
    
    @Test
    void testUpdateRequest_HTTP() throws Exception {
        Request saved = requestRepository.save(createRequest(1L, "Pediatría", "Consulta infantil", "PENDING"));
        String json = "{\"status\":\"COMPLETED\"}";
        
        mockMvc.perform(put("/api/requests/" + saved.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("COMPLETED"));
    }
    
    @Test
    void testDeleteRequest_HTTP() throws Exception {
        Request saved = requestRepository.save(createRequest(1L, "Oftalmología", "Revisión visual", "PENDING"));
        
        mockMvc.perform(delete("/api/requests/" + saved.getId()))
            .andExpect(status().isOk());
        
        assertEquals(0, requestRepository.count());
    }
    
    @Test
    void testGetRequestById_HTTP() throws Exception {
        Request saved = requestRepository.save(createRequest(1L, "Psiquiatría", "Evaluación", "PENDING"));
        
        mockMvc.perform(get("/api/requests/" + saved.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.patientId").value(1));
    }
}
