package org.example.devsync1.services;

import org.example.devsync1.entities.Tag;
import org.example.devsync1.repositories.implementations.TagRepository;

import java.util.List;

public class TagService {
    TagRepository tagRepository;
    public TagService() {
        tagRepository = new TagRepository();

    }
    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }
    public Tag findById(Long id) {
        return tagRepository.findById(id);
    }
 public Tag update(Tag tag) {
        return tagRepository.update(tag);
 }
 public void delete(Tag tag) {
        tagRepository.delete(tag);
 }
}
