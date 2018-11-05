import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { Nrq01000Component } from './nrq01000/nrq01000.component';
import { Nrq02000Component } from './nrq02000/nrq02000.component';
import { CanDeactivateGuard } from 'app/baiwa/baselayout/deactivate.guard';

const routes: Routes = [

  { path: 'nrq01000', component: Nrq01000Component, canDeactivate: [CanDeactivateGuard] },
  { path: 'nrq02000', component: Nrq02000Component }

];
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class Nrq00000RoutingModule { }
