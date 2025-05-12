import { Component, OnInit } from '@angular/core';
import { LikeService } from '../services/like.service';
import { UserService } from '../services/user.service';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  standalone: true,
  selector: 'app-liked-songs',
  templateUrl: './liked-songs.component.html',
  styleUrls: ['./liked-songs.component.scss'],
  imports: [MatCardModule, MatIconModule, CommonModule]
})
export class LikedSongsComponent implements OnInit {
  likedSongs: any[] = [];

  constructor(
    private likeService: LikeService,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.userService.getUserProfile().subscribe({
      next: (user) => {
        const userId = user.id;
        this.likeService.getLikedSongs(userId).subscribe({
          next: (songs) => {
          this.likedSongs = songs,
            console.log(this.likedSongs);
        },
          error: (err) => console.error('Error fetching liked songs:', err)
        });
      },
      error: (err) => console.error('Error fetching user profile:', err)
    });
  }
}