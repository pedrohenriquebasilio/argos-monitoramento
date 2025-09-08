package argos.com.br.blog.presentation;


import argos.com.br.blog.application.dto.auth.*;
import argos.com.br.blog.application.services.auth.AuthenticateUserService;
import argos.com.br.blog.application.services.auth.RegisterUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Operações relacionadas a autenticação e registro de usuários")
public class AuthController {

    private final RegisterUserService registerUser;
    private final AuthenticateUserService authenticateUser;

    public AuthController(RegisterUserService registerUser, AuthenticateUserService authenticateUser) {
        this.registerUser = registerUser;
        this.authenticateUser = authenticateUser;
    }

    @PostMapping("/signup")
    @Operation(summary = "Registrar novo usuário", description = "Cria um novo usuário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou email já cadastrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content)
    })
    public ResponseEntity<UserResponse> signup(
            @Parameter(description = "Dados para registro do usuário", required = true)
            @RequestBody SignupRequest in) {
        return ResponseEntity.ok(registerUser.execute(in));
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário", description = "Autentica um usuário e retorna um token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content)
    })
    public ResponseEntity<LoginResponse> login(
            @Parameter(description = "Credenciais de login", required = true)
            @RequestBody LoginRequest in) {
        String token = authenticateUser.execute(in);
        return ResponseEntity.ok(LoginResponse.bearer(token));
    }
}
