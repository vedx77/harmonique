package com.harmonique.likeservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.harmonique.likeservice.entity.LikedSong;

/**
 * Repository interface for LikedSong entity.
 * Provides basic CRUD operations and custom queries for like functionality.
 */
@Repository
public interface LikedSongRepository extends JpaRepository<LikedSong, Long> {

    /**
     * Finds all songs liked by a specific user.
     * 
     * @param userId ID of the user
     * @return List of LikedSong entities
     */
    List<LikedSong> findByUserId(Long userId);

    /**
     * Checks if a user has already liked a particular song.
     * 
     * @param userId ID of the user
     * @param songId ID of the song
     * @return true if the like exists, false otherwise
     */
    boolean existsByUserIdAndSongId(Long userId, Long songId);

    /**
     * Deletes a like by user and song ID.
     * 
     * @param userId ID of the user
     * @param songId ID of the song
     */
    void deleteByUserIdAndSongId(Long userId, Long songId);

    /**
     * Counts the total number of likes for a given song.
     * 
     * @param songId ID of the song
     * @return Number of likes
     */
    long countBySongId(Long songId);
}