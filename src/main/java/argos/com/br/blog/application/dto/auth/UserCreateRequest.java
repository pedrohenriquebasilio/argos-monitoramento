package argos.com.br.blog.application.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank @Size(min = 2, max = 100) String name,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6, max = 100) String passwrd
) { }
