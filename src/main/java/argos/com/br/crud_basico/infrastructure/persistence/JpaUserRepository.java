package argos.com.br.crud_basico.infrastructure.persistence;

import argos.com.br.crud_basico.domain.model.User;
import argos.com.br.crud_basico.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserRepository extends UserRepository, JpaRepository<User, Long> {
    @Override
    Optional<User> findByEmail(String email);
}
