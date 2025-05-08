import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LikedSongsRoutingModule } from './liked-songs-routing.module';
import { RouterModule } from '@angular/router';
import { routes } from '../../app.routes';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    LikedSongsRoutingModule,
    RouterModule.forChild(routes)
  ]
})
export class LikedSongsModule { }
