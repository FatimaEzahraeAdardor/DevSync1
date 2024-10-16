package org.example.devsync1.services;

import org.example.devsync1.entities.Request;
import org.example.devsync1.repositories.implementations.RequestRepository;

import java.util.List;
import java.util.Optional;

public class RequestService {
    private RequestRepository requestRepository;
    public RequestService() {
        this.requestRepository = new RequestRepository();
    }
    public Request save(Request request) {
        return requestRepository.save(request);
    }
    public Optional<Request> findById(Long id) {
        return requestRepository.findById(id);
    }
    public Request update(Request request) {
        return requestRepository.update(request);
    }
    public Optional<Request> findByTaskId(Long id) {
        return requestRepository.findByTaskId(id);
    }
    public List<Request> findByUserId(Long userId) {
        return requestRepository.findByUserId(userId);
    }

}
