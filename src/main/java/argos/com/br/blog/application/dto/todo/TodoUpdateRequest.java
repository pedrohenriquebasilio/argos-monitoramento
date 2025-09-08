package argos.com.br.blog.application.dto.todo;

import jakarta.validation.constraints.Size;

public record TodoUpdateRequest(
        @Size(min=3, max=200) String title,
        Boolean completed
) {}