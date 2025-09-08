package argos.com.br.blog.application.dto.todo;


import java.time.LocalDateTime;

public record TodoResponse(
        Long id,
        Long userId,
        String title,
        boolean completed,
        LocalDateTime createdAt
) {}