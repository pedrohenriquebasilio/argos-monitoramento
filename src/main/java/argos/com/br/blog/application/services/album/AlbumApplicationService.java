package argos.com.br.blog.application.services.album;

import argos.com.br.blog.application.dto.album.AlbumCreateRequest;
import argos.com.br.blog.application.dto.album.AlbumResponse;
import argos.com.br.blog.application.dto.album.AlbumUpdateRequest;
import argos.com.br.blog.domain.model.Album;
import argos.com.br.blog.domain.model.User;
import argos.com.br.blog.domain.repository.AlbumRepository;
import argos.com.br.blog.infrastructure.logging.LoggerService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class AlbumApplicationService {

    private final AlbumRepository albums;
    private final LoggerService logger;

    public AlbumApplicationService(AlbumRepository albums, LoggerService logger) {
        this.albums = albums;
        this.logger = logger;
    }

    public AlbumResponse create(AlbumCreateRequest in) {
        logger.logRequest("AlbumApplicationService", "create", in);
        var a = new Album();
        var u = new User();
        u.setId(in.userId());
        a.setUser(u);
        a.setTitle(in.title());
        var saved = albums.save(a);
        AlbumResponse response = toResponse(saved);
        logger.logResponse("AlbumApplicationService", "create", response);
        return response;
    }

    public Page<AlbumResponse> list(int page, int size) {
        logger.logRequest("AlbumApplicationService", "list", "page: " + page + ", size: " + size);
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<AlbumResponse> response = albums.findAll(pageable).map(this::toResponse);
        logger.logResponse("AlbumApplicationService", "list", "Total elements: " + response.getTotalElements());
        return response;
    }

    public Page<AlbumResponse> listByUser(Long userId, int page, int size) {
        logger.logRequest("AlbumApplicationService", "listByUser", "userId: " + userId + ", page: " + page + ", size: " + size);
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<AlbumResponse> response = albums.findByUserId(userId, pageable).map(this::toResponse);
        logger.logResponse("AlbumApplicationService", "listByUser", "Total elements: " + response.getTotalElements());
        return response;
    }

    public AlbumResponse get(Long id) {
        logger.logRequest("AlbumApplicationService", "get", "id: " + id);
        var a = albums.findById(id).orElseThrow(() -> new IllegalArgumentException("Album não encontrado"));
        AlbumResponse response = toResponse(a);
        logger.logResponse("AlbumApplicationService", "get", response);
        return response;
    }

    public AlbumResponse update(Long id, AlbumUpdateRequest in) {
        logger.logRequest("AlbumApplicationService", "update", "id: " + id + ", request: " + in);
        var a = albums.findById(id).orElseThrow(() -> new IllegalArgumentException("Album não encontrado"));
        if (in.title() != null && !in.title().isBlank()) a.setTitle(in.title());
        var saved = albums.save(a);
        AlbumResponse response = toResponse(saved);
        logger.logResponse("AlbumApplicationService", "update", response);
        return response;
    }

    public void delete(Long id) {
        logger.logRequest("AlbumApplicationService", "delete", "id: " + id);
        var a = albums.findById(id).orElseThrow(() -> new IllegalArgumentException("Album não encontrado"));
        albums.delete(a);
        logger.logResponse("AlbumApplicationService", "delete", "Album deleted: " + id);
    }

    private AlbumResponse toResponse(Album a) {
        return new AlbumResponse(a.getId(), a.getUser().getId(), a.getTitle(), a.getCreatedAt());
    }
}
