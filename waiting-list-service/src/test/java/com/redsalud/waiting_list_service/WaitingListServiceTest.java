package com.redsalud.waiting_list_service;

import com.redsalud.waiting_list_service.model.WaitingList;
import com.redsalud.waiting_list_service.repository.WaitingListRepository;
import com.redsalud.waiting_list_service.service.WaitingListService;
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
class WaitingListServiceTest {
    
    @Autowired private MockMvc mockMvc;
    @Autowired private WaitingListService service;
    @Autowired private WaitingListRepository repo;
    
    @BeforeEach
    void setUp() {
        repo.deleteAll();
    }
    
    private WaitingList createWaitingList(Long patientId, String priority) {
        WaitingList w = new WaitingList();
        w.setPatientId(patientId);
        w.setPriority(priority);
        return w;
    }
    
    @Test
    void testCreateWaitingList_Success() {
        WaitingList w = createWaitingList(1L, "HIGH");
        WaitingList created = service.createWaitingList(w);
        assertNotNull(created.getId());
        assertEquals(1, repo.count());
    }
    
    @Test
    void testGetWaitingListById_Success() {
        WaitingList saved = repo.save(createWaitingList(1L, "MEDIUM"));
        var result = service.getWaitingListById(saved.getId());
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getPatientId());
    }
    
    @Test
    void testGetAllWaitingLists() {
        repo.save(createWaitingList(1L, "HIGH"));
        repo.save(createWaitingList(2L, "LOW"));
        assertEquals(2, service.getAllWaitingLists().size());
    }
    
    @Test
    void testUpdateWaitingList_Success() {
        WaitingList saved = repo.save(createWaitingList(1L, "HIGH"));
        WaitingList update = new WaitingList();
        update.setPriority("LOW");
        WaitingList updated = service.updateWaitingList(saved.getId(), update);
        assertEquals("LOW", updated.getPriority());
    }
    
    @Test
    void testDeleteWaitingList_Success() {
        WaitingList saved = repo.save(createWaitingList(1L, "MEDIUM"));
        service.deleteWaitingList(saved.getId());
        assertEquals(0, repo.count());
    }
    
    @Test
    void testGetAll_HTTP() throws Exception {
        mockMvc.perform(get("/api/waiting-lists"))
            .andExpect(status().isOk());
    }
    
    @Test
    void testCreate_HTTP() throws Exception {
        String json = "{\"patientId\":1,\"priority\":\"HIGH\"}";
        mockMvc.perform(post("/api/waiting-lists")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isCreated());
        assertEquals(1, repo.count());
    }
    
    @Test
    void testUpdate_HTTP() throws Exception {
        WaitingList saved = repo.save(createWaitingList(1L, "HIGH"));
        String json = "{\"priority\":\"LOW\"}";
        mockMvc.perform(put("/api/waiting-lists/" + saved.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk());
    }
    
    @Test
    void testDelete_HTTP() throws Exception {
        WaitingList saved = repo.save(createWaitingList(1L, "MEDIUM"));
        mockMvc.perform(delete("/api/waiting-lists/" + saved.getId()))
            .andExpect(status().isOk());
        assertEquals(0, repo.count());
    }
    
    @Test
    void testGetById_HTTP() throws Exception {
        WaitingList saved = repo.save(createWaitingList(1L, "HIGH"));
        mockMvc.perform(get("/api/waiting-lists/" + saved.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.patientId").value(1));
    }
}
