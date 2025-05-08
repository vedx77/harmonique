import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home.component';

const routes: Routes = [
  { path: '', component: HomeComponent }  // The route for the HomeComponent
];

@NgModule({
  imports: [RouterModule.forChild(routes)],  // Make sure this line is correct
  exports: [RouterModule]  // Export RouterModule to make it accessible in HomeModule
})
export class HomeRoutingModule { }