package com.harmonique.songservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Represents a song entity in the Harmonique application.
 * This class maps to the 'songs' table in the database and includes
 * fields for metadata, file information, and auditing timestamps.
 */
@Entity
@Table(name = "songs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class) // Enables automatic population of createdAt and updatedAt
public class Song {

    /**
     * Primary key for the song entity.
     * Auto-incremented by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Optional URL for the song's image or album cover.
     */
    @Column(name = "image_url")
    private String imageUrl;

    /**
     * Title of the song.
     * Cannot be null.
     */
    @Column(nullable = false)
    private String title;

    /**
     * Artist or singer name.
     * Cannot be null.
     */
    @Column(nullable = false)
    private String artist;

    /**
     * Album name the song belongs to.
     * Optional field.
     */
    private String album;

    /**
     * Genre of the song (e.g., Rock, Pop, Jazz).
     * Optional field.
     */
    private String genre;

    /**
     * Language of the song (e.g., English, Hindi).
     * Optional field.
     */
    private String language;

    /**
     * Duration of the song in "MM:SS" format.
     * Limited to 8 characters to ensure proper format.
     */
    @Column(length = 8)
    private String duration;

    /**
     * Local file path on the server where the MP3 file is stored.
     * Used internally for downloads.
     */
    private String filePath;

    /**
     * URL to access the song file (local path or public cloud link).
     */
    private String url;

    /**
     * Description or notes about the song.
     * Optional field.
     */
    private String description;

    /**
     * Username or identifier of the user who uploaded the song.
     * Cannot be null.
     */
    @Column(name = "uploaded_by", nullable = false)
    private String uploadedBy;

    /**
     * Timestamp when the song was created/uploaded.
     * Automatically managed by JPA auditing.
     * Not updatable.
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the song was last updated.
     * Automatically managed by JPA auditing.
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}