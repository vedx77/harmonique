import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LikedSongsRoutingModule } from './liked-songs-routing.module';
import { RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { LikedSongsComponent } from './liked-songs.component';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    LikedSongsRoutingModule,
    RouterModule,
    MatIconModule,
    MatCardModule,
    LikedSongsComponent
  ]
})
export class LikedSongsModule { }