package org.example.devsync1.repositories.interfaces;

import org.example.devsync1.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserInterface {
    void save(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    void update(User user);

    void delete(User user);
}
