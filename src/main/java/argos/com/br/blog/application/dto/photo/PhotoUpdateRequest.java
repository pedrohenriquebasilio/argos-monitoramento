package argos.com.br.blog.application.dto.photo;

import jakarta.validation.constraints.Size;

public record PhotoUpdateRequest(
        @Size(min=3, max=200) String title,
        String url,
        String thumbnailUrl
) {}