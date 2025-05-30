package com.harmonique.songservice.payload;

import lombok.*;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) used to send song data back to the client.
 * This class shapes the response object for GET operations (list, view, etc.).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongResponse {

    private Long id;                    // Unique identifier for the song
    private String title;              // Song title
    private String artist;             // Artist name
    private String album;              // Album name (optional)
    private String genre;              // Genre of the song (optional)
    private String language;           // Language (optional)
    private String duration;           // Duration in format "mm:ss"
    private String url;                // URL/path to the audio file (cloud or local)
    private String uploadedBy;         // Username who uploaded the song
    private String imageUrl;           // Cover image URL
    private LocalDateTime createdAt;   // Timestamp of creation (auto-managed)
    private LocalDateTime updatedAt;   // Timestamp of last update (auto-managed)
}