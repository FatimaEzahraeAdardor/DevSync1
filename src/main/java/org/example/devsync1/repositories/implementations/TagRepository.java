package org.example.devsync1.repositories.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.devsync1.entities.Tag;
import org.example.devsync1.entities.Task;
import org.example.devsync1.repositories.interfaces.TagInterface;
import org.example.devsync1.repositories.interfaces.TaskInterface;

import java.util.List;

public class TagRepository implements TagInterface {
    private EntityManagerFactory entityManagerFactory;
    public TagRepository() {
        entityManagerFactory = Persistence.createEntityManagerFactory("myJPAUnit");
    }

    @Override
    public Tag save(Tag Tag) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();

            em.persist(Tag);
            em.getTransaction().commit();
        }catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }finally {
            em.close();
        }
        return Tag;
    }

    @Override
    public Tag findById(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.find(Tag.class, id);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Tag> findAll() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery("SELECT t FROM Tag t", Tag.class).getResultList();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Tag update(Tag tag) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(tag);
            em.getTransaction().commit();
        }catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
        return tag;
    }

    @Override
    public void delete(Tag tag) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            Tag managedTag = em.merge(tag);
            em.remove(managedTag);
            em.getTransaction().commit();
        }catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }finally {
            em.close();
        }

    }
}
