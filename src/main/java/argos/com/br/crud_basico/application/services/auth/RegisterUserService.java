package argos.com.br.crud_basico.application.services.auth;

import argos.com.br.crud_basico.application.dto.auth.SignupRequest;
import argos.com.br.crud_basico.application.dto.auth.UserResponse;
import argos.com.br.crud_basico.domain.model.User;
import argos.com.br.crud_basico.domain.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserService {


    private final UserRepository users;
    private final PasswordEncoder encoder;

    public RegisterUserService(UserRepository users, PasswordEncoder encoder){
        this.users = users;
        this.encoder = encoder;
    }

    public UserResponse execute(SignupRequest in){
        users.findByEmail(in.email()).ifPresent(u -> {
            throw new IllegalArgumentException("Email já cadastrado");
        });

        User user = new User();
        user.setName(in.name());
        user.setEmail(in.email());
        user.setPassword(encoder.encode(in.password()));
        User saved = users.save(user);

        return new UserResponse(saved.getId(), saved.getName(), saved.getEmail());
    }
}
