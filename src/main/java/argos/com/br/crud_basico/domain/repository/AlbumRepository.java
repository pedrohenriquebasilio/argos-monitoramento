package argos.com.br.crud_basico.domain.repository;

import argos.com.br.crud_basico.domain.model.Album;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository {
    Album save(Album album);
    Optional<Album> findById(Long id);
    List<Album> findAll();
    List<Album> findByUserId(Long userId);
    void delete(Album album);
}