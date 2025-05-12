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
import { MatIconModule } from '@angular/material/icon';
import { SidebarService } from '../home/services/sidebar.service';
import { Subscription } from 'rxjs';
import { LikeService } from './services/like.service';
import { UserService } from './services/user.service';

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

    this.fetchSongsFromBackend();

    // Fetch user ID and liked songs
    this.userService.getUserProfile().subscribe({
      next: (user) => {
        this.userId = user.id;
        this.likeService.getLikedSongsByUser(this.userId).subscribe({
          next: (likedIds) => {
            this.likedSongIds = likedIds;
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


  // fetchSongsFromBackend(): void {
  //   this.http.get<any[]>(environment.songsApi)
  //     .subscribe(
  //       (data) => {
  //         this.backendSongs = data.map(song => ({
  //           ...song,
  //           image: song.imageUrl  // ✅ Assign the backend image URL to the image field
  //         }));
  //       },
  //       (error) => {
  //         console.error('Error fetching songs:', error);
  //       }
  //     );
  // }

  //O2
  // fetchSongsFromBackend(): void {
  //   this.http.get<any[]>(environment.songsApi).subscribe(
  //     (data) => {
  //       this.backendSongs = data.map(song => ({
  //         ...song,
  //         image: song.imageUrl,
  //         isLiked: this.likedSongIds.includes(song.id)
  //       }));
  //     },
  //     (error) => {
  //       console.error('Error fetching songs:', error);
  //     }
  //   );
  // }

  // O3
  // fetchSongsFromBackend(): void {
  //   this.http.get<any[]>(environment.songsApi).subscribe(
  //     (data) => {
  //       this.backendSongs = data.map(song => ({
  //         ...song,
  //         image: song.imageUrl,
  //         isLiked: this.likedSongIds.includes(song.id)
  //       }));

  //       // ✅ Push all songs into the global audio service queue
  //       this.audioService.setSongs(this.backendSongs);
  //     },
  //     (error) => {
  //       console.error('Error fetching songs:', error);
  //     }
  //   );
  // }


  // O4
  fetchSongsFromBackend(): void {
    this.http.get<any[]>(environment.songsApi).subscribe(
      (data) => {
        const formattedSongs = data.map(song => {
          const audio = new Audio(song.url); // Assumes `url` field holds audio link
          const songObj = {
            id: song.id,
            name: song.title,
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

  toggleLike(songId: number): void {
    const index = this.likedSongIds.indexOf(songId);

    if (index > -1) {
      // Dislike
      this.likeService.unlikeSong(this.userId, songId).subscribe({
        next: () => {
          this.likedSongIds.splice(index, 1);
          this.updateSongLikeStatus(songId, false);
        },
        error: (err) => console.error('Error unliking song:', err)
      });
    } else {
      // Like
      this.likeService.likeSong(this.userId, songId).subscribe({
        next: () => {
          this.likedSongIds.push(songId);
          this.updateSongLikeStatus(songId, true);
        },
        error: (err) => console.error('Error liking song:', err)
      });
    }
  }

  updateSongLikeStatus(songId: number, isLiked: boolean): void {
    const song = this.backendSongs.find(s => s.id === songId);
    if (song) {
      song.isLiked = isLiked;
      this.changeDetectorRef.detectChanges();
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

  ngOnDestroy(): void {
    if (this.sidebarSubscription) {
      this.sidebarSubscription.unsubscribe();
    }
  }
}