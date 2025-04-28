package com.harmonique.songservice.service.impl;

import com.harmonique.songservice.entity.Song;
import com.harmonique.songservice.exception.ResourceNotFoundException;
import com.harmonique.songservice.payload.SongRequest;
import com.harmonique.songservice.payload.SongResponse;
import com.harmonique.songservice.repository.SongRepository;
import com.harmonique.songservice.service.SongService;

import lombok.extern.slf4j.Slf4j;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SongServiceImpl implements SongService {

    @Autowired
    private SongRepository songRepository;

    @Value("${song.upload.dir}")
    private String uploadDir;

    @Value("${song.access.url}")
    private String accessUrl;

    // ===========================
    // CRUD OPERATIONS
    // ===========================

    @Override
    public SongResponse createSong(SongRequest songRequest) {
        String uploadedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        Song song = mapToEntity(songRequest);
        song.setUploadedBy(uploadedBy);
        Song savedSong = songRepository.save(song);
        log.info("✅ Song created | ID: {}, Title: '{}', UploadedBy: {}", savedSong.getId(), savedSong.getTitle(), uploadedBy);
        return mapToResponse(savedSong);
    }

    @Override
    public SongResponse updateSong(SongRequest songRequest, Long songId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> {
                    log.warn("⚠️ Song not found for update | ID: {}", songId);
                    return new ResourceNotFoundException("Song not found with ID: " + songId);
                });

        song.setTitle(songRequest.getTitle());
        song.setArtist(songRequest.getArtist());
        song.setAlbum(songRequest.getAlbum());
        song.setGenre(songRequest.getGenre());
        song.setUrl(songRequest.getUrl());
        song.setDuration(songRequest.getDuration());

        Song updatedSong = songRepository.save(song);
        log.info("✅ Song updated | ID: {}, Title: '{}'", updatedSong.getId(), updatedSong.getTitle());
        return mapToResponse(updatedSong);
    }

    @Override
    public void deleteSong(Long songId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> {
                    log.warn("⚠️ Song not found for deletion | ID: {}", songId);
                    return new ResourceNotFoundException("Song not found with ID: " + songId);
                });
        songRepository.delete(song);
        log.info("🗑️ Song deleted | ID: {}, Title: '{}'", song.getId(), song.getTitle());
    }

    @Override
    public SongResponse getSongById(Long songId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> {
                    log.warn("⚠️ Song not found for retrieval | ID: {}", songId);
                    return new ResourceNotFoundException("Song not found with ID: " + songId);
                });
        log.info("📀 Song retrieved | ID: {}, Title: '{}'", song.getId(), song.getTitle());
        return mapToResponse(song);
    }

    @Override
    public List<SongResponse> getAllSongs() {
        List<Song> songs = songRepository.findAll();
        log.info("📚 All songs retrieved | Count: {}", songs.size());
        return songs.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // ===========================
    // FILE UPLOADS
    // ===========================

    @Override
    public SongResponse uploadWithManualMetadata(MultipartFile file, String title, String artist,
                                                 String album, String genre, String language, 
                                                 String description, String uploadedBy) {
        try {
            Path filePath = saveFile(file);
            String fileUrl = accessUrl + filePath.getFileName();

            SongRequest request = new SongRequest();
            request.setTitle(title);
            request.setArtist(artist);
            request.setAlbum(album);
            request.setGenre(genre);
            request.setLanguage(language);
            request.setDescription(description);
            request.setUrl(fileUrl);
            request.setDuration(extractDuration(filePath.toFile()));
            request.setUploadedBy(uploadedBy);  // Ensure the uploader is set here

            log.info("📤 Uploading song with manual metadata | Title: '{}', Artist: '{}'", title, artist);
            return createSong(request);

        } catch (IOException e) {
            log.error("❌ Failed to store file | Error: {}", e.getMessage(), e);
            throw new RuntimeException("Could not store file: " + e.getMessage());
        }
    }


    @Override
    public SongResponse uploadWithAutoMetadata(MultipartFile file, String uploadedBy) {
        try {
            Path filePath = saveFile(file);
            String fileUrl = accessUrl + filePath.getFileName();

            SongRequest request = extractMetadataFromFile(filePath.toFile(), fileUrl);
            request.setUploadedBy(uploadedBy);
            
            log.info("📥 Uploading song with auto-extracted metadata | File: '{}'", file.getOriginalFilename());
            return createSong(request);

        } catch (IOException e) {
            log.error("❌ Failed to upload and extract metadata | Error: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload and extract metadata: " + e.getMessage());
        }
    }

    // ===========================
    // FILE DOWNLOADS
    // ===========================    
    
    @Override
    public byte[] downloadSongFile(Long songId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with ID: " + songId));
        Path filePath = Paths.get(song.getUrl());  // Assuming `song.getUrl()` holds the file path
        try {
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            log.error("❌ Failed to read file | Error: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to download file: " + e.getMessage());
        }
    }

    // ===========================
    // SONG SEARCH
    // ===========================
    @Override
    public List<SongResponse> searchSongs(String keyword) {
        List<Song> songs = songRepository
            .findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCaseOrAlbumContainingIgnoreCase(
                keyword, keyword, keyword
            );

        return songs.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    // ===========================
    // PRIVATE HELPERS
    // ===========================

    private Path saveFile(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            log.info("📁 Upload directory created: {}", uploadPath.toAbsolutePath());
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = System.currentTimeMillis() + "_" + originalFilename;
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        log.info("✅ File saved successfully | Path: {}", filePath);
        return filePath;
    }

    private String extractDuration(File file) {
        try {
            AudioFile audioFile = AudioFileIO.read(file);
            int seconds = audioFile.getAudioHeader().getTrackLength();
            String duration = seconds + " sec";
            log.info("⏱️ Duration extracted: {}", duration);
            return duration;
        } catch (Exception e) {
            log.warn("⚠️ Duration extraction failed | Reason: {}", e.getMessage());
            return null;
        }
    }

    private SongRequest extractMetadataFromFile(File songFile, String url) {
        SongRequest request = new SongRequest();

        try {
            AudioFile audioFile = AudioFileIO.read(songFile);
            Tag tag = audioFile.getTag();

            if (tag != null) {
                request.setTitle(getSafeTag(tag, "TIT2"));
                request.setArtist(getSafeTag(tag, "TPE1"));
                request.setAlbum(getSafeTag(tag, "TALB"));
                request.setGenre(getSafeTag(tag, "TCON"));

                // 🖼️ Extract embedded image (album art)
                var artwork = tag.getFirstArtwork();
                if (artwork != null) {
                    byte[] imageData = artwork.getBinaryData();
                    String imageFileName = System.currentTimeMillis() + "_cover.jpg";
                    Path imagePath = Paths.get(uploadDir).resolve(imageFileName);
                    Files.write(imagePath, imageData);  // Save image file
                    String imageUrl = accessUrl + imageFileName;
                    request.setImageUrl(imageUrl);  // ✅ Correctly setting image URL
                }

                log.info("📝 Metadata extracted | Title: '{}', Artist: '{}', Album: '{}', Genre: '{}'",
                        request.getTitle(), request.getArtist(), request.getAlbum(), request.getGenre());
            }

            request.setDuration(audioFile.getAudioHeader().getTrackLength() + " sec");

        } catch (Exception e) {
            log.warn("⚠️ Metadata extraction failed | Reason: {}", e.getMessage());
        }

        request.setUrl(url);
        return request;
    }

    private String getSafeTag(Tag tag, String key) {
        try {
            String value = tag.getFirst(key);
            return value != null ? value : "";
        } catch (Exception e) {
            log.debug("🔍 Failed to extract tag '{}' | {}", key, e.getMessage());
            return "";
        }
    }

    private SongResponse mapToResponse(Song song) {
        return SongResponse.builder()
                .id(song.getId())
                .title(song.getTitle())
                .artist(song.getArtist())
                .album(song.getAlbum())
                .genre(song.getGenre())
                .language(song.getLanguage())
                .duration(song.getDuration())
                .url(song.getUrl())
                .uploadedBy(song.getUploadedBy())
                .imageUrl(song.getImageUrl())
                .createdAt(song.getCreatedAt())
                .updatedAt(song.getUpdatedAt())
                .build();
    }


    private Song mapToEntity(SongRequest request) {
        return Song.builder()
                .title(request.getTitle())
                .artist(request.getArtist())
                .album(request.getAlbum())
                .genre(request.getGenre())
                .language(request.getLanguage())
                .duration(request.getDuration())
                .url(request.getUrl())
                .imageUrl(request.getImageUrl())
                .uploadedBy(request.getUploadedBy())
                .build();
    }
}