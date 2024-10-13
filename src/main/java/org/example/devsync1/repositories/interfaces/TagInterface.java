package org.example.devsync1.repositories.interfaces;

import org.example.devsync1.entities.Tag;

import java.util.List;

public interface TagInterface {
    Tag save(Tag tag);
    Tag findById(Long id);
    List<Tag> findAll();
    Tag update(Tag tag);
    void delete(Tag tag);
}
