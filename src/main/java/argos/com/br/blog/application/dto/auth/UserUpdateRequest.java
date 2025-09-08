package argos.com.br.blog.application.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Size(min = 2, max = 100) String name,
        @Email String email,
        @Size(min = 6, max = 100)String password
) {
}
