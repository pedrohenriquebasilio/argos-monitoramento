package argos.com.br.blog.presentation;

import argos.com.br.blog.application.dto.todo.TodoCreateRequest;
import argos.com.br.blog.application.dto.todo.TodoResponse;
import argos.com.br.blog.application.dto.todo.TodoUpdateRequest;
import argos.com.br.blog.application.services.todo.TodoApplicationService;
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
@RequestMapping("/todos")
@Tag(name = "Todos", description = "API para gerenciamento de tarefas")
public class TodoController {

    private final TodoApplicationService service;

    public TodoController(TodoApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar uma nova tarefa", description = "Cria uma nova tarefa com os dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa criada com sucesso",
                    content = @Content(schema = @Schema(implementation = TodoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<TodoResponse> create(@Valid @RequestBody TodoCreateRequest in) {
        return ResponseEntity.ok(service.create(in));
    }

    @GetMapping
    @Operation(summary = "Listar todas as tarefas", description = "Retorna uma lista paginada de todas as tarefas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tarefas recuperada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Page<TodoResponse>> list(
            @Parameter(description = "Número da página (começa em 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.list(page, size));
    }

    @GetMapping("/by-user/{userId}")
    @Operation(summary = "Listar tarefas por usuário", description = "Retorna uma lista paginada de tarefas de um usuário específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tarefas do usuário recuperada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Page<TodoResponse>> listByUser(
            @Parameter(description = "ID do usuário") @PathVariable Long userId,
            @Parameter(description = "Número da página (começa em 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.listByUser(userId, page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter uma tarefa pelo ID", description = "Retorna uma tarefa específica pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa encontrada com sucesso",
                    content = @Content(schema = @Schema(implementation = TodoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<TodoResponse> get(@Parameter(description = "ID da tarefa") @PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma tarefa", description = "Atualiza uma tarefa existente pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso",
                    content = @Content(schema = @Schema(implementation = TodoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<TodoResponse> update(
            @Parameter(description = "ID da tarefa") @PathVariable Long id, 
            @Valid @RequestBody TodoUpdateRequest in) {
        return ResponseEntity.ok(service.update(id, in));
    }

    @PatchMapping("/{id}/toggle")
    @Operation(summary = "Alternar status de uma tarefa", description = "Alterna o status de conclusão de uma tarefa existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status da tarefa alternado com sucesso",
                    content = @Content(schema = @Schema(implementation = TodoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<TodoResponse> toggle(@Parameter(description = "ID da tarefa") @PathVariable Long id) {
        return ResponseEntity.ok(service.toggle(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma tarefa", description = "Remove uma tarefa existente pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tarefa excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> delete(@Parameter(description = "ID da tarefa") @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}