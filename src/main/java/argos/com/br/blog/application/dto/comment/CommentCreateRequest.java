package argos.com.br.blog.application.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentCreateRequest(
        @NotNull Long postId,
        @NotNull Long userId,
        @NotBlank String body
) {}