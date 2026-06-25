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

    @Test
    void testCreate() {
        WaitingList w = new WaitingList();
        w.setPatientId(1L);
        w.setPriority("HIGH");
        WaitingList created = service.createWaitingList(w);
        assertNotNull(created.getId());
        assertEquals(1, repo.count());
    }

    @Test
    void testGetById() {
        WaitingList w = new WaitingList();
        w.setPatientId(1L);
        WaitingList saved = repo.save(w);
        var result = service.getWaitingListById(saved.getId());
        assertTrue(result.isPresent());
    }

    @Test
    void testGetAll() {
        WaitingList w1 = new WaitingList();
        w1.setPatientId(1L);
        WaitingList w2 = new WaitingList();
        w2.setPatientId(2L);
        repo.save(w1);
        repo.save(w2);
        assertEquals(2, service.getAllWaitingLists().size());
    }

    @Test
    void testUpdate() {
        WaitingList w = new WaitingList();
        w.setPatientId(1L);
        w.setPriority("LOW");
        WaitingList saved = repo.save(w);
        WaitingList update = new WaitingList();
        update.setPriority("HIGH");
        WaitingList updated = service.updateWaitingList(saved.getId(), update);
        assertEquals("HIGH", updated.getPriority());
    }

    @Test
    void testDelete() {
        WaitingList w = new WaitingList();
        w.setPatientId(1L);
        WaitingList saved = repo.save(w);
        service.deleteWaitingList(saved.getId());
        assertEquals(0, repo.count());
    }

    @Test
    void testCreateInvalidPatientId() {
        WaitingList w = new WaitingList();
        w.setPatientId(0L);
        assertThrows(IllegalArgumentException.class, () -> service.createWaitingList(w));
    }

    @Test
    void testGetByIdInvalid() {
        assertThrows(IllegalArgumentException.class, () -> service.getWaitingListById(-1L));
    }

    @Test
    void testUpdateNotFound() {
        WaitingList update = new WaitingList();
        update.setPriority("HIGH");
        assertThrows(RuntimeException.class, () -> service.updateWaitingList(9999L, update));
    }

    @Test
    void testDeleteNotFound() {
        assertThrows(RuntimeException.class, () -> service.deleteWaitingList(9999L));
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
    }

    @Test
    void testCreate_HTTP_BadRequest() throws Exception {
        String json = "{\"patientId\":0}";
        mockMvc.perform(post("/api/waiting-lists")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testGetById_HTTP_NotFound() throws Exception {
        mockMvc.perform(get("/api/waiting-lists/9999"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testUpdate_HTTP() throws Exception {
        WaitingList w = new WaitingList();
        w.setPatientId(1L);
        WaitingList saved = repo.save(w);
        String json = "{\"priority\":\"MEDIUM\"}";
        mockMvc.perform(put("/api/waiting-lists/" + saved.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk());
    }

    @Test
    void testUpdate_HTTP_NotFound() throws Exception {
        String json = "{\"priority\":\"MEDIUM\"}";
        mockMvc.perform(put("/api/waiting-lists/9999")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isNotFound());
    }

    @Test
    void testDelete_HTTP() throws Exception {
        WaitingList w = new WaitingList();
        w.setPatientId(1L);
        WaitingList saved = repo.save(w);
        mockMvc.perform(delete("/api/waiting-lists/" + saved.getId()))
            .andExpect(status().isOk());
    }

    @Test
    void testDelete_HTTP_NotFound() throws Exception {
        mockMvc.perform(delete("/api/waiting-lists/9999"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testGetById_HTTP_OK() throws Exception {
        WaitingList w = new WaitingList();
        w.setPatientId(5L);
        WaitingList saved = repo.save(w);
        mockMvc.perform(get("/api/waiting-lists/" + saved.getId()))
            .andExpect(status().isOk());
    }
}
