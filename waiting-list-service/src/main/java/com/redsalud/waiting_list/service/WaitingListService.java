package com.redsalud.waiting_list.service;

import com.redsalud.waiting_list.model.WaitingList;
import com.redsalud.waiting_list.repository.WaitingListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaitingListService {

    private static final Logger logger = LoggerFactory.getLogger(WaitingListService.class);

    @Autowired
    private WaitingListRepository repository;

    public List<WaitingList> getAllEntries(Long patientId, String medicalSpecialty) {
        logger.info("Consultando entradas de la lista de espera con filtros patientId={} medicalSpecialty={}", patientId, medicalSpecialty);
        try {
            if (repository == null) {
                logger.error("¡ERROR CRÍTICO!: El objeto repository es NULL. Falta la inyección de dependencias.");
                throw new NullPointerException("El repositorio no fue inyectado correctamente.");
            }
            if (patientId != null && medicalSpecialty != null) {
                return repository.findByPatientIdAndMedicalSpecialty(patientId, medicalSpecialty);
            }
            if (patientId != null) {
                return repository.findByPatientId(patientId);
            }
            if (medicalSpecialty != null) {
                return repository.findByMedicalSpecialty(medicalSpecialty);
            }
            return repository.findAll();
        } catch (Exception e) {
            logger.error("Error interno en la base de datos al listar entradas: {}", e.getMessage(), e);
            throw new RuntimeException("Fallo en el servicio de lista de espera: " + e.getMessage());
        }
    }

    public WaitingList addToWaitingList(WaitingList entry) {
        logger.info("Intentando agregar un nuevo paciente a la lista de espera");
        try {
            return repository.save(entry);
        } catch (Exception e) {
            logger.error("Error al guardar en la lista de espera: {}", e.getMessage());
            throw new RuntimeException("No se pudo guardar la entrada: " + e.getMessage());
        }
    }
}