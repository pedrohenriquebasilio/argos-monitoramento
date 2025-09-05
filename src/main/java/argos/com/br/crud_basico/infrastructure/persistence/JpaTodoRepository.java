package argos.com.br.crud_basico.infrastructure.persistence;

import argos.com.br.crud_basico.domain.model.Todo;
import argos.com.br.crud_basico.domain.repository.TodoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaTodoRepository extends TodoRepository, JpaRepository<Todo, Long> {
    List<Todo> findUserById(long userId);
    List<Todo> findByCompleted(boolean completed);
}
