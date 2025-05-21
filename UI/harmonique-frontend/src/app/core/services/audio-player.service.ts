// audio-player.service.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class AudioPlayerService {
  private _songs = new BehaviorSubject<any[]>([]);
  private _currentSongIndex = new BehaviorSubject<number>(-1);
  private _isPlaying = new BehaviorSubject<boolean>(false);
  currentSongChanged = new BehaviorSubject<any | null>(null);


  // Public observables
  songs$ = this._songs.asObservable();
  currentSongIndex$ = this._currentSongIndex.asObservable();
  isPlaying$ = this._isPlaying.asObservable();

  // Public getters
  get songs(): any[] {
    return this._songs.getValue();
  }

  get currentSongIndex(): number {
    return this._currentSongIndex.getValue();
  }

  get isPlaying(): boolean {
    return this._isPlaying.getValue();
  }

  // Get current song
  get currentSong(): any {
    const index = this.currentSongIndex;
    if (index >= 0 && index < this.songs.length) {
      return this.songs[index];
    }
    return null;
  }

  // audio-player.service.ts
  setSongs(songs: any[]): void {
    this._songs.next(songs);
    this._currentSongIndex.next(0); // Start from the first song
  }

  setCurrentSongIndex(index: number): void {
    this._currentSongIndex.next(index);
    if (index >= 0 && index < this.songs.length) {
      const song = this.songs[index];
      this._isPlaying.next(!song.audio.paused);
    }
  }

  playSong(song: any): void {
    // Pause all current songs
    this.songs.forEach(s => s.audio?.pause());

    // Check if the song already exists in the playlist
    let index = this.songs.findIndex(s => s.src === song.url);

    // If not found, create a new song object and add it
    if (index === -1) {
      const songObj = {
        name: song.title,
        src: song.url,
        audio: new Audio(song.url),
        isPlaying: false,
        currentTime: 0,
        duration: 0,
        artist: song.artist,
        image: song.image || ''
      };

      songObj.audio.addEventListener('loadedmetadata', () => {
        songObj.duration = songObj.audio.duration;
      });

      songObj.audio.addEventListener('timeupdate', () => {
        songObj.currentTime = songObj.audio.currentTime;
      });

      const updatedSongs = [...this.songs, songObj];
      this.setSongs(updatedSongs);
      index = updatedSongs.length - 1;
    }

    this.changeSong(index); // central play method
  }

  togglePlay(index: number | null = null): void {
    let currentIndex = this.currentSongIndex;

    if (index !== null) {
      currentIndex = index;
      this.setCurrentSongIndex(currentIndex);
    }

    if (currentIndex === -1 && this.songs.length > 0) {
      currentIndex = 0;
      this.setCurrentSongIndex(currentIndex);
    }

    if (currentIndex === -1 || !this.songs[currentIndex]) return;

    const currentSong = this.songs[currentIndex];

    if (currentSong.audio.paused) {
      // Pause all other songs
      this.songs.forEach((song, i) => {
        if (i !== currentIndex && song.audio) {
          song.audio.pause();
        }
      });
      currentSong.audio.play();
      this._isPlaying.next(true);
    } else {
      currentSong.audio.pause();
      this._isPlaying.next(false);
    }
  }

  next(): void {
    if (this.songs.length === 0) return;
    const newIndex = (this.currentSongIndex + 1) % this.songs.length;
    this.changeSong(newIndex);
  }

  previous(): void {
    if (this.songs.length === 0) return;
    const newIndex = (this.currentSongIndex - 1 + this.songs.length) % this.songs.length;
    this.changeSong(newIndex);
  }

  private changeSong(newIndex: number): void {
    // Pause current song if playing
    if (this.currentSongIndex >= 0 && this.currentSongIndex < this.songs.length) {
      this.songs[this.currentSongIndex].audio.pause();
    }

    // Play new song
    this.setCurrentSongIndex(newIndex);
    this.songs[newIndex].audio.currentTime = 0;
    this.songs[newIndex].audio.play();
    this._isPlaying.next(true);

    // Emit current song
    this.currentSongChanged.next(this.songs[newIndex]);
  }

  seekTo(time: number): void {
    if (this.currentSongIndex >= 0 && this.currentSongIndex < this.songs.length) {
      this.songs[this.currentSongIndex].audio.currentTime = time;
    }
  }

  formatTime(sec: number): string {
    const minutes = Math.floor(sec / 60);
    const seconds = Math.floor(sec % 60).toString().padStart(2, '0');
    return `${minutes}:${seconds}`;
  }

  // Implementing previously unimplemented methods
  getCurrentTimeFormatted(): string {
    const song = this.currentSong;
    if (song && song.currentTime !== undefined) {
      return this.formatTime(song.currentTime);
    }
    return '0:00';
  }

  getDurationFormatted(): string {
    const song = this.currentSong;
    if (song && song.duration !== undefined) {
      return this.formatTime(song.duration);
    }
    return '0:00';
  }

  // Helper method to get current song progress as percentage
  getCurrentProgress(): number {
    const song = this.currentSong;
    if (song && song.duration > 0) {
      return (song.currentTime / song.duration) * 100;
    }
    return 0;
  }
}