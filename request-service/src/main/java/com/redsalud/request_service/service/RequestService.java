package com.redsalud.request_service.service;

import com.redsalud.request_service.model.Request;
import com.redsalud.request_service.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {

    private final RequestRepository repository;

    @Autowired
    public RequestService(RequestRepository repository) {
        this.repository = repository;
    }

    public Request createRequest(Request request) {
        return repository.save(request);
    }

    public List<Request> getAllRequests() {
        return repository.findAll();
    }

    public List<Request> getRequestsByPatient(Long patientId) {
        return repository.findByPatientId(patientId);
    }
}
