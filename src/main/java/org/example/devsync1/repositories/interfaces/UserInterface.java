package org.example.devsync1.repositories.interfaces;

import org.example.devsync1.entities.User;

import java.util.List;

public interface UserInterface {
    void save(User user);

    User findById(Long id);

    List<User> findAll();
    User findByEmail(String email);

    void update(User user);

    void delete(User user);
}
