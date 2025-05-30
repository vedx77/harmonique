import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { routes } from '../../app.routes';
import { FaqRoutingModule } from './faq-routing.module';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    FaqRoutingModule,
    RouterModule.forChild(routes)
  ]
})
export class FaqModule { }