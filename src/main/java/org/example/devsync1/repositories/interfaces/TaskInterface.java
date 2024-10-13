package org.example.devsync1.repositories.interfaces;

import org.example.devsync1.entities.Task;

import java.util.List;

public interface TaskInterface {

     Task save(Task task);

     Task findById(Long id);

     List<Task> findAll();
     List<Task> findByUserId(Long id);

     Task update(Task task);

     Boolean delete(Task task);


}
