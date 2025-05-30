package com.harmonique.likeservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class representing a liked song entry by a user.
 * Each combination of userId and songId is unique, enforced by a database constraint.
 */
@Entity
@Table(name = "liked_songs", uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "songId"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikedSong {

    /** Auto-generated primary key for the liked song record */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The ID of the user who liked the song */
    private Long userId;

    /** The ID of the liked song */
    private Long songId;

    /** Timestamp of when the song was liked */
    private LocalDateTime likedAt;

    /**
     * Automatically sets the `likedAt` field to the current time
     * when the entity is persisted for the first time.
     */
    @PrePersist
    public void prePersist() {
        likedAt = LocalDateTime.now();
    }
}