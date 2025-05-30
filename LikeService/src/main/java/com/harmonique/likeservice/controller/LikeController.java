package com.harmonique.likeservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.harmonique.likeservice.service.LikeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // For logging

/**
 * REST Controller for handling Like-related operations such as liking, unliking,
 * retrieving liked songs for a user, and getting the like count for a song.
 */
@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
@Slf4j
public class LikeController {

    // Service to handle the business logic for likes
    private final LikeService likeService;

    /**
     * Like a song for a specific user.
     * 
     * @param userId The ID of the user who is liking the song
     * @param songId The ID of the song to be liked
     * @return Success message
     */
    @PostMapping
    public ResponseEntity<String> likeSong(@RequestParam Long userId, @RequestParam Long songId) {
        log.info("Received request to like song with ID {} by user with ID {}", songId, userId);
        likeService.likeSong(userId, songId);
        log.info("User {} liked song {}", userId, songId);
        return ResponseEntity.ok("Song liked successfully");
    }

    /**
     * Unlike a previously liked song for a specific user.
     * 
     * @param userId The ID of the user who wants to unlike the song
     * @param songId The ID of the song to be unliked
     * @return Success message
     */
    @DeleteMapping("/{songId}")
    public ResponseEntity<String> unlikeSong(@RequestParam Long userId, @PathVariable Long songId) {
        log.info("Received request to unlike song with ID {} by user with ID {}", songId, userId);
        likeService.unlikeSong(userId, songId);
        log.info("User {} unliked song {}", userId, songId);
        return ResponseEntity.ok("Song unliked successfully");
    }

    /**
     * Get all liked song IDs for a specific user.
     * 
     * @param userId The ID of the user
     * @return List of song IDs that the user has liked
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Long>> getLikedSongs(@PathVariable Long userId) {
        log.info("Fetching liked songs for user with ID {}", userId);
        List<Long> likedSongs = likeService.getLikedSongs(userId);
        log.info("User {} has liked {} songs", userId, likedSongs.size());
        return ResponseEntity.ok(likedSongs);
    }

    /**
     * Get the total number of likes for a specific song.
     * 
     * @param songId The ID of the song
     * @return Number of likes for the song
     */
    @GetMapping("/song/{songId}")
    public ResponseEntity<Long> getLikeCount(@PathVariable Long songId) {
        log.info("Fetching like count for song with ID {}", songId);
        Long count = likeService.getLikeCount(songId);
        log.info("Song {} has {} likes", songId, count);
        return ResponseEntity.ok(count);
    }
}