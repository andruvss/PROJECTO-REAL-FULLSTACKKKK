package com.redsalud.request_service.service;

import com.redsalud.request_service.model.Request;
import com.redsalud.request_service.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RequestService {
    
    @Autowired
    private RequestRepository requestRepository;
    
    public Request createRequest(Request request) {
        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }
        if (request.getStatus() == null || request.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Status is required");
        }
        return requestRepository.save(request);
    }
    
    public Optional<Request> getRequestById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Request ID must be positive");
        }
        return requestRepository.findById(id);
    }
    
    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }
    
    public Request updateRequest(Long id, Request requestDetails) {
        Request request = requestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Request not found with id: " + id));
        
        if (requestDetails.getDescription() != null && !requestDetails.getDescription().trim().isEmpty()) {
            request.setDescription(requestDetails.getDescription());
        }
        if (requestDetails.getStatus() != null && !requestDetails.getStatus().trim().isEmpty()) {
            request.setStatus(requestDetails.getStatus());
        }
        return requestRepository.save(request);
    }
    
    public void deleteRequest(Long id) {
        if (!requestRepository.existsById(id)) {
            throw new RuntimeException("Request not found with id: " + id);
        }
        requestRepository.deleteById(id);
    }
}
