import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { LoginComponent } from "../../project/pages/login/login.component";
import { HomeComponent } from "../../project/pages/home/home.component";
import { MonitoringPerformaComponent } from "../../project/performa/monitoring-performa/monitoring-performa.component";
import { ECertificateDayComponent } from "../../project/report/e-certificate-day/e-certificate-day.component";
import { ECertificateMonthlyComponent } from "../../project/report/e-certificate-monthly/e-certificate-monthly.component";
import { ClientSignedComponent } from "../../project/new-Request/client-signed/client-signed.component";
import { DescriptionComponent } from "../../project/performa/description/description.component";
import { OutputVATComponent } from "../../project/report/output-vat/output-vat.component";

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
  },
  {
    path: "e-certificate-day",
    component: ECertificateDayComponent    
  } ,{
    path: "e-certificate-monthly",
    component: ECertificateMonthlyComponent    
  },
  {
    path: "client-signed",
    component: ClientSignedComponent    
  },
  {
    path: "description",
    component: DescriptionComponent    
  }
  ,
  {
    path: "output-VAT",
    component: OutputVATComponent    
  }
  
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
