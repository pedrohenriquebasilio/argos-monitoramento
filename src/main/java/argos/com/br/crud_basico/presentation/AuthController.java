package argos.com.br.crud_basico.presentation;


import argos.com.br.crud_basico.application.dto.auth.*;
import argos.com.br.crud_basico.application.services.auth.AuthenticateUserService;
import argos.com.br.crud_basico.application.services.auth.RegisterUserService;
import jakarta.validation.Valid;
import org.hibernate.query.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegisterUserService registerUser;
    private final AuthenticateUserService authenticateUser;

    public AuthController(RegisterUserService registerUser, AuthenticateUserService authenticateUser) {
        this.registerUser = registerUser;
        this.authenticateUser = authenticateUser;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody SignupRequest in) {
        return ResponseEntity.ok(registerUser.execute(in));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest in) {
        String token = authenticateUser.execute(in);
        return ResponseEntity.ok(LoginResponse.bearer(token));
    }
}
