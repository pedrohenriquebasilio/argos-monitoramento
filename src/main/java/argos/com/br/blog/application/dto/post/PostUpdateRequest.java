package argos.com.br.blog.application.dto.post;

import jakarta.validation.constraints.Size;

public record PostUpdateRequest(
        @Size(min = 3, max = 200) String title,
        String body
) {}
