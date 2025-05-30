import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SettingsRoutingModule } from './settings-routing.module';
import { RouterModule } from '@angular/router';
import { routes } from '../../app.routes';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    SettingsRoutingModule,
    RouterModule.forChild(routes)

  ]
})
export class SettingsModule { }