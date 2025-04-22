package com.harmonique.songservice.payload;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongResponse {

    private Long id;

    private String title;

    private String artist;

    private String album;
    
    private String genre;
    
    private String language;

    private String duration;

    private String url;
    
    private String uploadedBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
