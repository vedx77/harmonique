package com.harmonique.likeservice.controller;

import com.harmonique.likeservice.service.LikeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LikeController.class)
class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LikeService likeService;

    @Test
    @WithMockUser
    void likeSong_shouldReturnSuccessMessage() throws Exception {
        Mockito.doNothing().when(likeService).likeSong(1L, 100L);

        mockMvc.perform(post("/api/likes")
                        .param("userId", "1")
                        .param("songId", "100")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Song liked successfully"));
    }

    @Test
    @WithMockUser
    void unlikeSong_shouldReturnSuccessMessage() throws Exception {
        Mockito.doNothing().when(likeService).unlikeSong(1L, 100L);

        mockMvc.perform(delete("/api/likes/100")
                        .param("userId", "1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Song unliked successfully"));
    }

    @Test
    @WithMockUser
    void getLikedSongs_shouldReturnListOfSongIds() throws Exception {
        List<Long> likedSongs = Arrays.asList(100L, 101L);
        Mockito.when(likeService.getLikedSongs(1L)).thenReturn(likedSongs);

        mockMvc.perform(get("/api/likes/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0]").value(100))
                .andExpect(jsonPath("$[1]").value(101));
    }

    @Test
    @WithMockUser
    void getLikeCount_shouldReturnCount() throws Exception {
        Mockito.when(likeService.getLikeCount(100L)).thenReturn(5L);

        mockMvc.perform(get("/api/likes/song/100"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }
}