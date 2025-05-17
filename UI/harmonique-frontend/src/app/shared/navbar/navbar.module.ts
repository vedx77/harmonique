import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { routes } from '../../app.routes';
import { NavbarRoutingModule } from './navbar-routing.module';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    NavbarRoutingModule,
    RouterModule.forChild(routes)
  ]
})
export class NavbarModule { }
