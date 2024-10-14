package org.example.devsync1.services;

import org.example.devsync1.entities.Token;
import org.example.devsync1.entities.User;
import org.example.devsync1.enums.Role;
import org.example.devsync1.repositories.implementations.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserService {
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public UserService() {
        this.userRepository = new UserRepository();
        this.tokenService = new TokenService();
    }

    public User save(User user) throws Exception {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }

        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (optionalUser.isPresent()) {
            throw new IllegalArgumentException("User with ID " + user.getId() + " already exists.");
        }

        // Si c'est un utilisateur normal, créer des tokens
        if (user.getRole() == Role.USER) {
            Token token = new Token(user, 2, 1);
            if (user.getTokens() == null) {
                user.setTokens(new ArrayList<>());
            }
            user.getTokens().add(token);
        }

        return userRepository.save(user);
    }


    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll().stream().filter(user -> user.getRole() == Role.USER).collect(Collectors.toList());}

    public Optional<User> findByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }
        return userRepository.findByEmail(email);
    }

    public User update(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }

        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (optionalUser.isPresent()) {
            return userRepository.update(user);
        } else {
            throw new IllegalArgumentException("User with ID " + user.getId() + " not found.");
        }
    }

    public boolean delete(User user) {
        if (user==null) {
            throw new IllegalArgumentException("User cannot be null.");
        }

        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (optionalUser.isPresent()) {
            return userRepository.delete(user);
        } else {
            throw new IllegalArgumentException("User with ID " + user.getId() + " not found.");
        }

    }
}
