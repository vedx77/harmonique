import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegisterRoutingModule } from './register-routing.module';
import { RouterModule } from '@angular/router';
import { routes } from '../app.routes';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RegisterRoutingModule,
    RouterModule.forChild(routes)
  ]
})
export class RegisterModule { }
