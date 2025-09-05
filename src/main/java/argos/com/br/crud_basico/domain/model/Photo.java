package argos.com.br.crud_basico.domain.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String url;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;
}
