package argos.com.br.blog.domain.repository;

import argos.com.br.blog.domain.model.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository {
    Photo save(Photo photo);
    Optional<Photo> findById(Long id);
    Page<Photo> findByAlbumId(Long albumId, Pageable pageable);
    Page<Photo> findAll(Pageable pageable);
    void delete(Photo photo);
}