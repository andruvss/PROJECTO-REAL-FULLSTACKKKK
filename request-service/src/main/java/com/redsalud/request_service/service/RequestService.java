package com.redsalud.request_service.service;

import com.redsalud.request_service.client.PatientClient;
import com.redsalud.request_service.model.Request;
import com.redsalud.request_service.repository.RequestRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private PatientClient patientClient;

    public Request createRequest(Request request) {
        try {
            patientClient.getPatientById(request.getPatientId());
        } catch (FeignException.NotFound ex) {
            throw new IllegalArgumentException("Paciente no encontrado");
        }
        return requestRepository.save(request);
    }

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    public List<Request> getRequestsByPatient(Long patientId) {
        return requestRepository.findByPatientId(patientId);
    }
}
