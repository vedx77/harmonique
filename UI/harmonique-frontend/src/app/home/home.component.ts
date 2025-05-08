import { Component, ElementRef, ViewChild, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AudioPlayerService } from '../home/services/audio-player.service'; // Adjust the path as needed
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment.development';
import { AuthService } from './services/auth.service';
import { MatCardModule } from '@angular/material/card';
import { SidebarService } from '../home/services/sidebar.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-home',
  standalone: true,
  templateUrl: './home.component.html',
  imports: [RouterModule, CommonModule, MatCardModule],
  styleUrls: ['./home.component.scss'],
})

export class HomeComponent {
  form: FormGroup;
  currentStep: number = 1;
  totalSteps: number = 3;

  @ViewChild('sidebar') sidebar!: ElementRef;

  isSidebarVisible: boolean = false;
  crossfadeValue: number = 5;
  progressValue: number = 5;

  importedSongs: any[] = [];
  backendSongs: any[] = []; // ✅ Added this to store fetched songs from backend
  currentSongIndex: number = -1;

  availableImages = [
    'assets/51c547366f2853da1052e531f4bfe4d5.jpg',
    'assets/770a9cca1e543e6edeae6747db9522d2.jpg',
    'assets/956b070b9df64cdd16d966caa1e016bf.jpg',
    'assets/aa53683a96a23571867f1eafa0d845a1.jpg',
    'assets/Listening To Music GIF - Head Phones Music Recording Studio - Discover & Share GIFs.gif',
    'assets/Mask Group.png'
  ];

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private changeDetectorRef: ChangeDetectorRef,
    public audioService: AudioPlayerService,
    private http: HttpClient, // ✅ Added HttpClient
    private authService: AuthService,
    private sidebarService: SidebarService
  ) {
    this.form = this.fb.group({
      step1: ['', Validators.required],
      step2: ['', Validators.required],
      step3: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    // Subscribe to the songs observable to keep the local importedSongs array in sync
    this.audioService.songs$.subscribe(songs => {
      this.importedSongs = songs;
      this.changeDetectorRef.detectChanges();
    });

    // Subscribe to current song index updates
    this.audioService.currentSongIndex$.subscribe(index => {
      this.currentSongIndex = index;
      this.changeDetectorRef.detectChanges();
    });

    // Fetch songs from backend
    this.fetchSongsFromBackend();

    this.sidebarSubscription = this.sidebarService.expanded$.subscribe((expanded) => {
      this.isSidebarExpanded = expanded;
      this.changeDetectorRef.detectChanges();
    });
  }

  uploadSongs(event: any): void {
    const files: FileList = event.target.files;
    if (!files || files.length === 0) return;

    const formData = new FormData();
    for (let i = 0; i < files.length; i++) {
      formData.append('files', files[i]);
    }

    const token = this.authService.getToken(); // <-- Use the stored token

    this.http.post(`${environment.songsApi}/upload/auto`, formData, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).subscribe({
      next: (response: any) => {
        console.log('Upload successful:', response);
        this.fetchSongsFromBackend(); // Refresh the list after upload
      },
      error: (err) => {
        console.error('Upload failed:', err);
      }
    });
  }  

  fetchSongsFromBackend(): void {
    this.http.get<any[]>(environment.songsApi)
      .subscribe(
        (data) => {
          this.backendSongs = data.map(song => ({
            ...song,
            image: song.imageUrl  // ✅ Assign the backend image URL to the image field
          }));
        },
        (error) => {
          console.error('Error fetching songs:', error);
        }
      );
  }

  getRandomImage(): string {
    const index = Math.floor(Math.random() * this.availableImages.length);
    return this.availableImages[index];
  }

  loadFiles(event: any): void {
    const files: FileList = event.target.files;

    // Get current songs from service
    const newSongs = [...this.audioService.songs];

    for (let i = 0; i < files.length; i++) {
      const file = files[i];

      // Check if the file is an audio file
      if (!file.type.startsWith('audio/')) {
        console.warn(`File ${file.name} is not an audio file. Skipping.`);
        continue;
      }

      const url = URL.createObjectURL(file);
      const audio = new Audio(url);

      // Check if the song is already in the list by its src
      if (newSongs.some(song => song.src === url)) {
        console.warn(`Song ${file.name} already imported. Skipping.`);
        continue; // Skip if the song is already in the list
      }

      const songObj = {
        name: file.name.replace(/\.[^/.]+$/, ""), // Remove file extension
        src: url,
        audio: audio,
        isPlaying: false,
        currentTime: 0,
        duration: 0,
        artist: 'Unknown Artist',
        image: this.getRandomImage()
      };

      audio.addEventListener('loadedmetadata', () => {
        songObj.duration = audio.duration;
        this.changeDetectorRef.detectChanges();
      });

      audio.addEventListener('timeupdate', () => {
        songObj.currentTime = audio.currentTime;
        this.changeDetectorRef.detectChanges();
      });

      // Add error handler for audio loading failures
      audio.addEventListener('error', () => {
        console.error(`Error loading audio file: ${file.name}`);
        // Find and remove this song if it was already added
        const index = newSongs.indexOf(songObj);
        if (index !== -1) {
          newSongs.splice(index, 1);
          this.audioService.setSongs(newSongs);
        }
      });

      // Add the new song to the list
      newSongs.push(songObj);
    }

    // Update service with all songs (existing + new)
    this.audioService.setSongs(newSongs);

    // Reset file input to allow selecting the same file again if needed
    event.target.value = '';
  }

  togglePlay(index: number): void {
    // Use the audio service to control playback
    this.audioService.togglePlay(index);
  }

  get currentTime(): string {
    if (this.currentSongIndex >= 0 && this.currentSongIndex < this.importedSongs.length) {
      const currentSong = this.importedSongs[this.currentSongIndex];
      return this.formatTime(currentSong.currentTime);
    }
    return '0:00';
  }

  get duration(): string {
    if (this.currentSongIndex >= 0 && this.currentSongIndex < this.importedSongs.length) {
      const currentSong = this.importedSongs[this.currentSongIndex];
      return this.formatTime(currentSong.duration);
    }
    return '0:00';
  }

  private formatTime(sec: number): string {
    const minutes = Math.floor(sec / 60);
    const seconds = Math.floor(sec % 60).toString().padStart(2, '0');
    return `${minutes}:${seconds}`;
  }

  onCrossfadeChange(event: any): void {
    this.crossfadeValue = event.target.value;
    const percentage = (this.crossfadeValue / 50) * 100;
    event.target.style.setProperty('--progress', `${percentage}%`);
  }

  seekTo(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.audioService.seekTo(parseFloat(input.value));
  }

  // These methods are not implemented but kept to maintain compatibility
  Duration(_event: Event, _t16: number) {
    // Implementation not needed as we're using the service now
  }

  updateTime(_event: Event, _t16: number) {
    // Implementation not needed as we're using the service now
  }
  scrollIndex: number = 0;
  scrollSongs() {
    const songScroll: HTMLElement = document.querySelector('.song-scroll')!;
    const songWidth = songScroll.children[0].clientWidth; // Get the width of a single song container
    songScroll.scrollLeft = songWidth * this.scrollIndex; // Scroll horizontally by the width of a song
  }

  isSidebarExpanded = false;
  private sidebarSubscription!: Subscription;

  ngOnDestroy(): void {
    if (this.sidebarSubscription) {
      this.sidebarSubscription.unsubscribe();
    }
  }
}