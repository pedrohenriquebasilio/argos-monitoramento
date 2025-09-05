package argos.com.br.crud_basico.application.services.user;


import argos.com.br.crud_basico.application.dto.auth.UserCreateRequest;
import argos.com.br.crud_basico.application.dto.auth.UserResponse;
import argos.com.br.crud_basico.application.dto.auth.UserUpdateRequest;
import argos.com.br.crud_basico.domain.model.User;
import argos.com.br.crud_basico.domain.repository.UserRepository;
import org.hibernate.query.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserApplicationService {

    private final UserRepository users;
    private final PasswordEncoder encoder;

    public UserApplicationService(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    public UserResponse create(UserCreateRequest in){
        users.findByEmail(in.email()).ifPresent(u -> {
            throw  new IllegalArgumentException("Email já cadastrado");
        });
        var u = new User();
        u.setName(in.name());
        u.setEmail(in.email());
        u.setPassword(encoder.encode((in.passwrd())));
        return UserMapper.toResponse(users.save(u));
    }



    public UserResponse getById(Long id) {
        var u = users.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        return UserMapper.toResponse(u);
    }

    public UserResponse update(Long id, UserUpdateRequest in) {
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

        return UserMapper.toResponse(users.save(u));
    }

    public void delete(Long id) {
        var u = users.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        users.delete(u);
    }
}
