package com.harmonique.songservice.repository;

import com.harmonique.songservice.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Song entity.
 * Provides CRUD operations and custom search methods using Spring Data JPA.
 */
@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    /**
     * Searches for songs where the title, artist, or album contains the specified keyword (case-insensitive).
     *
     * @param title  Partial or full song title
     * @param artist Partial or full artist name
     * @param album  Partial or full album name
     * @return List of matching songs
     */
    List<Song> findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCaseOrAlbumContainingIgnoreCase(
            String title, String artist, String album
    );
}