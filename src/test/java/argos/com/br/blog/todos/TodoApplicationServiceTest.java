package argos.com.br.blog.todos;

import argos.com.br.blog.application.dto.todo.TodoCreateRequest;
import argos.com.br.blog.application.dto.todo.TodoUpdateRequest;
import argos.com.br.blog.application.services.todo.TodoApplicationService;
import argos.com.br.blog.domain.model.Todo;
import argos.com.br.blog.domain.model.User;
import argos.com.br.blog.domain.repository.TodoRepository;
import argos.com.br.blog.infrastructure.logging.LoggerService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoApplicationServiceTest {

    @Mock
    TodoRepository todos;
    @InjectMocks
    TodoApplicationService service;
    @Mock
    LoggerService logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new TodoApplicationService(todos, logger);
    }

    private static User userWithId(Long id){ var u = new User(); u.setId(id); return u; }

    @Test
    void create_ok() {
        var in = new TodoCreateRequest(2L, "Comprar");

        var t = new Todo();
        t.setId(1L);
        t.setUser(userWithId(2L));
        t.setTitle("Comprar");
        t.setCompleted(false);

        when(todos.save(any(Todo.class))).thenReturn(t);

        var out = service.create(in);

        assertThat(out.id()).isEqualTo(1L);
        assertThat(out.userId()).isEqualTo(2L);

        var captor = ArgumentCaptor.forClass(Todo.class);
        verify(todos).save(captor.capture());
        assertThat(captor.getValue().getUser().getId()).isEqualTo(2L);
    }

    @Test
    void listByUser_ok() {
        var t = new Todo(); t.setId(1L); t.setUser(userWithId(7L)); t.setTitle("A");
        when(todos.findByUserId(eq(7L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(t)));

        var page = service.listByUser(7L, 0, 10);

        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().get(0).userId()).isEqualTo(7L);
    }

    @Test
    void toggle_ok() {
        var t = new Todo(); t.setId(1L); t.setUser(userWithId(2L)); t.setTitle("Comprar"); t.setCompleted(false);
        when(todos.findById(1L)).thenReturn(Optional.of(t));
        when(todos.save(any(Todo.class))).thenAnswer(i -> i.getArgument(0));

        var out = service.toggle(1L);

        assertThat(out.completed()).isTrue();
    }
    @Test
    void toggle_notFound_erro() {
        when(todos.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.toggle(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Todo não encontrado");
    }
    @Test
    void list_ok() {
        var t = new Todo(); t.setId(1L); t.setUser(userWithId(3L)); t.setTitle("Ler");
        when(todos.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(t)));

        var page = service.list(0, 10);

        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().get(0).userId()).isEqualTo(3L);
    }
    @Test
    void update_ok() {
        var t = new Todo(); t.setId(1L); t.setUser(userWithId(2L)); t.setTitle("Old"); t.setCompleted(false);
        when(todos.findById(1L)).thenReturn(Optional.of(t));
        when(todos.save(any(Todo.class))).thenAnswer(i -> i.getArgument(0));

        var out = service.update(1L, new TodoUpdateRequest("New", true));

        assertThat(out.title()).isEqualTo("New");
        assertThat(out.completed()).isTrue();
    }

    @Test
    void get_ok() {
        var t = new Todo(); t.setId(1L); t.setUser(userWithId(2L)); t.setTitle("Comprar");
        when(todos.findById(1L)).thenReturn(Optional.of(t));

        var out = service.get(1L);

        assertThat(out.id()).isEqualTo(1L);
        assertThat(out.title()).isEqualTo("Comprar");
    }

    @Test
    void get_notFound_erro() {
        when(todos.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Todo não encontrado");
    }

    @Test
    void delete_ok() {
        var t = new Todo(); t.setId(1L);
        when(todos.findById(1L)).thenReturn(Optional.of(t));

        service.delete(1L);

        verify(todos).delete(t);
    }

    @Test
    void delete_notFound_erro() {
        when(todos.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Todo não encontrado");
    }

    @Test
    void update_notFound_erro() {
        when(todos.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(99L, new TodoUpdateRequest("B", true)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Todo não encontrado");
    }
}
