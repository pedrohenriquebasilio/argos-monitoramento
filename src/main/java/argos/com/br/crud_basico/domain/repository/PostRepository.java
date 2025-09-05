package argos.com.br.crud_basico.domain.repository;

import argos.com.br.crud_basico.domain.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);
    Optional<Post> findById(Long id);
    List<Post> findAll();
    List<Post> findByUserId(Long userId);
    void delete(Post post);
}