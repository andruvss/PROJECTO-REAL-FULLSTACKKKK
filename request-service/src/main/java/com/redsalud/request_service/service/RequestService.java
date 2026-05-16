package com.redsalud.request_service.service;

import com.redsalud.request_service.client.PatientClient;
import com.redsalud.request_service.model.Request;
import com.redsalud.request_service.repository.RequestRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {

    // Creamos el Logger para esta clase
    private static final Logger logger = LoggerFactory.getLogger(RequestService.class);

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private PatientClient patientClient;

    public Request createRequest(Request request) {
        // Iniciamos el cronómetro
        long startTime = System.currentTimeMillis();
        
        logger.info("Iniciando creación de solicitud para el paciente ID: {}", request.getPatientId());

        try {
            // Llamada al microservicio de pacientes (Puerto 8081)
            patientClient.getPatientById(request.getPatientId());
            
            logger.info("Validación exitosa: El paciente existe.");

        } catch (FeignException.NotFound ex) {
            long duration = System.currentTimeMillis() - startTime;
            logger.error("Fallo en la creación: Paciente {} no encontrado. Tiempo de respuesta: {} ms", 
                         request.getPatientId(), duration);
            throw new IllegalArgumentException("No se puede crear la solicitud: Paciente no encontrado");
        } catch (Exception ex) {
            logger.error("Error de comunicación con patient-service: {}", ex.getMessage());
            throw new RuntimeException("Error técnico al validar el paciente");
        }

        // Si todo sale bien, guardamos
        Request savedRequest = requestRepository.save(request);
        
        // Calculamos el tiempo total final
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Solicitud guardada con éxito (ID: {}). Tiempo total del proceso: {} ms", 
                    savedRequest.getId(), duration);
        
        return savedRequest;
    }

    public List<Request> getAllRequests() {
        logger.info("Consultando todas las solicitudes registradas");
        return requestRepository.findAll();
    }

    public List<Request> getRequestsByPatient(Long patientId) {
        logger.info("Buscando solicitudes para el paciente ID: {}", patientId);
        return requestRepository.findByPatientId(patientId);
    }
}