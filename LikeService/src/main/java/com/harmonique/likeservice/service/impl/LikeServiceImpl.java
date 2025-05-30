package com.harmonique.likeservice.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.harmonique.likeservice.entity.LikedSong;
import com.harmonique.likeservice.repository.LikedSongRepository;
import com.harmonique.likeservice.service.LikeService;

import lombok.RequiredArgsConstructor;

/**
 * Implementation of the LikeService interface.
 * Handles business logic for liking, unliking, and retrieving song likes.
 */
@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private static final Logger logger = LoggerFactory.getLogger(LikeServiceImpl.class);
    
    private final LikedSongRepository repository;

    /**
     * Allows a user to like a song.
     * Throws exception if the song is already liked by the user.
     *
     * @param userId ID of the user
     * @param songId ID of the song
     */
    @Override
    public void likeSong(Long userId, Long songId) {
        logger.info("User {} is attempting to like song {}", userId, songId);

        if (repository.existsByUserIdAndSongId(userId, songId)) {
            logger.warn("User {} has already liked song {}", userId, songId);
            throw new RuntimeException("Song already liked");
        }

        LikedSong likedSong = LikedSong.builder()
            .userId(userId)
            .songId(songId)
            .likedAt(LocalDateTime.now())
            .build();

        repository.save(likedSong);
        logger.info("User {} liked song {} successfully", userId, songId);
    }

    /**
     * Allows a user to unlike a previously liked song.
     * Throws exception if the song wasn't liked by the user.
     *
     * @param userId ID of the user
     * @param songId ID of the song
     */
    @Override
    @Transactional
    public void unlikeSong(Long userId, Long songId) {
        logger.info("User {} is attempting to unlike song {}", userId, songId);

        if (!repository.existsByUserIdAndSongId(userId, songId)) {
            logger.warn("User {} tried to unlike song {} which wasn't liked", userId, songId);
            throw new RuntimeException("Song not liked yet");
        }

        repository.deleteByUserIdAndSongId(userId, songId);
        logger.info("User {} unliked song {} successfully", userId, songId);
    }

    /**
     * Retrieves a list of song IDs liked by the user.
     *
     * @param userId ID of the user
     * @return List of song IDs
     */
    @Override
    public List<Long> getLikedSongs(Long userId) {
        logger.info("Fetching liked songs for user {}", userId);

        List<Long> likedSongIds = repository.findByUserId(userId)
                .stream()
                .map(LikedSong::getSongId)
                .toList();

        logger.debug("User {} has liked songs: {}", userId, likedSongIds);
        return likedSongIds;
    }

    /**
     * Returns the number of likes a song has received.
     *
     * @param songId ID of the song
     * @return Count of likes
     */
    @Override
    public long getLikeCount(Long songId) {
        long count = repository.countBySongId(songId);
        logger.info("Song {} has {} likes", songId, count);
        return count;
    }
}