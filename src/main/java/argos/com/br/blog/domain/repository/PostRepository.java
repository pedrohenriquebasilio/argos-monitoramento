package argos.com.br.blog.domain.repository;

import argos.com.br.blog.domain.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostRepository {
    Post save(Post post);
    Optional<Post> findById(Long id);
    Page<Post> findAll(Pageable pageable);
    Page<Post> findByUserId(Long userId, Pageable pageable);
    boolean existsById(Long id);
    void delete(Post post);

}