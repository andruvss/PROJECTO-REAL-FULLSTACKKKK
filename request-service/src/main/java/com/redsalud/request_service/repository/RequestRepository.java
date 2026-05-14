package com.redsalud.request_service.repository;

import com.redsalud.request_service.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    // Método para buscar solicitudes por paciente, útil para el alcance [cite: 27]
    List<Request> findByPatientId(Long patientId);
}