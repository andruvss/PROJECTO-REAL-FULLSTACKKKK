package com.redsalud.waiting_list_service;

import com.redsalud.waiting_list.WaitingListServiceApplication;
import com.redsalud.waiting_list.model.WaitingList;
import com.redsalud.waiting_list.repository.WaitingListRepository;
import com.redsalud.waiting_list.service.WaitingListService;
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

@SpringBootTest(classes = WaitingListServiceApplication.class)
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

    private WaitingList buildEntry(Long patientId, String specialty, Integer priority) {
        WaitingList w = new WaitingList();
        w.setPatientId(patientId);
        w.setMedicalSpecialty(specialty);
        w.setPriority(priority);
        return w;
    }

    @Test
    void testAddToWaitingList_Success() {
        WaitingList created = service.addToWaitingList(buildEntry(1L, "Cardiology", 1));
        assertNotNull(created.getId());
        assertEquals(1, repo.count());
    }

    @Test
    void testGetAllEntries_NoFilter() {
        repo.save(buildEntry(1L, "Cardiology", 1));
        repo.save(buildEntry(2L, "Neurology", 2));
        assertEquals(2, service.getAllEntries(null, null).size());
    }

    @Test
    void testGetAllEntries_FilterByPatientId() {
        repo.save(buildEntry(1L, "Cardiology", 1));
        repo.save(buildEntry(2L, "Neurology", 2));
        assertEquals(1, service.getAllEntries(1L, null).size());
    }

    @Test
    void testGetAllEntries_FilterBySpecialty() {
        repo.save(buildEntry(1L, "Cardiology", 1));
        repo.save(buildEntry(2L, "Neurology", 2));
        assertEquals(1, service.getAllEntries(null, "Cardiology").size());
    }

    @Test
    void testGetAllEntries_FilterByBoth() {
        repo.save(buildEntry(1L, "Cardiology", 1));
        repo.save(buildEntry(1L, "Neurology", 2));
        assertEquals(1, service.getAllEntries(1L, "Cardiology").size());
    }

    @Test
    void testGetAll_HTTP() throws Exception {
        mockMvc.perform(get("/api/waiting-list"))
            .andExpect(status().isOk());
    }

    @Test
    void testCreate_HTTP() throws Exception {
        mockMvc.perform(post("/api/waiting-list")
            .param("patientId", "1")
            .param("medicalSpecialty", "Cardiology")
            .param("priority", "1"))
            .andExpect(status().isCreated());
        assertEquals(1, repo.count());
    }

    @Test
    void testGetAll_HTTP_WithPatientFilter() throws Exception {
        repo.save(buildEntry(1L, "Cardiology", 1));
        mockMvc.perform(get("/api/waiting-list").param("id", "1"))
            .andExpect(status().isOk());
    }
}