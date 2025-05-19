import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { SearchService } from '../../core/services/search.service';
import { AudioPlayerService } from '../../core/services/audio-player.service';
import { LikeService } from '../../core/services/like.service';
import { UserService } from '../../core/services/user.service';
import { environment } from '../../../environments/environment.development';

@Component({
  selector: 'app-search',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatIconModule],
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {
  query: string = '';
  searchResults: any[] = [];
  loading = false;
  errorMessage = '';
  likedSongIds: number[] = [];
  userId: number = 0;
  isGridView: boolean = true;

  constructor(
    private route: ActivatedRoute,
    private searchService: SearchService,
    private audioService: AudioPlayerService,
    private likeService: LikeService,
    private userService: UserService,
    private cd: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.query = params['q'] || '';
      if (this.query.trim()) {
        this.fetchUserAndLikes();
      }
    });
  }

  fetchUserAndLikes(): void {
    this.userService.getUserProfile().subscribe({
      next: (user) => {
        this.userId = user.id;
        this.likeService.getLikedSongsByUser(this.userId).subscribe({
          next: (likedIds) => {
            this.likedSongIds = likedIds;
            this.performSearch(this.query);
          },
          error: (err) => {
            console.error('Error fetching liked songs:', err);
            this.performSearch(this.query);
          }
        });
      },
      error: (err) => {
        console.error('Error fetching user profile:', err);
        this.performSearch(this.query);
      }
    });
  }

  toggleView(): void {
    this.isGridView = !this.isGridView;
  }

  performSearch(query: string): void {
    this.loading = true;
    this.errorMessage = '';
    this.searchService.searchSongs(query).subscribe({
      next: (results) => {
        this.searchResults = results.map(song => ({
          ...song,
          image: song.imageUrl,
          isLiked: this.likedSongIds.includes(song.id)
        }));
        this.loading = false;
        this.cd.detectChanges();
      },
      error: (err) => {
        this.errorMessage = 'Something went wrong while searching.';
        this.loading = false;
        console.error(err);
      }
    });
  }

  playSong(index: number): void {
    const song = this.searchResults[index];
    this.audioService.playSong(song);
  }

  toggleLike(songId: number): void {
    const index = this.likedSongIds.indexOf(songId);

    if (index > -1) {
      this.likeService.unlikeSong(this.userId, songId).subscribe({
        next: () => {
          this.likedSongIds.splice(index, 1);
          this.updateSongLikeStatus(songId, false);
        },
        error: (err) => console.error('Error unliking song:', err)
      });
    } else {
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
    const song = this.searchResults.find(s => s.id === songId);
    if (song) {
      song.isLiked = isLiked;
      this.cd.detectChanges();
    }
  }

  downloadSong(songId: number): void {
    const downloadUrl = `${environment.songsApi}/download/${songId}`;
    window.open(downloadUrl, '_blank');
  }
}


// import { Component, OnInit } from '@angular/core';
// import { ActivatedRoute } from '@angular/router';
// import { CommonModule } from '@angular/common';
// import { SearchService } from '../services/search.service';

// @Component({
//   selector: 'app-search',
//   standalone: true,
//   imports: [CommonModule],
//   templateUrl: './search.component.html',
//   styleUrls: ['./search.component.scss']
// })
// export class SearchComponent implements OnInit {
//   query: string = '';
//   searchResults: any[] = [];
//   loading = false;
//   errorMessage = '';

//   constructor(
//     private route: ActivatedRoute,
//     private searchService: SearchService
//   ) {}

//   ngOnInit(): void {
//     this.route.queryParams.subscribe(params => {
//       this.query = params['q'] || '';
//       if (this.query.trim()) {
//         this.performSearch(this.query);
//       }
//     });
//   }

//   performSearch(query: string): void {
//     this.loading = true;
//     this.errorMessage = '';
//     this.searchService.searchSongs(query).subscribe({
//       next: (results) => {
//         this.searchResults = results;
//         this.loading = false;
//       },
//       error: (err) => {
//         this.errorMessage = 'Something went wrong while searching.';
//         this.loading = false;
//         console.error(err);
//       }
//     });
//   }
// }