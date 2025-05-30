package com.harmonique.likeservice.service.impl;

import com.harmonique.likeservice.entity.LikedSong;
import com.harmonique.likeservice.repository.LikedSongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LikeServiceImplTest {

    @Mock
    private LikedSongRepository repository;

    @InjectMocks
    private LikeServiceImpl likeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // likeSong - success
    @Test
    void likeSong_ShouldSave_WhenNotAlreadyLiked() {
        Long userId = 1L;
        Long songId = 100L;

        when(repository.existsByUserIdAndSongId(userId, songId)).thenReturn(false);

        likeService.likeSong(userId, songId);

        verify(repository, times(1)).save(argThat(likedSong ->
                likedSong.getUserId().equals(userId) &&
                likedSong.getSongId().equals(songId) &&
                likedSong.getLikedAt() != null
        ));
    }

    // likeSong - failure
    @Test
    void likeSong_ShouldThrowException_WhenAlreadyLiked() {
        Long userId = 2L;
        Long songId = 200L;

        when(repository.existsByUserIdAndSongId(userId, songId)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                likeService.likeSong(userId, songId)
        );

        assertEquals("Song already liked", ex.getMessage());
        verify(repository, never()).save(any());
    }

    // unlikeSong - success
    @Test
    void unlikeSong_ShouldDelete_WhenPreviouslyLiked() {
        Long userId = 3L;
        Long songId = 300L;

        when(repository.existsByUserIdAndSongId(userId, songId)).thenReturn(true);

        likeService.unlikeSong(userId, songId);

        verify(repository, times(1)).deleteByUserIdAndSongId(userId, songId);
    }

    // unlikeSong - failure
    @Test
    void unlikeSong_ShouldThrowException_WhenNotLiked() {
        Long userId = 4L;
        Long songId = 400L;

        when(repository.existsByUserIdAndSongId(userId, songId)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                likeService.unlikeSong(userId, songId)
        );

        assertEquals("Song not liked yet", ex.getMessage());
        verify(repository, never()).deleteByUserIdAndSongId(anyLong(), anyLong());
    }

    // getLikedSongs
    @Test
    void getLikedSongs_ShouldReturnListOfSongIds() {
        Long userId = 5L;

        List<LikedSong> likedSongs = List.of(
                LikedSong.builder().userId(userId).songId(1L).likedAt(LocalDateTime.now()).build(),
                LikedSong.builder().userId(userId).songId(2L).likedAt(LocalDateTime.now()).build()
        );

        when(repository.findByUserId(userId)).thenReturn(likedSongs);

        List<Long> songIds = likeService.getLikedSongs(userId);

        assertEquals(List.of(1L, 2L), songIds);
    }

    // getLikeCount
    @Test
    void getLikeCount_ShouldReturnCorrectCount() {
        Long songId = 600L;

        when(repository.countBySongId(songId)).thenReturn(5L);

        long count = likeService.getLikeCount(songId);

        assertEquals(5L, count);
    }
}