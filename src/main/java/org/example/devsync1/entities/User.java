package org.example.devsync1.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.example.devsync1.enums.Role;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public User(String username,String firstName, String lastName, String email,String password, Role role) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;


    @Enumerated(EnumType.STRING)
    private Role role;

    public User() {

    }
}