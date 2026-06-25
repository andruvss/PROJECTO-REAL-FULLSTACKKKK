package com.redsalud.waiting_list_service.service;

import com.redsalud.waiting_list_service.model.WaitingList;
import com.redsalud.waiting_list_service.repository.WaitingListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WaitingListService {

    private final WaitingListRepository waitingListRepository;

    public WaitingListService(WaitingListRepository waitingListRepository) {
        this.waitingListRepository = waitingListRepository;
    }

    public WaitingList createWaitingList(WaitingList waitingList) {
        if (waitingList.getPatientId() == null || waitingList.getPatientId() <= 0) {
            throw new IllegalArgumentException("Patient ID is required");
        }
        return waitingListRepository.save(waitingList);
    }

    public Optional<WaitingList> getWaitingListById(Long id) {
        if (id == null || id <= 0) throw new IllegalArgumentException("ID must be positive");
        return waitingListRepository.findById(id);
    }

    public List<WaitingList> getAllWaitingLists() {
        return waitingListRepository.findAll();
    }

    public WaitingList updateWaitingList(Long id, WaitingList details) {
        WaitingList w = waitingListRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Not found: " + id));
        if (details.getPatientId() != null) w.setPatientId(details.getPatientId());
        if (details.getMedicalSpecialty() != null) w.setMedicalSpecialty(details.getMedicalSpecialty());
        if (details.getPriority() != null) w.setPriority(details.getPriority());
        return waitingListRepository.save(w);
    }

    public void deleteWaitingList(Long id) {
        if (!waitingListRepository.existsById(id)) throw new RuntimeException("Not found: " + id);
        waitingListRepository.deleteById(id);
    }
}
