import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { LoginComponent } from "../../project/pages/login/login.component";
import { PageNotFoundComponent } from "projects/pages/pagenotfound/pagenotfound.component";
import { TestComponent } from "projects/pages/test/test.component";

const routes: Routes = [
  { path: "", redirectTo: "login", pathMatch: "full" },
  { path: "", loadChildren: "./layout-routing.module#LayoutRoutingModule" },
  { path: "test", component: TestComponent },
  { path: "login", component: LoginComponent },
  { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  declarations: [PageNotFoundComponent],
  exports: [RouterModule]
})
export class AppRoutingModule { }
