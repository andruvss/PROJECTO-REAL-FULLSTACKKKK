package com.redsalud.waiting_list_service.repository;

import com.redsalud.waiting_list_service.model.WaitingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaitingListRepository extends JpaRepository<WaitingList, Long> {
    List<WaitingList> findByPatientId(Long patientId);
    List<WaitingList> findByMedicalSpecialty(String medicalSpecialty);
    List<WaitingList> findByPatientIdAndMedicalSpecialty(Long patientId, String medicalSpecialty);
}
