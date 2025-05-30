package com.harmonique.likeservice.service;

import java.util.List;

/**
 * Service interface for handling like-related operations.
 * Defines the contract for liking/unliking songs, retrieving liked songs,
 * and getting the total like count for a song.
 */
public interface LikeService {

    /**
     * Allows a user to like a song.
     *
     * @param userId The ID of the user who likes the song
     * @param songId The ID of the song to be liked
     */
    void likeSong(Long userId, Long songId);

    /**
     * Allows a user to unlike a previously liked song.
     *
     * @param userId The ID of the user who wants to unlike the song
     * @param songId The ID of the song to be unliked
     */
    void unlikeSong(Long userId, Long songId);

    /**
     * Retrieves the list of song IDs that a user has liked.
     *
     * @param userId The ID of the user
     * @return A list of song IDs liked by the user
     */
    List<Long> getLikedSongs(Long userId);

    /**
     * Gets the total number of likes for a specific song.
     *
     * @param songId The ID of the song
     * @return The count of likes for the song
     */
    long getLikeCount(Long songId);
}