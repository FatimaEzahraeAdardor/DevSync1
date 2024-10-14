package org.example.devsync1.repositories.implementations;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.example.devsync1.entities.Tag;
import org.example.devsync1.entities.Task;
import org.example.devsync1.repositories.interfaces.TaskInterface;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class TaskRepository implements TaskInterface {
    private final EntityManagerFactory entityManagerFactory;
    public TaskRepository() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("myJPAUnit");
    }
    @Override
    public Task save(Task task) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            if (task.getTags() != null) {
                for (int i = 0; i < task.getTags().size(); i++) {
                    Tag tag = task.getTags().get(i);
                    if (tag.getId() != null) {
                        task.getTags().set(i, em.merge(tag));
                    } else {
                        em.persist(tag);
                    }
                }
            }
            em.persist(task);
            em.getTransaction().commit();
        }catch (Exception e) {
            if (em.getTransaction().isActive()){
                em.getTransaction().rollback();
        }
            System.out.println(e.getMessage());
        }finally {
            em.close();
        }
        return task;
    }
    @Override
    public Task findById(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.find(Task.class, id);

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;

    }

    @Override
    public List<Task> findAll() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery("SELECT t FROM Task t", Task.class).getResultList();

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Task> findByUserId(Long managerId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        TypedQuery<Task> query = em.createQuery(
                "SELECT t FROM Task t WHERE t.assignedTo.id = :userId", Task.class);
        query.setParameter("userId", managerId);
        return query.getResultList();
    }

    @Override
    public Task update(Task task) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            if (task.getTags() != null) {
                for (int i = 0; i < task.getTags().size(); i++) {
                    Tag tag = task.getTags().get(i);
                    if (tag.getId() != null) {
                        task.getTags().set(i, em.merge(tag));
                    } else {
                        em.persist(tag);
                    }
                }
            }
            em.merge(task);
            em.getTransaction().commit();
        }catch (Exception e) {
            if (em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            System.out.println(e.getMessage());
        }
        return task;
    }

    @Override
    public Boolean delete(Task task) {
        boolean deleted = false;
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();

            Task managedTask = em.merge(task);
            managedTask.getTags().clear();
            em.merge(managedTask);
            em.remove(managedTask);
            em.getTransaction().commit();
            deleted = true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println(e.getMessage());
        } finally {
            em.close();
        }
        return deleted;
    }

    public  static void main(String[] args) {
        TaskRepository repository = new TaskRepository();
        Task task = repository.findById(1L);
        repository.delete(task);
    }
}
