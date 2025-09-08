package argos.com.br.blog.users;

import argos.com.br.blog.application.dto.auth.UserCreateRequest;
import argos.com.br.blog.application.dto.auth.UserUpdateRequest;
import argos.com.br.blog.application.services.user.UserApplicationService;
import argos.com.br.blog.domain.model.User;
import argos.com.br.blog.domain.repository.UserRepository;
import argos.com.br.blog.infrastructure.logging.LoggerService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserApplicationServiceTest {

    @Mock
    UserRepository users;
    @Mock PasswordEncoder encoder;
    @Mock
    LoggerService logger;
    @InjectMocks
    UserApplicationService service;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new UserApplicationService(users, encoder, logger);
    }

    @Test
    void create_ok() {
        var in = new UserCreateRequest("Pedro", "pedro@mail.com", "123456");
        when(users.findByEmail("pedro@mail.com")).thenReturn(Optional.empty());
        when(encoder.encode("123456")).thenReturn("HASH");
        var saved = new User(); saved.setId(10L); saved.setName("Pedro"); saved.setEmail("pedro@mail.com"); saved.setPassword("HASH");
        when(users.save(any(User.class))).thenReturn(saved);

        var out = service.create(in);

        assertThat(out.id()).isEqualTo(10L);
        assertThat(out.email()).isEqualTo("pedro@mail.com");
        verify(users).save(any(User.class));
    }

    @Test
    void create_emailDuplicado_erro() {
        var in = new UserCreateRequest("Pedro", "ja@mail.com", "123456");
        when(users.findByEmail("ja@mail.com"))
                .thenReturn(Optional.of(new User()));


        assertThatThrownBy(() -> service.create(in))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email já cadastrado");
        verify(users, never()).save(any());
    }

    @Test
    void list_paginado_ok() {
        var u = new User(); u.setId(1L); u.setName("A"); u.setEmail("a@mail.com");
        var page = new PageImpl<>(List.of(u), PageRequest.of(0,10), 1);
        when(users.findAll(any(Pageable.class))).thenReturn(page);

        var out = service.list(0, 10);

        assertThat(out.getTotalElements()).isEqualTo(1);
        assertThat(out.getContent().get(0).email()).isEqualTo("a@mail.com");
    }

    @Test
    void update_ok() {
        var u = new User(); u.setId(1L); u.setName("Old"); u.setEmail("old@mail.com"); u.setPassword("P");
        when(users.findById(1L)).thenReturn(Optional.of(u));
        when(users.existsByEmail("new@mail.com")).thenReturn(false);
        when(users.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        var out = service.update(1L, new UserUpdateRequest("New", "new@mail.com", "abc12345"));

        assertThat(out.name()).isEqualTo("New");
        assertThat(out.email()).isEqualTo("new@mail.com");
        verify(encoder).encode("abc12345");
    }

    @Test
    void delete_naoEncontrado_erro() {
        when(users.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Usuário não encontrado");
    }
}
