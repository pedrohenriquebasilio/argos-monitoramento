package argos.com.br.blog.infrastructure.persistence;


import argos.com.br.blog.domain.model.Photo;
import argos.com.br.blog.domain.repository.PhotoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaPhotoRepository extends PhotoRepository, JpaRepository<Photo, Long> {
    List<Photo> findAllByAlbumId(Long albumId);
}
