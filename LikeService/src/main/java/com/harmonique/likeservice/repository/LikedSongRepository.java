package com.harmonique.likeservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.harmonique.likeservice.entity.LikedSong;

@Repository
public interface LikedSongRepository extends JpaRepository<LikedSong, Long> {
    List<LikedSong> findByUserId(Long userId);
    boolean existsByUserIdAndSongId(Long userId, Long songId);
    void deleteByUserIdAndSongId(Long userId, Long songId);
    long countBySongId(Long songId);
}