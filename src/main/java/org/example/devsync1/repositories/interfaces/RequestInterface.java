package org.example.devsync1.repositories.interfaces;

import org.example.devsync1.entities.Request;
import org.example.devsync1.enums.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface RequestInterface {
     Request save(Request request);
      Request update(Request request);
     Optional<Request> findById(Long id) ;
    Optional<Request> findByTaskId(Long taskId);
     List<Request> findByUserId(Long userId);
}



