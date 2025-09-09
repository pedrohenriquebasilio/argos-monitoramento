package argos.com.br.blog.photos;

import argos.com.br.blog.application.dto.photo.PhotoCreateRequest;
import argos.com.br.blog.application.dto.photo.PhotoUpdateRequest;
import argos.com.br.blog.application.services.photo.PhotoApplicationService;
import argos.com.br.blog.domain.model.Album;
import argos.com.br.blog.domain.model.Photo;
import argos.com.br.blog.domain.repository.PhotoRepository;
import argos.com.br.blog.infrastructure.logging.LoggerService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class PhotoApplicationServiceTest {

    @Mock
    PhotoRepository photos;
    @InjectMocks
    PhotoApplicationService service;
    @Mock
    LoggerService logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new PhotoApplicationService(photos, logger);
    }

    private static Album albumWithId(Long id){ var a = new Album(); a.setId(id); return a; }

    @Test
    void create_ok() {
        var in = new PhotoCreateRequest(7L, "Capa", "http://img/1.jpg", "http://img/1_thumb.jpg");

        var p = new Photo();
        p.setId(10L);
        p.setAlbum(albumWithId(7L));
        p.setTitle("Capa");
        p.setUrl("http://img/1.jpg");
        p.setThumbnailUrl("http://img/1_thumb.jpg");

        when(photos.save(any(Photo.class))).thenReturn(p);

        var out = service.create(in);

        assertThat(out.id()).isEqualTo(10L);
        assertThat(out.albumId()).isEqualTo(7L);
        assertThat(out.title()).isEqualTo("Capa");

        var captor = ArgumentCaptor.forClass(Photo.class);
        verify(photos).save(captor.capture());
        assertThat(captor.getValue().getAlbum().getId()).isEqualTo(7L);
    }

    @Test
    void listByAlbum_ok() {
        var p = new Photo(); p.setId(1L); p.setAlbum(albumWithId(5L)); p.setTitle("t");
        when(photos.findByAlbumId(eq(5L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(p)));

        var page = service.listByAlbum(5L, 0, 10);

        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().get(0).albumId()).isEqualTo(5L);
    }

    @Test
    void list_ok() {
        var p = new Photo(); p.setId(1L); p.setAlbum(albumWithId(2L)); p.setTitle("img");
        when(photos.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(p)));

        var page = service.list(0, 10);

        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().get(0).albumId()).isEqualTo(2L);
    }

    @Test
    void update_notFound_erro() {
        when(photos.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(99L, new PhotoUpdateRequest("t", null, null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Photo não encontrada");
    }

    @Test
    void update_ok() {
        var p = new Photo(); p.setId(1L); p.setAlbum(albumWithId(2L)); p.setTitle("Old");
        when(photos.findById(1L)).thenReturn(Optional.of(p));
        when(photos.save(any(Photo.class))).thenAnswer(i -> i.getArgument(0));

        var out = service.update(1L, new PhotoUpdateRequest("New", "url", "thumb"));

        assertThat(out.title()).isEqualTo("New");
        assertThat(out.url()).isEqualTo("url");
        assertThat(out.thumbnailUrl()).isEqualTo("thumb");
    }

    @Test
    void get_ok() {
        var p = new Photo(); p.setId(1L); p.setAlbum(albumWithId(2L)); p.setTitle("img");
        when(photos.findById(1L)).thenReturn(Optional.of(p));

        var out = service.get(1L);

        assertThat(out.id()).isEqualTo(1L);
        assertThat(out.albumId()).isEqualTo(2L);
    }

    @Test
    void get_notFound_erro() {
        when(photos.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Photo não encontrada");
    }

    @Test
    void delete_ok() {
        var p = new Photo(); p.setId(1L);
        when(photos.findById(1L)).thenReturn(Optional.of(p));

        service.delete(1L);

        verify(photos).delete(p);
    }

    @Test
    void delete_notFound_erro() {
        when(photos.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Photo não encontrada");
    }
}
