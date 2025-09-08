package argos.com.br.blog.application.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostCreateRequest(
        @NotNull Long userId,
        @NotBlank @Size(min = 3, max = 200) String title,
        @NotBlank String body){
}
