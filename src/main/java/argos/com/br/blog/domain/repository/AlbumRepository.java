package argos.com.br.blog.domain.repository;

import argos.com.br.blog.domain.model.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository {
    Album save(Album album);
    Optional<Album> findById(Long id);
    Page<Album> findAll(Pageable pageable);
    Page<Album> findByUserId(Long userId, Pageable pageable);
    void delete(Album album);
}