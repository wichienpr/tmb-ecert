import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { Man01000Component } from './man01000/man01000.component';

const routes: Routes = [
  { path: 'man01000', component: Man01000Component },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class Man00000RoutingModule { }
