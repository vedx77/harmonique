// footer.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AudioPlayerService } from '../../core/services/audio-player.service';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {
  crossfadeValue: number = 5;
  isPlaying: boolean = false;

  constructor(public audioService: AudioPlayerService) {}

  ngOnInit(): void {
    // Subscribe to isPlaying changes
    this.audioService.isPlaying$.subscribe(playing => {
      this.isPlaying = playing;
    });
  }

  /**
   * Toggles play/pause for the current song
   */
  togglePlay(): void {
    this.audioService.togglePlay();
  }

  /**
   * Skip to the next song
   */
  next(): void {
    this.audioService.next();
  }

  /**
   * Go back to the previous song
   */
  previous(): void {
    this.audioService.previous();
  }

  /**
   * Handle crossfade slider change
   */
  onCrossfadeChange(event: any): void {
    this.crossfadeValue = event.target.value;
    const percentage = (this.crossfadeValue / 50) * 100;
    event.target.style.setProperty('--progress', `${percentage}%`);
  }

  /**
   * Handle seek operation when user drags the progress slider
   */
  handleSeek(event: Event): void {
    const target = event.target as HTMLInputElement;
    this.audioService.seekTo(parseFloat(target.value));
  }

  /**
   * Get formatted current playback time
   */
  get currentTime(): string {
    return this.audioService.getCurrentTimeFormatted();
  }

  /**
   * Get formatted song duration
   */
  get duration(): string {
    return this.audioService.getDurationFormatted();
  }

  /**
   * Get current song progress as percentage (for progress bar)
   */
  get currentSongProgress(): number {
    return this.audioService.getCurrentProgress();
  }

  /**
   * Get current song playback time in seconds
   */
  get currentSongTime(): number {
    if (this.currentSong) {
      return this.currentSong.currentTime || 0;
    }
    return 0;
  }

  /**
   * Get current song duration in seconds
   */
  get currentSongDuration(): number {
    if (this.currentSong) {
      return this.currentSong.duration || 0;
    }
    return 0;
  }

  /**
   * Get current song object
   */
  get currentSong(): any {
    return this.audioService.currentSong;
  }
}