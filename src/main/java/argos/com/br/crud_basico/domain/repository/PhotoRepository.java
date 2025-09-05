package argos.com.br.crud_basico.domain.repository;

import argos.com.br.crud_basico.domain.model.Photo;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository {
    Photo save(Photo photo);
    Optional<Photo> findById(Long id);
    List<Photo> findAllByAlbumId(Long albumId);
    void delete(Photo photo);
}