import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { Ex1Component } from 'app/tmb-ecert/example/ex1/ex1.component';

const routes: Routes = [
  { path: 'ex1', component: Ex1Component }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ExampleRoutingModule { }
