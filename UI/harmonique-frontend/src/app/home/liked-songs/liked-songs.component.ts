import { Component, ElementRef, ViewChild, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
@Component({
  selector: 'app-liked-songs',
  imports: [CommonModule, RouterModule],
  templateUrl: './liked-songs.component.html',
  styleUrl: './liked-songs.component.scss'
})
export class LikedSongsComponent {
  crossfadeValue: number = 5;
  goBack(): void {
    window.history.back();
  }
  // Update the crossfade value
  onCrossfadeChange(event: any): void {
    this.crossfadeValue = event.target.value;

    const percentage = (this.crossfadeValue / 50) * 100;
    event.target.style.setProperty('--progress', `${percentage}%`);
  }
  currentStep: number = 1;
  totalSteps: number = 3;

  isSidebarVisible: boolean = false;
  progressValue: number = 5;

  importedSongs: any[] = [];
  currentSongIndex: number = -1;

  availableImages = [
    'assets/51c547366f2853da1052e531f4bfe4d5.jpg',
    'assets/770a9cca1e543e6edeae6747db9522d2.jpg',
    'assets/956b070b9df64cdd16d966caa1e016bf.jpg',
    'assets/aa53683a96a23571867f1eafa0d845a1.jpg',
    'assets/Listening To Music GIF - Head Phones Music Recording Studio - Discover & Share GIFs.gif',
    'assets/Mask Group.png'
  ];


  recommendedSongs: any[] = [
    { title: 'Believer', artist: 'Imagine Dragons' },
    { title: 'Monsters Go Bump', artist: 'Erika Recinos' },
    { title: 'Moment Apart', artist: 'ODESZA' }
  ];

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private changeDetectorRef: ChangeDetectorRef
  ) { }

  getRandomImage(): string {
    const index = Math.floor(Math.random() * this.availableImages.length);
    return this.availableImages[index];
  }

  loadFiles(event: any): void {
    const files: FileList = event.target.files;
    this.importedSongs = [];

    for (let i = 0; i < files.length; i++) {
      const file = files[i];
      const url = URL.createObjectURL(file);
      const audio = new Audio(url);

      const songObj = {
        name: file.name,
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
      });

      audio.addEventListener('timeupdate', () => {
        songObj.currentTime = audio.currentTime;
      });

      this.importedSongs.push(songObj);
    }
  }

  togglePlay(index: number | null): void {
    if ((index === null || index === -1) && this.importedSongs.length > 0) {
      index = 0;
    }

    if (index === null || !this.importedSongs[index]) return;

    const currentSong = this.importedSongs[index];
    this.currentSongIndex = index;

    if (currentSong.audio.paused) {
      this.importedSongs.forEach((song, i) => {
        if (i !== index && song.audio) {
          song.audio.pause();
          song.isPlaying = false;
        }
      });
      currentSong.audio.play();
      currentSong.isPlaying = true;
    } else {
      currentSong.audio.pause();
      currentSong.isPlaying = false;
    }
  }

  get currentTime(): string {
    const currentSong = this.importedSongs[this.currentSongIndex];
    return currentSong ? this.formatTime(currentSong.currentTime) : '0:00';
  }

  get duration(): string {
    const currentSong = this.importedSongs[this.currentSongIndex];
    return currentSong ? this.formatTime(currentSong.duration) : '0:00';
  }

  private formatTime(sec: number): string {
    const minutes = Math.floor(sec / 60);
    const seconds = Math.floor(sec % 60).toString().padStart(2, '0');
    return `${minutes}:${seconds}`;
  }


  seekTo(event: Event): void {
    const target = event.target as HTMLInputElement;
    const seekTime = parseFloat(target.value);
    const currentSong = this.importedSongs[this.currentSongIndex];

    if (currentSong?.audio) {
      const clampedTime = Math.max(0, Math.min(seekTime, currentSong.duration || 0));
      currentSong.audio.currentTime = clampedTime;
      currentSong.currentTime = clampedTime;
      this.changeDetectorRef.detectChanges();
    }
  }

  Duration($event: Event, _t16: number) {
    throw new Error('Method not implemented.');
  }

  updateTime($event: Event, _t16: number) {
    throw new Error('Method not implemented.');
  }
}


