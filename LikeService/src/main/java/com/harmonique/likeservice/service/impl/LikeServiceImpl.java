package com.harmonique.likeservice.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.harmonique.likeservice.entity.LikedSong;
import com.harmonique.likeservice.repository.LikedSongRepository;
import com.harmonique.likeservice.service.LikeService;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikedSongRepository repository;

    @Override
    public void likeSong(Long userId, Long songId) {
        if (repository.existsByUserIdAndSongId(userId, songId)) {
            throw new RuntimeException("Song already liked");
        }
        LikedSong likedSong = LikedSong.builder()
            .userId(userId)
            .songId(songId)
            .likedAt(LocalDateTime.now())
            .build();
        repository.save(likedSong);
    }

    @Override
    @Transactional
    public void unlikeSong(Long userId, Long songId) {
        if (!repository.existsByUserIdAndSongId(userId, songId)) {
            throw new RuntimeException("Song not liked yet");
        }
        repository.deleteByUserIdAndSongId(userId, songId);
    }

    @Override
    public List<Long> getLikedSongs(Long userId) {
        return repository.findByUserId(userId)
                .stream()
                .map(LikedSong::getSongId)
                .toList();
    }

    @Override
    public long getLikeCount(Long songId) {
        return repository.countBySongId(songId);
    }
}