package argos.com.br.blog.presentation;

import argos.com.br.blog.application.dto.album.AlbumCreateRequest;
import argos.com.br.blog.application.dto.album.AlbumResponse;
import argos.com.br.blog.application.dto.album.AlbumUpdateRequest;
import argos.com.br.blog.application.services.album.AlbumApplicationService;
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
@RequestMapping("/albums")
@Tag(name = "Albums", description = "API para gerenciamento de álbuns")
public class AlbumController {

    private final AlbumApplicationService service;

    public AlbumController(AlbumApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar um novo álbum", description = "Cria um novo álbum com os dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Álbum criado com sucesso",
                    content = @Content(schema = @Schema(implementation = AlbumResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<AlbumResponse> create(@Valid @RequestBody AlbumCreateRequest in) {
        return ResponseEntity.ok(service.create(in));
    }

    @GetMapping
    @Operation(summary = "Listar todos os álbuns", description = "Retorna uma lista paginada de todos os álbuns")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de álbuns recuperada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Page<AlbumResponse>> list(
            @Parameter(description = "Número da página (começa em 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.list(page, size));
    }

    @GetMapping("/by-user/{userId}")
    @Operation(summary = "Listar álbuns por usuário", description = "Retorna uma lista paginada de álbuns de um usuário específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de álbuns do usuário recuperada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Page<AlbumResponse>> listByUser(
            @Parameter(description = "ID do usuário") @PathVariable Long userId,
            @Parameter(description = "Número da página (começa em 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.listByUser(userId, page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter um álbum pelo ID", description = "Retorna um álbum específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Álbum encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = AlbumResponse.class))),
            @ApiResponse(responseCode = "404", description = "Álbum não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<AlbumResponse> get(@Parameter(description = "ID do álbum") @PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um álbum", description = "Atualiza um álbum existente pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Álbum atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = AlbumResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Álbum não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<AlbumResponse> update(
            @Parameter(description = "ID do álbum") @PathVariable Long id, 
            @Valid @RequestBody AlbumUpdateRequest in) {
        return ResponseEntity.ok(service.update(id, in));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um álbum", description = "Remove um álbum existente pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Álbum excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Álbum não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> delete(@Parameter(description = "ID do álbum") @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
