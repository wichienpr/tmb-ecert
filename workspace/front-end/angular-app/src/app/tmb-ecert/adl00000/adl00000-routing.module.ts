import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { Adl01000Component } from 'app/tmb-ecert/adl00000/adl01000/adl01000.component';

const routes: Routes = [
  
    { path: 'adl01000', component: Adl01000Component }
  
];
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class Adl00000RoutingModule { }
