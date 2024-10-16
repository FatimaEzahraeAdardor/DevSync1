package org.example.devsync1.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "tasks")
@Getter
@Setter
@EqualsAndHashCode
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "task_tags",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    @ManyToOne
    @JoinColumn(name = "assigned_to_user_id", nullable = false)
    private User assignedTo;
    private String title;
    private String description;
    private String status;
    private LocalDate creationDate;
    private LocalDate dueDate;
    private Boolean isChanged;

    public Task(String title, String description, LocalDate creationDate, LocalDate dueDate, String status, List<Tag> tags, User user, User assignedTo,  boolean isChanged) {
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        this.dueDate = dueDate;
        this.status = status;
        this.tags = tags;
        this.user = user;
        this.assignedTo = assignedTo;
        this.isChanged = isChanged;
    }

    public Task() {
    }
}
