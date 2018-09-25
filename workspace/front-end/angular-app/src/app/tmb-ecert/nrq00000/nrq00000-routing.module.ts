import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { Nrq000100Component } from './nrq000100/nrq000100.component';

const routes: Routes = [
  
    { path: 'nrq00100', component: Nrq000100Component },
  
];
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class Nrq00000RoutingModule { }
