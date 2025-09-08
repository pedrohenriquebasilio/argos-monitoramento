package argos.com.br.blog.domain.repository;

import argos.com.br.blog.domain.model.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TodoRepository {
    Todo save(Todo todo);
    Optional<Todo> findById(Long id);
    Page<Todo> findAll(Pageable pageable);
    Page<Todo> findByUserId(Long userId, Pageable pageable);
    void delete(Todo todo);
}