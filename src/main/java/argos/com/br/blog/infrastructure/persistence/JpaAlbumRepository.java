package argos.com.br.blog.infrastructure.persistence;


import argos.com.br.blog.domain.model.Album;
import argos.com.br.blog.domain.repository.AlbumRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaAlbumRepository extends AlbumRepository, JpaRepository<Album, Long> {
    List<Album> findByUserId(Long userId);
}
