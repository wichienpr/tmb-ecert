import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { Rep01000Component } from 'app/tmb-ecert/rep00000/rep01000/rep01000.component';
import { Rep02000Component } from 'app/tmb-ecert/rep00000/rep02000/rep02000.component';
import { Rep03000Component } from 'app/tmb-ecert/rep00000/rep03000/rep03000.component';
import { Rep02100Component } from 'app/tmb-ecert/rep00000/rep02000/rep02100/rep02100.component';
import { Rep02200Component } from 'app/tmb-ecert/rep00000/rep02000/rep02200/rep02200.component';

const routes: Routes = [
  
    { path: 'rep01000', component: Rep01000Component },
    { path: 'rep02000', component: Rep02000Component },
    { path: 'rep02100', component: Rep02100Component },
    { path: 'rep02200', component: Rep02200Component },
    { path: 'rep03000', component: Rep03000Component }
  
];
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class Rep00000RoutingModule { }
