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

@Entity
@Table(name = "liked_songs", uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "songId"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikedSong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long songId;

    private LocalDateTime likedAt;
    
    @PrePersist
    public void prePersist() {
        likedAt = LocalDateTime.now();
    }
}