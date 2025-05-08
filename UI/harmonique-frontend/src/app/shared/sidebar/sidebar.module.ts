import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SidebarRoutingModule } from './sidebar-routing.module';
import { RouterModule } from '@angular/router';
import { routes } from '../../app.routes';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    SidebarRoutingModule,
    RouterModule.forChild(routes)
  ]
})
export class SidebarModule { }
