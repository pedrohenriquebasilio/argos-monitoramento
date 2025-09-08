package argos.com.br.blog.application.dto.album;

import jakarta.validation.constraints.Size;

public record AlbumUpdateRequest(
        @Size(min=3, max=200) String title
) {}
