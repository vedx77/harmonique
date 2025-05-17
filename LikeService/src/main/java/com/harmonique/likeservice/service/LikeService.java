package com.harmonique.likeservice.service;

import java.util.List;

public interface LikeService {
    void likeSong(Long userId, Long songId);
    void unlikeSong(Long userId, Long songId);
    List<Long> getLikedSongs(Long userId);
    long getLikeCount(Long songId);
}