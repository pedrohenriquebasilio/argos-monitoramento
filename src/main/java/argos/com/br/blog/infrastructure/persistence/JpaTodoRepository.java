package argos.com.br.blog.infrastructure.persistence;

import argos.com.br.blog.domain.model.Todo;
import argos.com.br.blog.domain.repository.TodoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaTodoRepository extends TodoRepository, JpaRepository<Todo, Long> {
    List<Todo> findUserById(long userId);
    List<Todo> findByCompleted(boolean completed);
}
