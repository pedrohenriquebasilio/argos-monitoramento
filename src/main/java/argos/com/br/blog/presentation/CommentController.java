package argos.com.br.blog.presentation;

import argos.com.br.blog.application.dto.comment.CommentCreateRequest;
import argos.com.br.blog.application.dto.comment.CommentResponse;
import argos.com.br.blog.application.dto.comment.CommentUpdateRequest;
import argos.com.br.blog.application.services.comment.CommentApplicationService;
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

@RestController
@RequestMapping("/comments")
@Tag(name = "Comments", description = "API para gerenciamento de comentários")
public class CommentController {

    private final CommentApplicationService service;

    public CommentController(CommentApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar um novo comentário", description = "Cria um novo comentário com os dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentário criado com sucesso",
                    content = @Content(schema = @Schema(implementation = CommentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<CommentResponse> create(@Valid @RequestBody CommentCreateRequest in) {
        return ResponseEntity.ok(service.create(in));
    }

    @GetMapping
    @Operation(summary = "Listar todos os comentários", description = "Retorna uma lista paginada de todos os comentários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de comentários recuperada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Page<CommentResponse>> list(
            @Parameter(description = "Número da página (começa em 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.list(page, size));
    }

    @GetMapping("/by-post/{postId}")
    @Operation(summary = "Listar comentários por post", description = "Retorna uma lista paginada de comentários de um post específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de comentários do post recuperada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Post não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Page<CommentResponse>> listByPost(
            @Parameter(description = "ID do post") @PathVariable Long postId,
            @Parameter(description = "Número da página (começa em 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.listByPost(postId, page, size));
    }

    @GetMapping("/by-user/{userId}")
    @Operation(summary = "Listar comentários por usuário", description = "Retorna uma lista paginada de comentários de um usuário específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de comentários do usuário recuperada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Page<CommentResponse>> listByUser(
            @Parameter(description = "ID do usuário") @PathVariable Long userId,
            @Parameter(description = "Número da página (começa em 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.listByUser(userId, page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter um comentário pelo ID", description = "Retorna um comentário específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentário encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = CommentResponse.class))),
            @ApiResponse(responseCode = "404", description = "Comentário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<CommentResponse> get(@Parameter(description = "ID do comentário") @PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um comentário", description = "Atualiza um comentário existente pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentário atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = CommentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Comentário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<CommentResponse> update(
            @Parameter(description = "ID do comentário") @PathVariable Long id, 
            @Valid @RequestBody CommentUpdateRequest in) {
        return ResponseEntity.ok(service.update(id, in));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um comentário", description = "Remove um comentário existente pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comentário excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Comentário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> delete(@Parameter(description = "ID do comentário") @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
