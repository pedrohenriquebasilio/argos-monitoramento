package argos.com.br.blog.application.services.comment;

import argos.com.br.blog.application.dto.comment.CommentCreateRequest;
import argos.com.br.blog.application.dto.comment.CommentResponse;
import argos.com.br.blog.application.dto.comment.CommentUpdateRequest;
import argos.com.br.blog.domain.model.Comment;
import argos.com.br.blog.domain.model.Post;
import argos.com.br.blog.domain.model.User;
import argos.com.br.blog.domain.repository.CommentRepository;
import argos.com.br.blog.infrastructure.logging.LoggerService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class CommentApplicationService {

    private final CommentRepository comments;
    private final LoggerService logger;

    public CommentApplicationService(CommentRepository comments, LoggerService logger) {
        this.comments = comments;
        this.logger = logger;
    }

    public CommentResponse create(CommentCreateRequest in) {
        logger.logRequest("CommentApplicationService", "create", in);
        var c = new Comment();
        var p = new Post();
        p.setId(in.postId());
        c.setBody(in.body());
        c.setUserId(in.userId());
        c.setPost(p);
        var saved = comments.save(c);
        CommentResponse response = toResponse(saved);
        logger.logResponse("CommentApplicationService", "create", response);
        return response;
    }

    public Page<CommentResponse> list(int page, int size) {
        logger.logRequest("CommentApplicationService", "list", "page: " + page + ", size: " + size);
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<CommentResponse> response = comments.findAll(pageable).map(this::toResponse);
        logger.logResponse("CommentApplicationService", "list", "Total elements: " + response.getTotalElements());
        return response;
    }

    public Page<CommentResponse> listByPost(Long postId, int page, int size) {
        logger.logRequest("CommentApplicationService", "listByPost", "postId: " + postId + ", page: " + page + ", size: " + size);
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<CommentResponse> response = comments.findByPostId(postId, pageable).map(this::toResponse);
        logger.logResponse("CommentApplicationService", "listByPost", "Total elements: " + response.getTotalElements());
        return response;
    }

    public Page<CommentResponse> listByUser(Long userId, int page, int size) {
        logger.logRequest("CommentApplicationService", "listByUser", "userId: " + userId + ", page: " + page + ", size: " + size);
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<CommentResponse> response = comments.findByUserId(userId, pageable).map(this::toResponse);
        logger.logResponse("CommentApplicationService", "listByUser", "Total elements: " + response.getTotalElements());
        return response;
    }

    public CommentResponse get(Long id) {
        logger.logRequest("CommentApplicationService", "get", "id: " + id);
        var c = comments.findById(id).orElseThrow(() -> new IllegalArgumentException("Comment não encontrado"));
        CommentResponse response = toResponse(c);
        logger.logResponse("CommentApplicationService", "get", response);
        return response;
    }

    public CommentResponse update(Long id, CommentUpdateRequest in) {
        logger.logRequest("CommentApplicationService", "update", "id: " + id + ", request: " + in);
        var c = comments.findById(id).orElseThrow(() -> new IllegalArgumentException("Comment não encontrado"));
        if (in.body() != null && !in.body().isBlank()) c.setBody(in.body());
        var saved = comments.save(c);
        CommentResponse response = toResponse(saved);
        logger.logResponse("CommentApplicationService", "update", response);
        return response;
    }

    public void delete(Long id) {
        logger.logRequest("CommentApplicationService", "delete", "id: " + id);
        var c = comments.findById(id).orElseThrow(() -> new IllegalArgumentException("Comment não encontrado"));
        comments.delete(c);
        logger.logResponse("CommentApplicationService", "delete", "Comment deleted: " + id);
    }

    private CommentResponse toResponse(Comment c) {
        return new CommentResponse(c.getId(), c.getPost().getId(), c.getUserId(), c.getBody(), c.getCreatedAt());
    }
}
