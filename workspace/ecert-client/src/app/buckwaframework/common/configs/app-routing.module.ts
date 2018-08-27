import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { LoginComponent } from "../../project/pages/login/login.component";
import { HomeComponent } from "../../project/pages/home/home.component";
import { AppComponent } from "src/app/app.component";
import { MonitoringPerformaComponent } from "../../project/performa/monitoring-performa/monitoring-performa.component";

const routes: Routes = [
  { path: "", redirectTo: "/login", pathMatch: "full" },
  {
    path: "login",
    component: LoginComponent
  },
  {
    path: "home",
    component: HomeComponent,
    
  },
  {
    path: "performa",
    component: MonitoringPerformaComponent,
    
  }



];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
