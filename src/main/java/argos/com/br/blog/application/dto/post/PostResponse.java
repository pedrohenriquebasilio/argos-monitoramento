package argos.com.br.blog.application.dto.post;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        Long userId,
        String title,
        String body,
        LocalDateTime createdAt
) {}