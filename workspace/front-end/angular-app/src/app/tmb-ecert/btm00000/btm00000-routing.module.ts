import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { Btm01000Component } from 'app/tmb-ecert/btm00000/btm01000/btm01000.component';

const routes: Routes = [
  { path: 'btm01000', component: Btm01000Component },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class Btm00000RoutingModule { }
