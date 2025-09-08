package argos.com.br.blog.application.dto.album;

import java.time.LocalDateTime;

public record AlbumResponse(
        Long id,
        Long userId,
        String title,
        LocalDateTime createdAt
) {}
