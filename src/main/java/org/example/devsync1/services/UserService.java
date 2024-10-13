package org.example.devsync1.services;

import org.example.devsync1.entities.User;
import org.example.devsync1.enums.Role;
import org.example.devsync1.repositories.implementations.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserService {
    UserRepository userRepository;
    public UserService() {
        this.userRepository = new UserRepository();
    }
    public void save(User user) {
       userRepository.save(user);
    }
    public User findById(long id) {
        return userRepository.findById(id);
    }
    public List<User> findAll() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRole().equals(Role.USER))
                .collect(Collectors.toList());
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public void update(User user) {
        User userfound = this.findById(user.getId());
        if (userfound != null) {
            userRepository.delete(user);
        }else {
            System.out.println("User not found");
        }
    }
    public void delete(User user) {
        User userfound = this.findById(user.getId());
        if (userfound != null) {
            userRepository.delete(user);
        }else {
            System.out.println("User not found");
        }
    }
}
