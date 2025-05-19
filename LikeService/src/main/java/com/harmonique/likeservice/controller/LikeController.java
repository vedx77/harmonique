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

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<String> likeSong(@RequestParam Long userId, @RequestParam Long songId) {
        likeService.likeSong(userId, songId);
        return ResponseEntity.ok("Song liked successfully");
    }

    @DeleteMapping("/{songId}")
    public ResponseEntity<String> unlikeSong(@RequestParam Long userId, @PathVariable Long songId) {
        likeService.unlikeSong(userId, songId);
        return ResponseEntity.ok("Song unliked successfully");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Long>> getLikedSongs(@PathVariable Long userId) {
        return ResponseEntity.ok(likeService.getLikedSongs(userId));
    }

    @GetMapping("/song/{songId}")
    public ResponseEntity<Long> getLikeCount(@PathVariable Long songId) {
        return ResponseEntity.ok(likeService.getLikeCount(songId));
    }
}