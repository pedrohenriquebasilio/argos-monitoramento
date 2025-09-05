package argos.com.br.crud_basico.infrastructure.persistence;


import argos.com.br.crud_basico.domain.model.Comment;
import argos.com.br.crud_basico.domain.repository.CommentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface JpaCommentRepository extends CommentRepository, JpaRepository<Comment, Long> {
    List<Comment> findAllByPostId(long postId);
}
