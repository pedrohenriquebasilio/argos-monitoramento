package argos.com.br.blog.presentation;

import argos.com.br.blog.application.dto.post.PostCreateRequest;
import argos.com.br.blog.application.dto.post.PostResponse;
import argos.com.br.blog.application.dto.post.PostUpdateRequest;
import argos.com.br.blog.application.services.posts.PostApplicationService;
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
@RequestMapping("/posts")
@Tag(name = "Posts", description = "Operações relacionadas a posts")
public class PostController {

    private final PostApplicationService service;

    public PostController(PostApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar um novo post", description = "Cria um novo post com os dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post criado com sucesso",
                    content = @Content(schema = @Schema(implementation = PostResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content)
    })
    public ResponseEntity<PostResponse> create(
            @Parameter(description = "Dados para criação do post", required = true)
            @Valid @RequestBody PostCreateRequest in) {
        return ResponseEntity.ok(service.create(in));
    }

    @GetMapping
    @Operation(summary = "Listar posts", description = "Retorna uma lista paginada de posts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de posts retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content)
    })
    public ResponseEntity<Page<PostResponse>> list(
            @Parameter(description = "Número da página (começa em 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.list(page, size));
    }

    @GetMapping("/by-user/{userId}")
    @Operation(summary = "Listar posts por usuário", description = "Retorna uma lista paginada de posts de um usuário específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de posts retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content)
    })
    public ResponseEntity<Page<PostResponse>> listByUser(
            @Parameter(description = "ID do usuário", required = true) @PathVariable Long userId,
            @Parameter(description = "Número da página (começa em 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.listByUser(userId, size, page)); // cuidado com a ordem!
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar post por ID", description = "Retorna os dados de um post específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = PostResponse.class))),
            @ApiResponse(responseCode = "400", description = "Post não encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content)
    })
    public ResponseEntity<PostResponse> get(
            @Parameter(description = "ID do post", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar post", description = "Atualiza os dados de um post existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = PostResponse.class))),
            @ApiResponse(responseCode = "400", description = "Post não encontrado ou dados inválidos",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content)
    })
    public ResponseEntity<PostResponse> update(
            @Parameter(description = "ID do post", required = true) @PathVariable Long id,
            @Parameter(description = "Dados para atualização do post", required = true) 
            @Valid @RequestBody PostUpdateRequest in) {
        return ResponseEntity.ok(service.update(id, in));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir post", description = "Remove um post do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Post excluído com sucesso",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Post não encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content)
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do post", required = true) @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
