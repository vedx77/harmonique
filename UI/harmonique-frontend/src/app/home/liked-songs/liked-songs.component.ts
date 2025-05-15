// import { Component, OnInit } from '@angular/core';
// import { LikeService } from '../../core/services/like.service';
// import { UserService } from '../../core/services/user.service';
// import { CommonModule } from '@angular/common';
// import { MatCardModule } from '@angular/material/card';
// import { MatIconModule } from '@angular/material/icon';

// @Component({
//   standalone: true,
//   selector: 'app-liked-songs',
//   templateUrl: './liked-songs.component.html',
//   styleUrls: ['./liked-songs.component.scss'],
//   imports: [MatCardModule, MatIconModule, CommonModule]
// })
// export class LikedSongsComponent implements OnInit {
//   likedSongs: any[] = [];
//   userId: number = 0;
//   likedSongIds: number[] = [];

//   constructor(
//     private likeService: LikeService,
//     private userService: UserService
//   ) { }

//   ngOnInit(): void {
//     // this.userService.getUserProfile().subscribe({
//     //   next: (user) => {
//     //     const userId = user.id;
//     //     this.likeService.getLikedSongsByUser(userId).subscribe({
//     //       next: (songs) => {
//     //       this.likedSongs = songs,
//     //         console.log(this.likedSongs);
//     //     },
//     //       error: (err) => console.error('Error fetching liked songs:', err)
//     //     });
//     //   },
//     //   error: (err) => console.error('Error fetching user profile:', err)
//     // });

//     this.userService.getUserProfile().subscribe({
//       next: (user) => {
//         this.userId = user.id;

//         this.likeService.getLikedSongsByUser(this.userId).subscribe({
//           next: (songs) => {
//             this.likedSongs = songs
//               .filter(song => typeof song === 'object' && song !== null)
//               .map(song => ({
//                 ...song,
//                 isLiked: true // Since these are liked songs
//               }));

//             this.likedSongIds = this.likedSongs.map(song => song.id);
//           },
//           error: (err) => console.error('Error fetching liked songs:', err)
//         });
//       },
//       error: (err) => console.error('Error fetching user profile:', err)
//     });
//   }

//   toggleLike(songId: number): void {
//     const isCurrentlyLiked = this.likedSongIds.includes(songId);

//     if (isCurrentlyLiked) {
//       this.likedSongIds = this.likedSongIds.filter(id => id !== songId);
//     } else {
//       this.likedSongIds.push(songId);
//     }

//     this.updateSongLikeStatus(songId, !isCurrentlyLiked);

//     const likeOp = isCurrentlyLiked
//       ? this.likeService.unlikeSong(this.userId, songId)
//       : this.likeService.likeSong(this.userId, songId);

//     likeOp.subscribe({
//       next: () => { },
//       error: (err) => {
//         console.warn('Like/Unlike failed:', err);
//       }
//     });
//   }

//   // updateSongLikeStatus(songId: number, isLiked: boolean): void {
//   //   const index = this.likedSongs.findIndex(s => s.id === songId);
//   //   //if (index > -1) {
//   //     if (index > -1 && this.likedSongs[index]) {
//   //     const updatedSong = {
//   //       ...this.likedSongs[index],
//   //       isLiked: isLiked
//   //     };

//   //     this.likedSongs = [
//   //       ...this.likedSongs.slice(0, index),
//   //       updatedSong,
//   //       ...this.likedSongs.slice(index + 1)
//   //     ];

//   //     // Optionally remove unliked song from the list
//   //     if (!isLiked) {
//   //       this.likedSongs.splice(index, 1);
//   //     }
//   //   }
//   // }

//   updateSongLikeStatus(songId: number, isLiked: boolean): void {
//     const index = this.likedSongs.findIndex(s => s.id === songId);
//     if (index > -1) {
//       const updatedSong = {
//         ...this.likedSongs[index],
//         isLiked: isLiked
//       };

//       // Replace the song object in the array to trigger change detection
//       this.likedSongs = [
//         ...this.likedSongs.slice(0, index),
//         updatedSong,
//         ...this.likedSongs.slice(index + 1)
//       ];
//     }
//   }
// }

import { Component, OnInit } from '@angular/core';
import { LikeService } from '../../core/services/like.service';
import { UserService } from '../../core/services/user.service';
import { SongService } from '../../core/services/song.service';
import { AudioPlayerService } from '../../core/services/audio-player.service';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { forkJoin } from 'rxjs';
import { environment } from '../../../environments/environment.development';

@Component({
  standalone: true,
  selector: 'app-liked-songs',
  templateUrl: './liked-songs.component.html',
  styleUrls: ['./liked-songs.component.scss'],
  imports: [MatCardModule, MatIconModule, CommonModule]
})
export class LikedSongsComponent implements OnInit {
  likedSongs: any[] = [];
  userId: number = 0;
  likedSongIds: number[] = [];

  constructor(
    private likeService: LikeService,
    private userService: UserService,
    private songService: SongService,
    private audioService: AudioPlayerService
  ) { }

  ngOnInit(): void {
    this.userService.getUserProfile().subscribe({
      next: (user) => {
        this.userId = user.id;

        this.likeService.getLikedSongsByUser(this.userId).subscribe({
          next: (songIds) => {
            this.likedSongIds = songIds;

            // Use forkJoin to fetch all song details in parallel
            const songRequests = songIds.map(id => this.songService.getSongById(id));
            forkJoin(songRequests).subscribe({
              next: (songs) => {
                this.likedSongs = songs.map(song => ({
                  ...song,
                  isLiked: true,
                  image: song.imageUrl
                }));
              },
              error: (err) => console.error('Error fetching full song details:', err)
            });
          },
          error: (err) => console.error('Error fetching liked song IDs:', err)
        });
      },
      error: (err) => console.error('Error fetching user profile:', err)
    });
  }

  toggleLike(songId: number): void {
    const isCurrentlyLiked = this.likedSongIds.includes(songId);

    if (isCurrentlyLiked) {
      this.likedSongIds = this.likedSongIds.filter(id => id !== songId);
    } else {
      this.likedSongIds.push(songId);
    }

    this.updateSongLikeStatus(songId, !isCurrentlyLiked);

    const likeOp = isCurrentlyLiked
      ? this.likeService.unlikeSong(this.userId, songId)
      : this.likeService.likeSong(this.userId, songId);

    likeOp.subscribe({
      next: () => { },
      error: (err) => {
        console.warn('Like/Unlike failed:', err);
      }
    });
  }

  updateSongLikeStatus(songId: number, isLiked: boolean): void {
    const index = this.likedSongs.findIndex(s => s.id === songId);
    if (index > -1) {
      const updatedSong = {
        ...this.likedSongs[index],
        isLiked: isLiked
      };

      this.likedSongs = [
        ...this.likedSongs.slice(0, index),
        updatedSong,
        ...this.likedSongs.slice(index + 1)
      ];
    }
  }

    playLikedSong(index: number): void {
    const songToPlay = this.likedSongs[index];
    if (!songToPlay) return;

    this.audioService.playSong(songToPlay);
  }

  downloadSong(songId: string): void {
    const downloadUrl = `${environment.songsApi}/download/${songId}`;
    window.open(downloadUrl, '_blank');
  }
}