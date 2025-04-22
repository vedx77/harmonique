package com.harmonique.songservice.payload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongRequest {

    private String title; // Now optional (manual OR metadata)

    private String artist;

    private String album;

    private String genre;
    
    private String language;

    private String duration;

    private String url;

    private String description;
    
    private String uploadedBy;
}
