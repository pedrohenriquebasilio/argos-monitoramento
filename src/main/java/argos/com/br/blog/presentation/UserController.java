package argos.com.br.blog.presentation;


import argos.com.br.blog.application.dto.auth.UserCreateRequest;
import argos.com.br.blog.application.dto.auth.UserResponse;
import argos.com.br.blog.application.dto.auth.UserUpdateRequest;
import argos.com.br.blog.application.services.user.UserApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuários", description = "Operações relacionadas a usuários")
public class UserController {

    private final UserApplicationService service;

    public UserController(UserApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar um novo usuário", description = "Cria um novo usuário com os dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário criado com sucesso",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou email já cadastrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content)
    })
    public ResponseEntity<UserResponse> create(
            @Parameter(description = "Dados para criação do usuário", required = true)
            @Valid @RequestBody UserCreateRequest in) {
        return ResponseEntity.ok(service.create(in));
    }

    @GetMapping
    @Operation(summary = "Listar usuários", description = "Retorna uma lista paginada de usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content)
    })
    public ResponseEntity<Page<UserResponse>> list(
            @Parameter(description = "Número da página (começa em 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.list(page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID", description = "Retorna os dados de um usuário específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Usuário não encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content)
    })
    public ResponseEntity<UserResponse> get(
            @Parameter(description = "ID do usuário", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Usuário não encontrado ou dados inválidos",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content)
    })
    public ResponseEntity<UserResponse> update(
            @Parameter(description = "ID do usuário", required = true) @PathVariable Long id,
            @Parameter(description = "Dados para atualização do usuário", required = true) 
            @Valid @RequestBody UserUpdateRequest in) {
        return ResponseEntity.ok(service.update(id, in));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir usuário", description = "Remove um usuário do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Usuário não encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content)
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do usuário", required = true) @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
