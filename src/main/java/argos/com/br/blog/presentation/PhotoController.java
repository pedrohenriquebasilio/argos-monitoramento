package argos.com.br.blog.presentation;


import argos.com.br.blog.application.dto.photo.PhotoCreateRequest;
import argos.com.br.blog.application.dto.photo.PhotoResponse;
import argos.com.br.blog.application.dto.photo.PhotoUpdateRequest;
import argos.com.br.blog.application.services.photo.PhotoApplicationService;
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
@RequestMapping("/photos")
@Tag(name = "Photos", description = "API para gerenciamento de fotos")
public class PhotoController {

    private final PhotoApplicationService service;

    public PhotoController(PhotoApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar uma nova foto", description = "Cria uma nova foto com os dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Foto criada com sucesso",
                    content = @Content(schema = @Schema(implementation = PhotoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<PhotoResponse> create(@Valid @RequestBody PhotoCreateRequest in) {
        return ResponseEntity.ok(service.create(in));
    }

    @GetMapping
    @Operation(summary = "Listar todas as fotos", description = "Retorna uma lista paginada de todas as fotos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de fotos recuperada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Page<PhotoResponse>> list(
            @Parameter(description = "Número da página (começa em 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.list(page, size));
    }

    @GetMapping("/by-album/{albumId}")
    @Operation(summary = "Listar fotos por álbum", description = "Retorna uma lista paginada de fotos de um álbum específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de fotos do álbum recuperada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Álbum não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Page<PhotoResponse>> listByAlbum(
            @Parameter(description = "ID do álbum") @PathVariable Long albumId,
            @Parameter(description = "Número da página (começa em 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.listByAlbum(albumId, page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter uma foto pelo ID", description = "Retorna uma foto específica pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Foto encontrada com sucesso",
                    content = @Content(schema = @Schema(implementation = PhotoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Foto não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<PhotoResponse> get(@Parameter(description = "ID da foto") @PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma foto", description = "Atualiza uma foto existente pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Foto atualizada com sucesso",
                    content = @Content(schema = @Schema(implementation = PhotoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Foto não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<PhotoResponse> update(
            @Parameter(description = "ID da foto") @PathVariable Long id, 
            @Valid @RequestBody PhotoUpdateRequest in) {
        return ResponseEntity.ok(service.update(id, in));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma foto", description = "Remove uma foto existente pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Foto excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Foto não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> delete(@Parameter(description = "ID da foto") @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
