package com.harmonique.songservice.controller;

import com.harmonique.songservice.payload.ApiResponse;
import com.harmonique.songservice.payload.SongRequest;
import com.harmonique.songservice.payload.SongResponse;
import com.harmonique.songservice.security.JwtTokenHelper;
import com.harmonique.songservice.service.SongService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;

import java.util.List;

@RestController
@RequestMapping("/songs")
@Tag(name = "Songs", description = "Song Management APIs")
@SecurityRequirement(name = "bearerAuth")
public class SongController {

    private static final Logger logger = LoggerFactory.getLogger(SongController.class);

    @Autowired
    private SongService songService;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    // ------------------------------------------
    // Basic CRUD APIs
    // ------------------------------------------

    @PostMapping
    @Operation(summary = "Create a new song manually (without upload)")
    public ResponseEntity<SongResponse> createSong(@RequestBody @Valid SongRequest songRequest) {
        logger.info("Creating song with title: {}", songRequest.getTitle());
        SongResponse songResponse = songService.createSong(songRequest);
        logger.debug("Song created successfully: {}", songResponse);
        return new ResponseEntity<>(songResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update song metadata by ID")
    public ResponseEntity<SongResponse> updateSong(@Valid @RequestBody SongRequest request, @PathVariable Long id) {
        logger.info("Updating song with ID: {}", id);
        SongResponse response = songService.updateSong(request, id);
        logger.debug("Song updated: {}", response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete song by ID")
    public ResponseEntity<ApiResponse> deleteSong(@PathVariable Long id) {
        logger.warn("Deleting song with ID: {}", id);
        songService.deleteSong(id);
        return new ResponseEntity<>(
                new ApiResponse("Song deleted successfully", true, HttpStatus.OK),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get song details by ID")
    public ResponseEntity<SongResponse> getSongById(@PathVariable Long id) {
        logger.info("Fetching song with ID: {}", id);
        return ResponseEntity.ok(songService.getSongById(id));
    }

    @GetMapping
    @Operation(summary = "Get all uploaded songs")
    public ResponseEntity<List<SongResponse>> getAllSongs() {
        logger.info("Fetching all songs");
        return ResponseEntity.ok(songService.getAllSongs());
    }

    // ------------------------------------------
    // Upload APIs
    // ------------------------------------------

    @PostMapping(value = "/upload/auto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a song file and extract metadata automatically")
    public ResponseEntity<SongResponse> uploadWithAutoMetadata(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("Authorization") String bearerToken
    ) {
        String token = bearerToken.replace("Bearer ", "");
        String uploadedBy = jwtTokenHelper.getUsernameFromToken(token);

        logger.info("Uploading file with auto metadata by: {}", uploadedBy);
        SongResponse songResponse = songService.uploadWithAutoMetadata(file, uploadedBy);
        logger.debug("Auto upload complete: {}", songResponse.getTitle());

        return new ResponseEntity<>(songResponse, HttpStatus.CREATED);
    }

    @PostMapping(value = "/upload/manual", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a song file with manual metadata input")
    public ResponseEntity<SongResponse> uploadWithManualMetadata(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("artist") String artist,
            @RequestParam("album") String album,
            @RequestParam("genre") String genre,
            @RequestParam("language") String language,
            @RequestParam("description") String description,
            @RequestHeader("Authorization") String bearerToken
    ) {
        String token = bearerToken.replace("Bearer ", "");
        String uploadedBy = jwtTokenHelper.getUsernameFromToken(token);

        logger.info("Uploading file with manual metadata by: {}", uploadedBy);
        SongResponse songResponse = songService.uploadWithManualMetadata(
                file, title, artist, album, genre, language, description, uploadedBy
        );
        logger.debug("Manual upload complete: {}", songResponse.getTitle());

        return new ResponseEntity<>(songResponse, HttpStatus.CREATED);
    }

    // ------------------------------------------
    // Download API
    // ------------------------------------------

    @GetMapping("/download/{id}")
    @Operation(summary = "Download song file by ID")
    public ResponseEntity<byte[]> downloadSongFile(@PathVariable Long id) {
        logger.info("Request to download song file with ID: {}", id);

        byte[] fileData = songService.downloadSongFile(id); // your method returns byte[]
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "song-" + id + ".mp3");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(fileData);
    }

    // ------------------------------------------
    // Search Song API
    // ------------------------------------------
    
    @GetMapping("/search")
    @Operation(summary = "Search songs by title, artist, or album")
    public ResponseEntity<List<SongResponse>> searchSongs(@RequestParam("query") String query) {
        logger.info("Searching songs with keyword: {}", query);
        List<SongResponse> results = songService.searchSongs(query);
        return ResponseEntity.ok(results);
    }
}
