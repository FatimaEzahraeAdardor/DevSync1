package org.example.devsync1.repositories.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.example.devsync1.entities.Request;
import org.example.devsync1.entities.Token;
import org.example.devsync1.enums.RequestStatus;
import org.example.devsync1.repositories.interfaces.RequestInterface;

import java.util.List;
import java.util.Optional;

public class RequestRepository implements RequestInterface {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myJPAUnit");

    @Override
    public Request save(Request request) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();

            em.persist(request);
            em.getTransaction().commit();
        }catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }finally {
            em.close();
        }
        return request;
    }

    @Override
    public Request update(Request request) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(request);
            em.getTransaction().commit();
            return request;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    @Override
    public Optional<Request> findById(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            Request request = em.find(Request.class, id);
            return Optional.ofNullable(request);
        } finally {
            em.close();
        }
    }

    // New method to find a request by task ID
    @Override
    public Optional<Request> findByTaskId(Long taskId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<Request> query = em.createQuery("SELECT r FROM Request r WHERE r.task.id = :taskId", Request.class);
            query.setParameter("taskId", taskId);
            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }
@Override
    public List<Request> findByUserId(Long userId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.createQuery("SELECT tr FROM Request tr WHERE tr.task.id = :userId", Request.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }
}
