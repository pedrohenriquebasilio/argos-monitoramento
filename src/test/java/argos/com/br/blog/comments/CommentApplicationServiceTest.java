package argos.com.br.blog.comments;

import argos.com.br.blog.application.dto.comment.CommentCreateRequest;
import argos.com.br.blog.application.services.comment.CommentApplicationService;
import argos.com.br.blog.application.services.photo.PhotoApplicationService;
import argos.com.br.blog.domain.model.Comment;
import argos.com.br.blog.domain.model.Post;
import argos.com.br.blog.domain.model.User;
import argos.com.br.blog.domain.repository.CommentRepository;
import argos.com.br.blog.infrastructure.logging.LoggerService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentApplicationServiceTest {

    @Mock
    CommentRepository comments;
    @InjectMocks
    CommentApplicationService service;
    @Mock
    LoggerService logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new CommentApplicationService(comments, logger);
    }


    private static Post postWithId(Long id) {
        var p = new Post();
        p.setId(id);
        return p;
    }



    @Test
    void create_ok() {
        var in = new CommentCreateRequest(1L, 2L, "texto");

        // entidade que o repo retornará (com relações)
        var c = new Comment();
        c.setId(8L);
        c.setPost(postWithId(1L));
        c.setUserId(2L);
        c.setBody("texto");

        when(comments.save(any(Comment.class))).thenReturn(c);

        var out = service.create(in);

        assertThat(out.id()).isEqualTo(8L);
        assertThat(out.postId()).isEqualTo(1L);
        assertThat(out.userId()).isEqualTo(2L);
        assertThat(out.body()).isEqualTo("texto");

        var captor = ArgumentCaptor.forClass(Comment.class);
        verify(comments).save(captor.capture());
        assertThat(captor.getValue().getPost().getId()).isEqualTo(1L);
        assertThat(captor.getValue().getUserId()).isEqualTo(2L);
    }

    @Test
    void listByPost_ok() {
        var c = new Comment();
        c.setId(1L);
        c.setPost(postWithId(9L));
        c.setUserId(3L);
        c.setBody("oi");

        when(comments.findByPostId(eq(9L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(c)));

        var page = service.listByPost(9L, 0, 10);

        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().get(0).postId()).isEqualTo(9L);
        assertThat(page.getContent().get(0).userId()).isEqualTo(3L);
    }

    @Test
    void get_notFound_erro() {
        when(comments.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(42L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Comment não encontrado");
    }
}
