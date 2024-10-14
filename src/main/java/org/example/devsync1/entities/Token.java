package org.example.devsync1.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Entity
@Table(name = "tokens")
@Data
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "modify_token_count", nullable = false)
    private int modifyTokenCount;

    @Column(name = "delete_token_count", nullable = false)
    private int deleteTokenCount;

    public Token(User user, int modifyTokenCount, int deleteTokenCount) {
        this.user = user;
        this.modifyTokenCount = modifyTokenCount;
        this.deleteTokenCount = deleteTokenCount;
    }

    public Token(User user) {
        this(user, 2, 1);
    }

    public Token(User user, int modifyTokenCount, int deleteTokenCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.user = user;
        this.modifyTokenCount = modifyTokenCount;
        this.deleteTokenCount = deleteTokenCount;
    }

    public Token() {

    }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", modifyTokenCount=" + modifyTokenCount +
                ", deleteTokenCount=" + deleteTokenCount +
                '}';
    }
}