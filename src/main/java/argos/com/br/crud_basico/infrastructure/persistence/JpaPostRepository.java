package argos.com.br.crud_basico.infrastructure.persistence;

import argos.com.br.crud_basico.domain.model.Post;
import argos.com.br.crud_basico.domain.repository.PostRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaPostRepository extends PostRepository, JpaRepository<Post, Long> {
    List<Post> findByUserId(long userId);
}
