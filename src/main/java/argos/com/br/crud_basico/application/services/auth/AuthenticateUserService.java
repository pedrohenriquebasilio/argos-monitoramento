package argos.com.br.crud_basico.application.services.auth;


import argos.com.br.crud_basico.application.dto.auth.LoginRequest;
import argos.com.br.crud_basico.infrastructure.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateUserService {

    private final AuthenticationManager manager;
    private final JwtTokenProvider jwt;

    public AuthenticateUserService(AuthenticationManager manager, JwtTokenProvider jwt){
        this.manager = manager;
        this.jwt = jwt;
    }

    public String execute(LoginRequest in){
        var token = new UsernamePasswordAuthenticationToken(in.email(), in.password());
        var authentication = manager.authenticate(token);

        return jwt.generateToken(authentication.getName());
    }
}
