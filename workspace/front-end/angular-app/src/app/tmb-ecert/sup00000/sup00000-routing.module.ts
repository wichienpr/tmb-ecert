import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Sup01000Component } from 'app/tmb-ecert/sup00000/sup01000/sup01000.component';
import { sup01100Component } from 'app/tmb-ecert/sup00000/sup01100/sup01100.component';
import { Sup02000Component } from 'app/tmb-ecert/sup00000/sup02000/sup02000.component';

const routes: Routes = [
  { path: 'sup01000', component: Sup01000Component },
  { path: 'sup01100', component: sup01100Component },
  { path: 'sup02000', component: Sup02000Component },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class Sup00000RoutingModule { }
