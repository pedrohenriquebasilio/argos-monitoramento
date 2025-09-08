package argos.com.br.blog.application.dto.comment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        Long postId,
        Long userId,
        String body,
        LocalDateTime createdAt
) {}
