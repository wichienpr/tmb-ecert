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
      { path: 'home', loadChildren: 'app/baiwa/home/home.module#HomeModule' },
      { path: 'nrq', loadChildren: 'app/tmb-ecert/nrq00000/nrq00000.module#Nrq00000Module' },
      { path: 'rep', loadChildren: 'app/tmb-ecert/rep00000/rep00000.module#Rep00000Module' },
      { path: 'crs', loadChildren: 'app/tmb-ecert/crs00000/crs00000.module#Crs00000Module' },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BaselayoutRoutingModule { }
