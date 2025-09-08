package argos.com.br.blog.domain.repository;

import argos.com.br.blog.domain.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment c);
    Optional<Comment> findById(Long id);
    void delete(Comment c);
    Page<Comment> findAll(Pageable pageable);
    Page<Comment> findByPostId(Long postId, Pageable pageable);
    Page<Comment> findByUserId(Long userId, Pageable pageable);
    boolean existsById(Long id);
}