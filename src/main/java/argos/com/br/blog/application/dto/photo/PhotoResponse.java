package argos.com.br.blog.application.dto.photo;
import java.time.LocalDateTime;

public record PhotoResponse(
        Long id,
        Long albumId,
        String title,
        String url,
        String thumbnailUrl,
        LocalDateTime createdAt
) {}