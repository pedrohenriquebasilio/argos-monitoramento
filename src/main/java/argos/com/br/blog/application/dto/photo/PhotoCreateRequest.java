package argos.com.br.blog.application.dto.photo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PhotoCreateRequest(
        @NotNull Long albumId,
        @NotBlank @Size(min=3, max=200) String title,
        @NotBlank String url,
        String thumbnailUrl
) {}
