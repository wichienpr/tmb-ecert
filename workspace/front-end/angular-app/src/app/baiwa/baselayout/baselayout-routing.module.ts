import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SemanticBodyComponent } from './semantic-body/semantic-body.component';

const routes: Routes = [
  {
    path: '',
    component: SemanticBodyComponent,
    children: [
      // For Examples
      { path: 'examples', loadChildren: 'app/tmb-ecert/example/example.module#ExampleModule' },
      // TMB Modules
      { path: 'nrq00000', loadChildren: 'app/tmb-ecert/nrq00000/nrq00000.module#Nrq00000Module' },
    ]
  }
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BaselayoutRoutingModule { }
