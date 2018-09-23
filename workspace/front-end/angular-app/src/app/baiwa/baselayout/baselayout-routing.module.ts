import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SemanticBodyComponent } from './semantic-body/semantic-body.component';

const routes: Routes = [
  {
    path: '',
    component : SemanticBodyComponent,
    children: [
      // { path: 'customers',  loadChildren: '../customers/customers.module#CustomersModule'},
      // { path: 'orders',  loadChildren: '../orders/orders.module#OrdersModule'}
    ]
  }
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BaselayoutRoutingModule { }
