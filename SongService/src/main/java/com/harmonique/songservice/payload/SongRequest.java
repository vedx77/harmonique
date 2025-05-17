package com.harmonique.songservice.payload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongRequest {

    private String title;
    private String artist;
    private String album;
    private String genre;
    private String language;
    private String duration;
    private String filePath;
    private String url;
    private String imageUrl;
    private String description;
    private String uploadedBy;
}