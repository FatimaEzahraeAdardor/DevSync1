package org.example.devsync1.repositories.interfaces;

import org.example.devsync1.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserInterface {
    User save(User user);

    Optional<User> findById(Long id);

    List<User> findAll();
    Optional<User> findByEmail(String email);

    User update(User user);

    Boolean delete(User user);
}
