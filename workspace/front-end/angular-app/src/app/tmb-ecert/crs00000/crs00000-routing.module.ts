import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { Crs01000Component } from './crs01000/crs01000.component';

const routes: Routes = [
  { path: 'crs01000', component: Crs01000Component },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class Crs00000RoutingModule { }