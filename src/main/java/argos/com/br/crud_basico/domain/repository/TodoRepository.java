package argos.com.br.crud_basico.domain.repository;

import argos.com.br.crud_basico.domain.model.Todo;

import java.util.List;
import java.util.Optional;

public interface TodoRepository {
    Todo save(Todo todo);
    Optional<Todo> findById(Long id);
    List<Todo> findAll();
    List<Todo> findByUserId(Long userId);
    void delete(Todo todo);
}