package com.harmonique.songservice.payload;

import lombok.*;

/**
 * DTO used to receive song creation or update requests from the client.
 * This class is used in POST/PUT endpoints when a new song is added or edited.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongRequest {

    private String title;         // Required: Song title
    private String artist;        // Required: Artist name
    private String album;         // Optional: Album name
    private String genre;         // Optional: Genre
    private String language;      // Optional: Language
    private String duration;      // Optional: Duration in "mm:ss"
    private String filePath;      // Internal server file path (optional)
    private String url;           // Public or internal song file URL
    private String imageUrl;      // Cover image URL (optional)
    private String description;   // Optional song description
    private String uploadedBy;    // Required: Username of the uploader
}