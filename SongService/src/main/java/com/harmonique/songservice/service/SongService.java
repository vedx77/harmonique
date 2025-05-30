package com.harmonique.songservice.service;

import com.harmonique.songservice.payload.SongRequest;
import com.harmonique.songservice.payload.SongResponse;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * Interface that defines the contract for song-related business logic.
 * This includes CRUD operations, file uploads, metadata handling, and search.
 */
public interface SongService {

    // ------------------ CORE CRUD OPERATIONS ------------------

    /**
     * Create a new song entry using data provided in the SongRequest.
     *
     * @param songRequest the DTO containing song details
     * @return the saved SongResponse object
     */
    SongResponse createSong(SongRequest songRequest);

    /**
     * Update an existing song by its ID with new data.
     *
     * @param songRequest the DTO with updated song data
     * @param songId the ID of the song to update
     * @return the updated SongResponse
     */
    SongResponse updateSong(SongRequest songRequest, Long songId);

    /**
     * Delete a song by its unique ID.
     *
     * @param songId the ID of the song to delete
     */
    void deleteSong(Long songId);

    /**
     * Get details of a song by its ID.
     *
     * @param songId the ID of the song
     * @return the corresponding SongResponse
     */
    SongResponse getSongById(Long songId);

    /**
     * Get a list of all available songs.
     *
     * @return list of SongResponse objects
     */
    List<SongResponse> getAllSongs();


    // ------------------ FILE UPLOAD OPERATIONS ------------------

    /**
     * Upload a song file along with manually provided metadata like title, artist, genre, etc.
     *
     * @param file the audio file
     * @param title the title of the song
     * @param artist the artist name
     * @param album the album name
     * @param genre the genre
     * @param language the language
     * @param description optional description
     * @param uploadedBy username or ID of the uploader
     * @return the saved SongResponse
     */
    SongResponse uploadWithManualMetadata(
        MultipartFile file, 
        String title, 
        String artist, 
        String album, 
        String genre, 
        String language, 
        String description, 
        String uploadedBy
    );

    /**
     * Upload a song file and extract metadata automatically using a tag reader (like JAudioTagger).
     *
     * @param file the audio file
     * @param uploadedBy uploader's ID or username
     * @return the saved SongResponse with extracted metadata
     */
    SongResponse uploadWithAutoMetadata(MultipartFile file, String uploadedBy);

    
    // ------------------ DOWNLOAD & SEARCH ------------------

    /**
     * Download the binary content (byte[]) of a song file by its ID.
     *
     * @param songId the ID of the song to download
     * @return byte array of the song file
     */
    byte[] downloadSongFile(Long songId);

    /**
     * Search for songs by a keyword that could match title, artist, album, or genre.
     *
     * @param keyword the search string
     * @return list of matching SongResponse objects
     */
    List<SongResponse> searchSongs(String keyword);
}