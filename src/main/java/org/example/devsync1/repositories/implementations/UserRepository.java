package org.example.devsync1.repositories.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.devsync1.entities.User;
import org.example.devsync1.repositories.interfaces.UserInterface;

import java.util.List;
import java.util.Optional;

public class UserRepository implements UserInterface {
    private final EntityManagerFactory entityManagerFactory;
    private final EntityManager entityManager;

    public UserRepository() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("myJPAUnit");
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    @Override
    public void save(User user) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }


    }

    @Override
    public Optional<User> findById(Long id) {
        try {
            return Optional.ofNullable(entityManager.find(User.class, id));
        }catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }

    }

    @Override
    public List<User> findAll() {
        try {
            return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void update(User user) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(user);
            entityManager.getTransaction().commit();
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }
    }


    @Override
    public void delete(User user) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(user);
            entityManager.getTransaction().commit();
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }

    }


}
