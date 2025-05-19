package com.harmonique.songservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "songs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String artist;

    private String album;

    private String genre;

    private String language;

    @Column(length = 8)
    private String duration;  // Format like "03:30", stored as string
    
    private String filePath;

    private String url;  // Song file location (local or cloud)

    private String description;

    @Column(name = "uploaded_by", nullable = false)
    private String uploadedBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}