package argos.com.br.blog.posts;

import argos.com.br.blog.application.dto.post.PostCreateRequest;
import argos.com.br.blog.application.dto.post.PostUpdateRequest;
import argos.com.br.blog.application.services.posts.PostApplicationService;
import argos.com.br.blog.domain.model.Post;
import argos.com.br.blog.domain.model.User;
import argos.com.br.blog.domain.repository.PostRepository;
import argos.com.br.blog.infrastructure.logging.LoggerService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostApplicationServiceTest {

    @Mock
    PostRepository posts;
    @InjectMocks
    PostApplicationService service;
    @Mock
    LoggerService logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new PostApplicationService(posts, logger);
    }

    private static User userWithId(Long id){ var u = new User(); u.setId(id); return u; }

    @Test
    void create_ok() {
        var in = new PostCreateRequest(1L, "titulo", "body");

        var entity = new Post();
        entity.setId(5L);
        entity.setUser(userWithId(1L));
        entity.setTitle("titulo");
        entity.setBody("body");

        when(posts.save(any(Post.class))).thenReturn(entity);

        var out = service.create(in);

        assertThat(out.id()).isEqualTo(5L);
        assertThat(out.userId()).isEqualTo(1L);

        var captor = ArgumentCaptor.forClass(Post.class);
        verify(posts).save(captor.capture());
        assertThat(captor.getValue().getUser().getId()).isEqualTo(1L);
    }

    @Test
    void get_ok() {
        var p = new Post(); p.setId(1L); p.setUser(userWithId(2L)); p.setTitle("t"); p.setBody("b");
        when(posts.findById(1L)).thenReturn(Optional.of(p));

        var out = service.get(1L);

        assertThat(out.id()).isEqualTo(1L);
        assertThat(out.userId()).isEqualTo(2L);
    }
    @Test
    void update_ok() {
        var p = new Post(); p.setId(1L); p.setUser(userWithId(2L)); p.setTitle("Old"); p.setBody("Body");
        when(posts.findById(1L)).thenReturn(Optional.of(p));
        when(posts.save(any(Post.class))).thenAnswer(i -> i.getArgument(0));

        var out = service.update(1L, new PostUpdateRequest("New", "NewBody"));

        assertThat(out.title()).isEqualTo("New");
        assertThat(out.body()).isEqualTo("NewBody");
    }

    @Test
    void delete_ok() {
        var p = new Post(); p.setId(1L);
        when(posts.findById(1L)).thenReturn(Optional.of(p));

        service.delete(1L);

        verify(posts).delete(p);
    }

    @Test
    void delete_notFound_erro() {
        when(posts.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Post não encontrado");
    }

    @Test
    void list_ok() {
        var p = new Post(); p.setId(1L); p.setUser(userWithId(4L)); p.setTitle("t"); p.setBody("b");
        when(posts.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(p)));

        var page = service.list(0, 10);

        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().get(0).userId()).isEqualTo(4L);
    }

    @Test
    void listByUser_ok() {
        var p = new Post(); p.setId(1L); p.setUser(userWithId(9L)); p.setTitle("t"); p.setBody("b");
        when(posts.findByUserId(eq(9L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(p)));

        var out = service.listByUser(9L, 0, 10);

        assertThat(out.getContent()).hasSize(1);
        assertThat(out.getContent().get(0).userId()).isEqualTo(9L);
    }

    @Test
    void get_notFound_erro() {
        when(posts.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Post não encontrado");
    }
}
