package org.example.devsync1.services;

import org.example.devsync1.entities.Token;
import org.example.devsync1.repositories.implementations.TokenRepository;

import java.util.List;
import java.util.Optional;

public class TokenService {
    private final TokenRepository tokenRepository;
    public TokenService() {
        tokenRepository = new TokenRepository();
    }
    public Token save(Token token) {
        return tokenRepository.save(token);
    }
    public List<Token> findAll() {
        return tokenRepository.findAll();
    }
    public List<Token> findTokensById(Long id) {
        return tokenRepository.findTokenByUserId(id);
    }
    public Optional<Token> findById(Long id) {
        return tokenRepository.findById(id);
    }
    public Token update(Token token) {
        return tokenRepository.update(token);
    }
    public boolean delete(Token token) {
        return tokenRepository.delete(token);
    }
    public Optional<Token> findByUserId(Long id) {
        return tokenRepository.findByUserId(id);
    }
}
