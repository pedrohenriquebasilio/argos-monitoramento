package argos.com.br.blog.application.services.todo;

import argos.com.br.blog.application.dto.todo.TodoCreateRequest;
import argos.com.br.blog.application.dto.todo.TodoResponse;
import argos.com.br.blog.application.dto.todo.TodoUpdateRequest;
import argos.com.br.blog.domain.model.Todo;
import argos.com.br.blog.domain.model.User;
import argos.com.br.blog.domain.repository.TodoRepository;
import argos.com.br.blog.infrastructure.logging.LoggerService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class TodoApplicationService {

    private final TodoRepository todos;
    private final LoggerService logger;

    public TodoApplicationService(TodoRepository todos, LoggerService logger) {
        this.todos = todos;
        this.logger = logger;
    }

    public TodoResponse create(TodoCreateRequest in) {
        logger.logRequest("TodoApplicationService", "create", in);
        var t = new Todo();
        var u = new User();
        u.setId(in.userId());
        t.setUser(u);
        t.setTitle(in.title());
        var saved = todos.save(t);
        TodoResponse response = toResponse(saved);
        logger.logResponse("TodoApplicationService", "create", response);
        return response;
    }

    public Page<TodoResponse> list(int page, int size) {
        logger.logRequest("TodoApplicationService", "list", "page: " + page + ", size: " + size);
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<TodoResponse> response = todos.findAll(pageable).map(this::toResponse);
        logger.logResponse("TodoApplicationService", "list", "Total elements: " + response.getTotalElements());
        return response;
    }

    public Page<TodoResponse> listByUser(Long userId, int page, int size) {
        logger.logRequest("TodoApplicationService", "listByUser", "userId: " + userId + ", page: " + page + ", size: " + size);
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<TodoResponse> response = todos.findByUserId(userId, pageable).map(this::toResponse);
        logger.logResponse("TodoApplicationService", "listByUser", "Total elements: " + response.getTotalElements());
        return response;
    }

    public TodoResponse get(Long id) {
        logger.logRequest("TodoApplicationService", "get", "id: " + id);
        var t = todos.findById(id).orElseThrow(() -> new IllegalArgumentException("Todo não encontrado"));
        TodoResponse response = toResponse(t);
        logger.logResponse("TodoApplicationService", "get", response);
        return response;
    }

    public TodoResponse update(Long id, TodoUpdateRequest in) {
        logger.logRequest("TodoApplicationService", "update", "id: " + id + ", request: " + in);
        var t = todos.findById(id).orElseThrow(() -> new IllegalArgumentException("Todo não encontrado"));

        if (in.title() != null && !in.title().isBlank()) t.setTitle(in.title());
        if (in.completed() != null) t.setCompleted(in.completed());

        var saved = todos.save(t);
        TodoResponse response = toResponse(saved);
        logger.logResponse("TodoApplicationService", "update", response);
        return response;
    }

    public TodoResponse toggle(Long id) {
        logger.logRequest("TodoApplicationService", "toggle", "id: " + id);
        var t = todos.findById(id).orElseThrow(() -> new IllegalArgumentException("Todo não encontrado"));
        t.setCompleted(!t.isCompleted());
        var saved = todos.save(t);
        TodoResponse response = toResponse(saved);
        logger.logResponse("TodoApplicationService", "toggle", response);
        return response;
    }

    public void delete(Long id) {
        logger.logRequest("TodoApplicationService", "delete", "id: " + id);
        var t = todos.findById(id).orElseThrow(() -> new IllegalArgumentException("Todo não encontrado"));
        todos.delete(t);
        logger.logResponse("TodoApplicationService", "delete", "Todo deleted: " + id);
    }

    private TodoResponse toResponse(Todo t) {
        return new TodoResponse(t.getId(), t.getUser().getId(), t.getTitle(), t.isCompleted(), t.getCreatedAt());
    }
}
