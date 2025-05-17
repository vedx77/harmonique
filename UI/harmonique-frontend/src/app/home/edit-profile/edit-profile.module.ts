import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { routes } from '../../app.routes';
import { EditProfileRoutingModule } from './edit-profile-routing.module';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    EditProfileRoutingModule,
    RouterModule.forChild(routes)
  ]
})
export class EditProfileModule { }
