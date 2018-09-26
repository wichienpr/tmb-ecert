import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './baiwa/login/login.component';

const routes: Routes = [
  { path: "", redirectTo: "login", pathMatch: "full" },
  { path: '', loadChildren: './baiwa/baselayout/baselayout.module#BaselayoutModule' },
  { path: 'login', component: LoginComponent },
];

@NgModule({
  imports: [
    RouterModule.forRoot(
      routes,
      { useHash: true } // <-- debugging purposes only
    )
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
