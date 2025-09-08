package argos.com.br.blog.application.services.photo;
import argos.com.br.blog.application.dto.photo.PhotoCreateRequest;
import argos.com.br.blog.application.dto.photo.PhotoResponse;
import argos.com.br.blog.application.dto.photo.PhotoUpdateRequest;
import argos.com.br.blog.domain.model.Album;
import argos.com.br.blog.domain.model.Photo;
import argos.com.br.blog.domain.repository.PhotoRepository;
import argos.com.br.blog.infrastructure.logging.LoggerService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class PhotoApplicationService {

    private final PhotoRepository photos;
    private final LoggerService logger;

    public PhotoApplicationService(PhotoRepository photos, LoggerService logger) {
        this.photos = photos;
        this.logger = logger;
    }

    public PhotoResponse create(PhotoCreateRequest in) {
        logger.logRequest("PhotoApplicationService", "create", in);
        var p = new Photo();
        var a = new Album();
        a.setId(in.albumId());
        p.setAlbum(a);
        p.setTitle(in.title());
        p.setUrl(in.url());
        p.setThumbnailUrl(in.thumbnailUrl());
        var saved = photos.save(p);
        PhotoResponse response = toResponse(saved);
        logger.logResponse("PhotoApplicationService", "create", response);
        return response;
    }

    public Page<PhotoResponse> list(int page, int size) {
        logger.logRequest("PhotoApplicationService", "list", "page: " + page + ", size: " + size);
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<PhotoResponse> response = photos.findAll(pageable).map(this::toResponse);
        logger.logResponse("PhotoApplicationService", "list", "Total elements: " + response.getTotalElements());
        return response;
    }

    public Page<PhotoResponse> listByAlbum(Long albumId, int page, int size) {
        logger.logRequest("PhotoApplicationService", "listByAlbum", "albumId: " + albumId + ", page: " + page + ", size: " + size);
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<PhotoResponse> response = photos.findByAlbumId(albumId, pageable).map(this::toResponse);
        logger.logResponse("PhotoApplicationService", "listByAlbum", "Total elements: " + response.getTotalElements());
        return response;
    }

    public PhotoResponse get(Long id) {
        logger.logRequest("PhotoApplicationService", "get", "id: " + id);
        var p = photos.findById(id).orElseThrow(() -> new IllegalArgumentException("Photo não encontrada"));
        PhotoResponse response = toResponse(p);
        logger.logResponse("PhotoApplicationService", "get", response);
        return response;
    }

    public PhotoResponse update(Long id, PhotoUpdateRequest in) {
        logger.logRequest("PhotoApplicationService", "update", "id: " + id + ", request: " + in);
        var p = photos.findById(id).orElseThrow(() -> new IllegalArgumentException("Photo não encontrada"));
        if (in.title() != null && !in.title().isBlank()) p.setTitle(in.title());
        if (in.url() != null && !in.url().isBlank()) p.setUrl(in.url());
        if (in.thumbnailUrl() != null && !in.thumbnailUrl().isBlank()) p.setThumbnailUrl(in.thumbnailUrl());
        var saved = photos.save(p);
        PhotoResponse response = toResponse(saved);
        logger.logResponse("PhotoApplicationService", "update", response);
        return response;
    }

    public void delete(Long id) {
        logger.logRequest("PhotoApplicationService", "delete", "id: " + id);
        var p = photos.findById(id).orElseThrow(() -> new IllegalArgumentException("Photo não encontrada"));
        photos.delete(p);
        logger.logResponse("PhotoApplicationService", "delete", "Photo deleted: " + id);
    }

    private PhotoResponse toResponse(Photo p) {
        return new PhotoResponse(p.getId(), p.getAlbum().getId(), p.getTitle(), p.getUrl(), p.getThumbnailUrl(), p.getCreatedAt());
    }
}

