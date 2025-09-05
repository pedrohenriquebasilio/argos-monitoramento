package argos.com.br.crud_basico.infrastructure.persistence;


import argos.com.br.crud_basico.domain.model.Album;
import argos.com.br.crud_basico.domain.repository.AlbumRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaAlbumRepository extends AlbumRepository, JpaRepository<Album, Long> {
    List<Album> findByUserId(Long userId);
}
