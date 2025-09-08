package argos.com.br.blog.album;

import argos.com.br.blog.application.dto.album.AlbumCreateRequest;
import argos.com.br.blog.application.dto.album.AlbumUpdateRequest;
import argos.com.br.blog.application.services.album.AlbumApplicationService;
import argos.com.br.blog.application.services.comment.CommentApplicationService;
import argos.com.br.blog.domain.model.Album;
import argos.com.br.blog.domain.model.User;
import argos.com.br.blog.domain.repository.AlbumRepository;
import argos.com.br.blog.infrastructure.logging.LoggerService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlbumApplicationServiceTest {

    @Mock
    AlbumRepository albums;
    @InjectMocks
    AlbumApplicationService service;
    @Mock
    LoggerService logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new AlbumApplicationService(albums, logger);
    }

    private static User userWithId(Long id){ var u = new User(); u.setId(id); return u; }

    @Test
    void create_ok() {
        var in = new AlbumCreateRequest(1L, "Viagem");

        var a = new Album();
        a.setId(3L);
        a.setTitle("Viagem");
        a.setUser(userWithId(1L));

        when(albums.save(any(Album.class))).thenReturn(a);

        var out = service.create(in);

        assertThat(out.id()).isEqualTo(3L);
        assertThat(out.userId()).isEqualTo(1L);
        assertThat(out.title()).isEqualTo("Viagem");

        var captor = ArgumentCaptor.forClass(Album.class);
        verify(albums).save(captor.capture());
        assertThat(captor.getValue().getUser().getId()).isEqualTo(1L);
    }

    @Test
    void listByUser_ok() {
        var a = new Album(); a.setId(1L); a.setTitle("A"); a.setUser(userWithId(9L));
        when(albums.findByUserId(eq(9L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(a)));

        var page = service.listByUser(9L, 0, 10);

        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().get(0).userId()).isEqualTo(9L);
    }

    @Test
    void update_notFound_erro() {
        when(albums.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(99L, new AlbumUpdateRequest("X")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Album não encontrado");
    }
}
