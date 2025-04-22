package com.harmonique.songservice.service;

import com.harmonique.songservice.payload.SongRequest;
import com.harmonique.songservice.payload.SongResponse;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface SongService {

    // Core CRUD Operations
    SongResponse createSong(SongRequest songRequest);

    SongResponse updateSong(SongRequest songRequest, Long songId);

    void deleteSong(Long songId);

    SongResponse getSongById(Long songId);

    List<SongResponse> getAllSongs();

    // Upload with manual metadata (provided by user)
    SongResponse uploadWithManualMetadata(MultipartFile file, String title, String artist, String album, 
                                          String genre, String language, String description, String uploadedBy);

    // Upload with auto metadata (extracted from file)
    SongResponse uploadWithAutoMetadata(MultipartFile file, String uploadedBy);
    
    byte[] downloadSongFile(Long songId);

	List<SongResponse> searchSongs(String keyword);
}