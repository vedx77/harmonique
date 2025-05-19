import { Component, ElementRef, ViewChild, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { environment } from '../../environments/environment.development';
import { Subscription } from 'rxjs';
import { AuthService } from '../core/services/auth.service';
import { AudioPlayerService } from '../core/services/audio-player.service';
import { SidebarService } from '../core/services/sidebar.service';
import { LikeService } from '../core/services/like.service';
import { UserService } from '../core/services/user.service';

@Component({
  selector: 'app-home',
  standalone: true,
  templateUrl: './home.component.html',
  imports: [RouterModule, CommonModule, MatCardModule, MatIconModule],
  styleUrls: ['./home.component.scss'],
})

export class HomeComponent {
  form: FormGroup;
  currentStep: number = 1;
  totalSteps: number = 3;

  @ViewChild('sidebar') sidebar!: ElementRef;

  isSidebarVisible: boolean = false;
  isGridView = true;
  crossfadeValue: number = 5;
  progressValue: number = 5;

  importedSongs: any[] = [];
  backendSongs: any[] = []; // Added this to store fetched songs from backend
  currentSongIndex: number = -1;

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private changeDetectorRef: ChangeDetectorRef,
    public audioService: AudioPlayerService,
    private http: HttpClient, // Added HttpClient
    private authService: AuthService,
    private sidebarService: SidebarService,
    private likeService: LikeService,
    private userService: UserService
  ) {
    this.form = this.fb.group({
      step1: ['', Validators.required],
      step2: ['', Validators.required],
      step3: ['', Validators.required]
    });
  }

  isSidebarExpanded = false;
  private sidebarSubscription!: Subscription;
  userId: number = 0;
  likedSongIds: number[] = [];
  currentSongTitle: string = '';
  currentSongImage: string = '';

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

    this.audioService.currentSongChanged.subscribe(song => {
      this.currentSongTitle = song?.name || '';
      this.currentSongImage = song?.image || '';
      this.changeDetectorRef.detectChanges(); // ensure UI reflects changes
    });

    this.sidebarSubscription = this.sidebarService.expanded$.subscribe((expanded) => {
      this.isSidebarExpanded = expanded;
      this.changeDetectorRef.detectChanges();
    });

    // Fetch user ID and liked songs
    this.userService.getUserProfile().subscribe({
      next: (user) => {
        this.userId = user.id;
        this.likeService.getLikedSongsByUser(this.userId).subscribe({
          next: (likedIds) => {
            this.likedSongIds = likedIds.map(id => Number(id));
            this.fetchSongsFromBackend(); // Load songs after knowing liked ones
          },
          error: (err) => console.error('Error fetching liked songs:', err)
        });
      },
      error: (err) => console.error('Error fetching user profile:', err)
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

  downloadSong(songId: string): void {
    const downloadUrl = `${environment.songsApi}/download/${songId}`;
    window.open(downloadUrl, '_blank');
  }

  fetchSongsFromBackend(): void {
    this.http.get<any[]>(environment.songsApi).subscribe(
      (data) => {
        const formattedSongs = data.map(song => {
          const audio = new Audio(song.url); // Assumes `url` field holds audio link
          const songObj = {
            id: song.id,
            title: song.title,
            src: song.url,
            audio: audio,
            isPlaying: false,
            currentTime: 0,
            duration: 0,
            artist: song.artist,
            image: song.imageUrl,
            isLiked: this.likedSongIds.includes(song.id)
          };

          audio.addEventListener('loadedmetadata', () => {
            songObj.duration = audio.duration;
          });

          audio.addEventListener('timeupdate', () => {
            songObj.currentTime = audio.currentTime;
          });

          return songObj;
        });

        this.backendSongs = formattedSongs;
        this.audioService.setSongs(formattedSongs);
      },
      (error) => {
        console.error('Error fetching songs:', error);
      }
    );
  }

  playBackendSong(index: number): void {
    const songToPlay = this.backendSongs[index];
    this.audioService.playSong(songToPlay);
  }

  trackBySongId(index: number, song: any): number {
    return song.id;
  }

  toggleLike(songId: number): void {
    const isCurrentlyLiked = this.likedSongIds.includes(songId);

    // Update likedSongIds immediately
    if (isCurrentlyLiked) {
      this.likedSongIds = this.likedSongIds.filter(id => id !== songId);
    } else {
      this.likedSongIds.push(songId);
    }

    // Immediately toggle UI state
    this.updateSongLikeStatus(songId, !isCurrentlyLiked);

    // Fire-and-forget the API call
    const likeOp = isCurrentlyLiked
      ? this.likeService.unlikeSong(this.userId, songId)
      : this.likeService.likeSong(this.userId, songId);

    likeOp.subscribe({
      next: () => {
        // No need to do anything; state already updated
      },
      error: (err) => {
        console.warn('Like/Unlike failed:', err);
      }
    });
  }

  toggleView(): void {
    this.isGridView = !this.isGridView;
  }

  updateSongLikeStatus(songId: number, isLiked: boolean): void {
    const index = this.backendSongs.findIndex(s => s.id === songId);
    if (index > -1) {
      const updatedSong = {
        ...this.backendSongs[index],
        isLiked: isLiked
      };

      // Replace the song object in the array to trigger change detection
      this.backendSongs = [
        ...this.backendSongs.slice(0, index),
        updatedSong,
        ...this.backendSongs.slice(index + 1)
      ];
    }
  }

  togglePlay(index: number): void {
    // Use the audio service to control playback
    this.audioService.togglePlay(index);
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

  ngOnDestroy(): void {
    if (this.sidebarSubscription) {
      this.sidebarSubscription.unsubscribe();
    }
  }
}