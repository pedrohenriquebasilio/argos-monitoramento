package argos.com.br.blog.domain.repository;

import argos.com.br.blog.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Page<User> findAll(Pageable pageable);
    void delete(User user);
    boolean existsByEmail(String email);
}
