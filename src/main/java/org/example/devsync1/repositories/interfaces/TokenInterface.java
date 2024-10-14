package org.example.devsync1.repositories.interfaces;

import org.example.devsync1.entities.Token;
import org.example.devsync1.entities.User;

import java.util.List;
import java.util.Optional;

public interface
TokenInterface {
    Token save(Token token);
    Optional<Token> findById(Long id);
    List<Token> findAll();
    List<Token> findTokenByUserId(Long userId);
    Token update(Token token);
    Boolean delete(Token token);
    Optional<Token> findByUserId(Long id);
}
