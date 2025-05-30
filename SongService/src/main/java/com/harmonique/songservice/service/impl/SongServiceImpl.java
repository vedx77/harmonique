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

    /**
     * Creates a new song entry in the database.
     *
     * @param songRequest the request payload containing song metadata
     * @return the saved song response
     */
    @Override
    public SongResponse createSong(SongRequest songRequest) {
        String uploadedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        Song song = mapToEntity(songRequest);
        song.setUploadedBy(uploadedBy);
        Song savedSong = songRepository.save(song);
        log.info("✅ Song created | ID: {}, Title: '{}', UploadedBy: {}", savedSong.getId(), savedSong.getTitle(), uploadedBy);
        return mapToResponse(savedSong);
    }

    /**
     * Updates an existing song.
     *
     * @param songRequest the new metadata
     * @param songId      the ID of the song to update
     * @return the updated song response
     */
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

    /**
     * Deletes a song by its ID.
     *
     * @param songId the ID of the song to delete
     */
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

    /**
     * Retrieves a song by its ID.
     *
     * @param songId the ID of the song
     * @return the corresponding song response
     */
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

    /**
     * Retrieves all songs from the database.
     *
     * @return a list of all song responses
     */
    @Override
    public List<SongResponse> getAllSongs() {
        List<Song> songs = songRepository.findAll();
        log.info("📚 All songs retrieved | Count: {}", songs.size());
        return songs.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    /**
     * Uploads a song file with manually provided metadata.
     *
     * @param file        the song file
     * @param title       the song title
     * @param artist      the song artist
     * @param album       the album name
     * @param genre       the genre
     * @param language    the language
     * @param description the description
     * @param uploadedBy  the uploader's username
     * @return the saved song response
     */
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
            request.setUploadedBy(uploadedBy);

            log.info("📤 Uploading song with manual metadata | Title: '{}', Artist: '{}'", title, artist);
            return createSong(request);

        } catch (IOException e) {
            log.error("❌ Failed to store file | Error: {}", e.getMessage(), e);
            throw new RuntimeException("Could not store file: " + e.getMessage());
        }
    }

    /**
     * Uploads a song file and extracts metadata automatically from it.
     *
     * @param file       the song file
     * @param uploadedBy the uploader's username
     * @return the saved song response
     */
    @Override
    public SongResponse uploadWithAutoMetadata(MultipartFile file, String uploadedBy) {
        try {
            Path filePath = saveFile(file);
            String fileUrl = accessUrl + filePath.getFileName();

            SongRequest request = extractMetadataFromFile(filePath.toFile(), fileUrl);
            request.setUploadedBy(uploadedBy);
            request.setFilePath(filePath.toString());

            log.info("📥 Uploading song with auto-extracted metadata | File: '{}'", file.getOriginalFilename());
            return createSong(request);

        } catch (IOException e) {
            log.error("❌ Failed to upload and extract metadata | Error: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload and extract metadata: " + e.getMessage());
        }
    }

    /**
     * Downloads the song file for the given song ID.
     *
     * @param songId the ID of the song
     * @return a byte array of the file content
     */
    @Override
    public byte[] downloadSongFile(Long songId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with ID: " + songId));

        String filePathStr = song.getFilePath();
        if (filePathStr == null || filePathStr.isBlank()) {
            throw new RuntimeException("No file path found for song ID: " + songId);
        }

        Path filePath = Paths.get(filePathStr);
        try {
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            log.error("❌ Failed to read file | Path: {}, Error: {}", filePath, e.getMessage(), e);
            throw new RuntimeException("Failed to download file: " + e.getMessage());
        }
    }

    /**
     * Searches for songs matching the provided keyword in title, artist, or album.
     *
     * @param keyword the search keyword
     * @return a list of matching songs
     */
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

    /**
     * Saves the uploaded file to the local file system.
     *
     * @param file the file to save
     * @return the path of the saved file
     * @throws IOException if file saving fails
     */
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

    /**
     * Extracts the duration of a song in seconds.
     *
     * @param file the audio file
     * @return duration as a string (e.g., "320 sec")
     */
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

    /**
     * Extracts metadata from the audio file using JAudioTagger.
     *
     * @param songFile the audio file
     * @param url      the file's URL
     * @return a populated SongRequest with extracted metadata
     */
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

                var artwork = tag.getFirstArtwork();
                if (artwork != null) {
                    byte[] imageData = artwork.getBinaryData();
                    String imageFileName = System.currentTimeMillis() + "_cover.jpg";
                    Path imagePath = Paths.get(uploadDir).resolve(imageFileName);
                    Files.write(imagePath, imageData);
                    String imageUrl = accessUrl + imageFileName;
                    request.setImageUrl(imageUrl);
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

    /**
     * Safely extracts a metadata tag by its key.
     *
     * @param tag the audio tag
     * @param key the tag key
     * @return the tag value, or an empty string if not found
     */
    private String getSafeTag(Tag tag, String key) {
        try {
            String value = tag.getFirst(key);
            return value != null ? value : "";
        } catch (Exception e) {
            log.debug("🔍 Failed to extract tag '{}' | {}", key, e.getMessage());
            return "";
        }
    }

    /**
     * Maps a Song entity to a SongResponse DTO.
     *
     * @param song the Song entity
     * @return the SongResponse DTO
     */
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

    /**
     * Maps a SongRequest DTO to a Song entity.
     *
     * @param request the SongRequest DTO
     * @return the Song entity
     */
    private Song mapToEntity(SongRequest request) {
        return Song.builder()
                .title(request.getTitle())
                .artist(request.getArtist())
                .album(request.getAlbum())
                .genre(request.getGenre())
                .language(request.getLanguage())
                .duration(request.getDuration())
                .filePath(request.getFilePath())
                .url(request.getUrl())
                .imageUrl(request.getImageUrl())
                .uploadedBy(request.getUploadedBy())
                .build();
    }
}