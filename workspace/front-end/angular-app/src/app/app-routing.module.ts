import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './baiwa/login/login.component';
import { LoginGuard } from './baiwa/baselayout/login.guard';

const routes: Routes = [
  { path: "", redirectTo: "home", pathMatch: "full" },
  { path: '', loadChildren: './baiwa/baselayout/baselayout.module#BaselayoutModule' },
  { path: 'login', component: LoginComponent, canActivate : [LoginGuard], },
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
