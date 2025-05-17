import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';

// Defining the Song model
export interface Song {
    imageUrl: any;
    id: number;
    title: string;
    artist: string;
    album: string;
    genre: string;
    language: string;
    duration: string;
    url: string;
    uploadedBy: string;
    createdAt: string;
    updatedAt: string;
}

@Injectable({
    providedIn: 'root'
})
export class SongService {
    private baseUrl = environment.songsApi;  // <-- use songsApi from environment

    constructor(private http: HttpClient) { }

    // Method to fetch all songs from the backend
    getAllSongs(): Observable<Song[]> {
        return this.http.get<Song[]>(this.baseUrl);
    }

    // Method to get a song by id
    getSongById(songId: number): Observable<Song> {
        return this.http.get<Song>(`${this.baseUrl}/${songId}`);
    }

    // Updated method to upload a song file
    uploadSong(file: File): Observable<Song> {
        const formData = new FormData();
        formData.append('file', file);

        return this.http.post<Song>(`${this.baseUrl}/upload/auto`, formData);
    }
}