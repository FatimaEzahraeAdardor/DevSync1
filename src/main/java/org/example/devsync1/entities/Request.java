package org.example.devsync1.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.example.devsync1.enums.RequestStatus;

import java.time.LocalDate;

@Entity
@Table(name = "token_requests")
@Data
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    private LocalDate creationDate;

    public Request(Task task,User user) {
        this.user = user;
        this.task = task;
        this.status = RequestStatus.PENDING;
        this.creationDate = LocalDate.now();

    }

    public Request() {

    }
}