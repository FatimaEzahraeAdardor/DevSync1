package org.example.devsync1.services;

import org.example.devsync1.entities.Task;
import org.example.devsync1.enums.Role;
import org.example.devsync1.repositories.implementations.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;

public class TaskService {
    TaskRepository taskRepository;
    public TaskService() {
        this.taskRepository = new TaskRepository();
    }
    public void save(Task task) {
        taskRepository.save(task);
    }
    public Task findById(Long id) {
        return taskRepository.findById(id);
    }
    public List<Task> findAll() {
        return taskRepository.findAll()
                .stream()
                .filter(task ->task.getUser().getRole().equals(Role.MANAGER))
                .collect(Collectors.toList());
    }
    public List<Task> findByUserId(Long id) {
        return taskRepository.findByUserId(id);
    }
    public Task update(Task task) {
        return taskRepository.update(task);
    }
    public void updateTaskStatus(Long taskId, String newStatus){
        Task task = this.findById(taskId);
        if (task != null) {
            task.setStatus(newStatus);
            this.update(task);
    }
}
    public void delete(Task task) {
        taskRepository.delete(task);
    }
}
