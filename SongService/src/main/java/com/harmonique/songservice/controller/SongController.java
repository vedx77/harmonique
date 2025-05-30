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
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing songs in the Harmonique music library.
 * Supports CRUD operations, file uploads with metadata (manual or automatic), download, and search functionality.
 */
@RestController
@RequestMapping("/api/songs")
@Tag(name = "Songs", description = "APIs for managing songs (upload, download, search)")
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

    /**
     * Creates a new song with manual metadata (without file upload).
     */
    @PostMapping
    @Operation(summary = "Create a new song manually (without uploading file)")
    public ResponseEntity<SongResponse> createSong(@Valid @RequestBody SongRequest songRequest) {
        logger.info("Creating new song with title: {}", songRequest.getTitle());
        SongResponse songResponse = songService.createSong(songRequest);
        logger.debug("Song created successfully: {}", songResponse);
        return new ResponseEntity<>(songResponse, HttpStatus.CREATED);
    }

    /**
     * Updates metadata of an existing song by ID.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update song metadata by ID")
    public ResponseEntity<SongResponse> updateSong(
            @PathVariable Long id,
            @Valid @RequestBody SongRequest request) {
        logger.info("Updating song with ID: {}", id);
        SongResponse response = songService.updateSong(request, id);
        logger.debug("Song updated successfully: {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a song by its ID.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete song by ID")
    public ResponseEntity<ApiResponse> deleteSong(@PathVariable Long id) {
        logger.warn("Deleting song with ID: {}", id);
        songService.deleteSong(id);
        ApiResponse response = new ApiResponse("Song deleted successfully", true, HttpStatus.OK);
        return ResponseEntity.ok(response);
    }

    /**
     * Fetches details of a song by its ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get song details by ID")
    public ResponseEntity<SongResponse> getSongById(@PathVariable Long id) {
        logger.info("Fetching song with ID: {}", id);
        SongResponse songResponse = songService.getSongById(id);
        logger.debug("Fetched song: {}", songResponse);
        return ResponseEntity.ok(songResponse);
    }

    /**
     * Returns a list of all uploaded songs.
     */
    @GetMapping
    @Operation(summary = "Get list of all uploaded songs")
    public ResponseEntity<List<SongResponse>> getAllSongs() {
        logger.info("Fetching all uploaded songs");
        List<SongResponse> songs = songService.getAllSongs();
        logger.debug("Total songs fetched: {}", songs.size());
        return ResponseEntity.ok(songs);
    }

    // ------------------------------------------
    // Upload APIs
    // ------------------------------------------

    /**
     * Uploads one or more song files and auto-extracts metadata from each file.
     */
    @PostMapping(value = "/upload/auto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload song file(s) and auto-extract metadata")
    public ResponseEntity<List<SongResponse>> uploadWithAutoMetadata(
            @RequestPart("files") MultipartFile[] files) {

        String uploadedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Uploading {} file(s) with auto metadata by user: {}", files.length, uploadedBy);

        List<SongResponse> responses = Arrays.stream(files)
                .map(file -> {
                    logger.debug("Processing file: {}", file.getOriginalFilename());
                    return songService.uploadWithAutoMetadata(file, uploadedBy);
                })
                .collect(Collectors.toList());

        logger.debug("Auto metadata upload completed for all files.");
        return new ResponseEntity<>(responses, HttpStatus.CREATED);
    }

    /**
     * Uploads a song file with metadata manually provided by the user.
     */
    @PostMapping(value = "/upload/manual", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload song file with manual metadata input")
    public ResponseEntity<SongResponse> uploadWithManualMetadata(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("artist") String artist,
            @RequestParam("album") String album,
            @RequestParam("genre") String genre,
            @RequestParam("language") String language,
            @RequestParam("description") String description,
            @RequestHeader("Authorization") String bearerToken) {

        String token = bearerToken.replace("Bearer ", "");
        String uploadedBy = jwtTokenHelper.getUsernameFromToken(token);
        logger.info("Uploading file with manual metadata by user: {}", uploadedBy);
        logger.debug("File: {}, Title: {}, Artist: {}", file.getOriginalFilename(), title, artist);

        SongResponse songResponse = songService.uploadWithManualMetadata(
                file, title, artist, album, genre, language, description, uploadedBy
        );
        logger.debug("Manual upload completed for song: {}", songResponse.getTitle());

        return new ResponseEntity<>(songResponse, HttpStatus.CREATED);
    }

    // ------------------------------------------
    // Download API
    // ------------------------------------------

    /**
     * Downloads the actual MP3 file of a song by its ID.
     */
    @GetMapping("/download/{id}")
    @Operation(summary = "Download song file by ID")
    public ResponseEntity<byte[]> downloadSongFile(@PathVariable Long id) {
        logger.info("Downloading song file with ID: {}", id);

        SongResponse songResponse = songService.getSongById(id); // Metadata
        byte[] fileData = songService.downloadSongFile(id);      // Actual bytes

        // Sanitize title to avoid illegal characters in filenames
        String safeTitle = songResponse.getTitle().replaceAll("[^a-zA-Z0-9\\-_\\. ]", "_");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", safeTitle + ".mp3");

        logger.debug("Download ready for song: {}", safeTitle);
        return ResponseEntity.ok()
                .headers(headers)
                .body(fileData);
    }

    // ------------------------------------------
    // Search API
    // ------------------------------------------

    /**
     * Searches for songs by matching title, artist, or album fields with the given query string.
     */
    @GetMapping("/search")
    @Operation(summary = "Search songs by title, artist, or album")
    public ResponseEntity<List<SongResponse>> searchSongs(@RequestParam("query") String query) {
        logger.info("Searching songs with query: '{}'", query);
        List<SongResponse> searchResults = songService.searchSongs(query);
        logger.debug("Search returned {} results", searchResults.size());
        return ResponseEntity.ok(searchResults);
    }
}