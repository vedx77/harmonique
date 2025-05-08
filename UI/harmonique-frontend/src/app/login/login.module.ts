import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginRoutingModule } from './login-routing.module';
import { RouterModule } from '@angular/router';
import { routes } from '../app.routes';
import { HttpClientModule } from '@angular/common/http'; 


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    LoginRoutingModule,
    RouterModule.forChild(routes)
  ]
})
export class LoginModule { }
