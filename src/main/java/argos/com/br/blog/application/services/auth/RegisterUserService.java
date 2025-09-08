package argos.com.br.blog.application.services.auth;

import argos.com.br.blog.application.dto.auth.SignupRequest;
import argos.com.br.blog.application.dto.auth.UserResponse;
import argos.com.br.blog.domain.model.User;
import argos.com.br.blog.domain.repository.UserRepository;
import argos.com.br.blog.infrastructure.logging.LoggerService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserService {


    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final LoggerService logger;

    public RegisterUserService(UserRepository users, PasswordEncoder encoder, LoggerService logger){
        this.users = users;
        this.encoder = encoder;
        this.logger = logger;
    }

    public UserResponse execute(SignupRequest in){
        logger.logRequest("RegisterUserService", "execute", "Registration attempt for: " + in.email());
        
        try {
            users.findByEmail(in.email()).ifPresent(u -> {
                logger.logError("RegisterUserService", "execute", new IllegalArgumentException("Email já cadastrado"));
                throw new IllegalArgumentException("Email já cadastrado");
            });

            User user = new User();
            user.setName(in.name());
            user.setEmail(in.email());
            user.setPassword(encoder.encode(in.password()));
            User saved = users.save(user);

            UserResponse response = new UserResponse(saved.getId(), saved.getName(), saved.getEmail());
            logger.logResponse("RegisterUserService", "execute", "User registered successfully: " + saved.getId());
            return response;
        } catch (Exception e) {
            if (!(e instanceof IllegalArgumentException)) {
                logger.logError("RegisterUserService", "execute", e);
            }
            throw e;
        }
    }
}
