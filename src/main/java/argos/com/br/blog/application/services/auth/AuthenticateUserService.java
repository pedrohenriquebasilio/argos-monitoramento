package argos.com.br.blog.application.services.auth;


import argos.com.br.blog.application.dto.auth.LoginRequest;
import argos.com.br.blog.infrastructure.logging.LoggerService;
import argos.com.br.blog.infrastructure.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateUserService {

    private final AuthenticationManager manager;
    private final JwtTokenProvider jwt;
    private final LoggerService logger;

    public AuthenticateUserService(AuthenticationManager manager, JwtTokenProvider jwt, LoggerService logger){
        this.manager = manager;
        this.jwt = jwt;
        this.logger = logger;
    }

    public String execute(LoginRequest in){
        logger.logRequest("AuthenticateUserService", "execute", "Login attempt for: " + in.email());
        try {
            var token = new UsernamePasswordAuthenticationToken(in.email(), in.password());
            var authentication = manager.authenticate(token);
            
            String jwtToken = jwt.generateToken(authentication.getName());
            logger.logResponse("AuthenticateUserService", "execute", "Login successful for: " + in.email());
            return jwtToken;
        } catch (AuthenticationException e) {
            logger.logError("AuthenticateUserService", "execute", e);
            throw e;
        }
    }
}
