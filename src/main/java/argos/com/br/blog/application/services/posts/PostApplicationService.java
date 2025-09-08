package argos.com.br.blog.application.services.posts;


import argos.com.br.blog.application.dto.post.PostCreateRequest;
import argos.com.br.blog.application.dto.post.PostResponse;
import argos.com.br.blog.application.dto.post.PostUpdateRequest;
import argos.com.br.blog.domain.model.Post;
import argos.com.br.blog.domain.model.User;
import argos.com.br.blog.domain.repository.PostRepository;
import argos.com.br.blog.infrastructure.logging.LoggerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PostApplicationService {

    private final PostRepository posts;
    private final LoggerService logger;

    public PostApplicationService(PostRepository posts, LoggerService logger) {
        this.posts = posts;
        this.logger = logger;
    }

    public PostResponse create(PostCreateRequest in){
        logger.logRequest("PostApplicationService", "create", in);
        var p = new Post();
        var u = new User();
        u.setId(in.userId());
        p.setUser(u);
        p.setTitle(in.title());
        p.setBody(in.body());
        var saved = posts.save(p);

        PostResponse response = toResponse(saved);
        logger.logResponse("PostApplicationService", "create", response);
        return response;
    }

    public Page<PostResponse> list(int page, int size) {
        logger.logRequest("PostApplicationService", "list", "page: " + page + ", size: " + size);
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<PostResponse> response = posts.findAll(pageable).map(this::toResponse);
        logger.logResponse("PostApplicationService", "list", "Total elements: " + response.getTotalElements());
        return response;
    }

    public Page<PostResponse> listByUser(Long userId, int page, int size) {
        logger.logRequest("PostApplicationService", "listByUser", "userId: " + userId + ", page: " + page + ", size: " + size);
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<PostResponse> response = posts.findByUserId(userId, pageable).map(this::toResponse);
        logger.logResponse("PostApplicationService", "listByUser", "Total elements: " + response.getTotalElements());
        return response;
    }

    private PostResponse toResponse(Post p) {
        return new PostResponse(p.getId(),p.getUser().getId(), p.getTitle(), p.getBody(), p.getCreatedAt());
    }

    public PostResponse get(Long id) {
        logger.logRequest("PostApplicationService", "get", "id: " + id);
        var p = posts.findById(id).orElseThrow(() -> new IllegalArgumentException("Post não encontrado"));
        PostResponse response = toResponse(p);
        logger.logResponse("PostApplicationService", "get", response);
        return response;
    }

    public PostResponse update(Long id, PostUpdateRequest in) {
        logger.logRequest("PostApplicationService", "update", "id: " + id + ", request: " + in);
        var p = posts.findById(id).orElseThrow(() -> new IllegalArgumentException("Post não encontrado"));
        if (in.title() != null && !in.title().isBlank()) p.setTitle(in.title());
        if (in.body() != null && !in.body().isBlank()) p.setBody(in.body());
        var saved = posts.save(p);
        PostResponse response = toResponse(saved);
        logger.logResponse("PostApplicationService", "update", response);
        return response;
    }

    public void delete(Long id) {
        logger.logRequest("PostApplicationService", "delete", "id: " + id);
        var p = posts.findById(id).orElseThrow(() -> new IllegalArgumentException("Post não encontrado"));
        posts.delete(p);
        logger.logResponse("PostApplicationService", "delete", "Post deleted: " + id);
    }

}
