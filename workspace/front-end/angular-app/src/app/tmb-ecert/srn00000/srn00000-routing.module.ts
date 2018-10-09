import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { Srn01000Component } from './srn01000/srn01000.component';

const routes: Routes = [
  { path: 'srn01000', component: Srn01000Component }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class Srn00000RoutingModule { }
