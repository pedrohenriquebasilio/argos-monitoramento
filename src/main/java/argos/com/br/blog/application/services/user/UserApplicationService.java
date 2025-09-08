package argos.com.br.blog.application.services.user;


import argos.com.br.blog.application.dto.auth.UserCreateRequest;
import argos.com.br.blog.application.dto.auth.UserResponse;
import argos.com.br.blog.application.dto.auth.UserUpdateRequest;
import argos.com.br.blog.domain.model.User;
import argos.com.br.blog.domain.repository.UserRepository;
import argos.com.br.blog.infrastructure.logging.LoggerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserApplicationService {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final LoggerService logger;

    public UserApplicationService(UserRepository users, PasswordEncoder encoder, LoggerService logger) {
        this.users = users;
        this.encoder = encoder;
        this.logger = logger;
    }



    public UserResponse create(UserCreateRequest in){

        logger.logRequest("UserApplicationService", "create", in);
        users.findByEmail(in.email()).ifPresent(u -> {
            throw  new IllegalArgumentException("Email já cadastrado");
        });
        var u = new User();
        u.setName(in.name());
        u.setEmail(in.email());
        u.setPassword(encoder.encode((in.passwrd())));
        UserResponse response = UserMapper.toResponse(users.save(u));
        logger.logResponse("UserApplicationService", "create", response);
        return response;
    }



    public UserResponse getById(Long id) {
        logger.logRequest("UserApplicationService", "getById", id);
        var u = users.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        UserResponse response = UserMapper.toResponse(u);
        logger.logResponse("UserApplicationService", "getById", response);
        return response;
    }

    public UserResponse update(Long id, UserUpdateRequest in) {
        logger.logRequest("UserApplicationService", "update", "id: " + id + ", request: " + in);
        var u = users.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (in.name() != null && !in.name().isBlank()) u.setName(in.name());
        if (in.email() != null && !in.email().isBlank()) {
            users.findByEmail(in.email()).ifPresent(existing -> {
                if (!existing.getId().equals(id)) throw new IllegalArgumentException("Email já em uso");
            });
            u.setEmail(in.email());
        }
        if (in.password() != null && !in.password().isBlank()) {
            u.setPassword(encoder.encode(in.password()));
        }

        UserResponse response = UserMapper.toResponse(users.save(u));
        logger.logResponse("UserApplicationService", "update", response);
        return response;
    }

    public void delete(Long id) {
        logger.logRequest("UserApplicationService", "delete", id);
        var u = users.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        users.delete(u);
        logger.logResponse("UserApplicationService", "delete", "User deleted: " + id);
    }

    public Page<UserResponse> list(int page, int size) {
        logger.logRequest("UserApplicationService", "list", "page: " + page + ", size: " + size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<UserResponse> response = users.findAll(pageable).map(this::toResponse);
        logger.logResponse("UserApplicationService", "list", "Total elements: " + response.getTotalElements());
        return response;
    }

    private UserResponse toResponse(User u) {
        return new UserResponse(u.getId(), u.getName(), u.getEmail());
    }


}
