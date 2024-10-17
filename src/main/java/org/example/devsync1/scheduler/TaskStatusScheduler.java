package org.example.devsync1.scheduler;

import org.example.devsync1.entities.Task;
import org.example.devsync1.repositories.implementations.TagRepository;
import org.example.devsync1.repositories.implementations.TaskRepository;
import org.example.devsync1.repositories.implementations.UserRepository;
import org.example.devsync1.services.TaskService;

import java.time.LocalDate;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class TaskStatusScheduler {
    private final TaskService taskService;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public TaskStatusScheduler() {
        this.taskService = new TaskService();
    }

    public void startScheduler() {
            scheduler.scheduleAtFixedRate(this::checkAndUpdateTaskStatuses, 0, 1, TimeUnit.DAYS);
    }

    public void checkAndUpdateTaskStatuses() {
        List<Task> allTasks = taskService.findAll();
        LocalDate today = LocalDate.now();
        for (Task task : allTasks) {
            try {
                if (task.getDueDate().isBefore(today) && !"Completed".equalsIgnoreCase(task.getStatus())) {
                    taskService.updateTaskStatus(task.getId(), "Canceled");
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
    public void stopScheduler() {
        scheduler.shutdown();
    }
}
