package argos.com.br.crud_basico.domain.repository;

import argos.com.br.crud_basico.domain.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);
    Optional<Comment> findById(Long id);
    List<Comment> findAllByPostId(Long postId);
    void delete(Comment comment);
}