import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SidebarComponent } from '../../shared/sidebar/sidebar.component';
import { RouterModule } from '@angular/router';
import { routes } from '.../../../src/app/app.routes';
import { AboutUsRoutingModule } from './about-us-routing.module';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    AboutUsRoutingModule,
    SidebarComponent,
    RouterModule.forChild(routes)
  ]
})
export class AboutUsModule { }