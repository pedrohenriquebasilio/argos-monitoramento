package argos.com.br.blog.infrastructure.persistence;

import argos.com.br.blog.domain.model.User;
import argos.com.br.blog.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserRepository extends UserRepository, JpaRepository<User, Long> {
    @Override
    Optional<User> findByEmail(String email);
}
