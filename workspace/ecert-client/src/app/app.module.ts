import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { SuiModule } from 'ng2-semantic-ui';
import { AppComponent } from './app.component';
import { LoginComponent } from './buckwaframework/project/pages/login/login.component';
import { HomeComponent } from './buckwaframework/project/pages/home/home.component';
import { AppRoutingModule } from './buckwaframework/common/configs/app-routing.module';
import { MonitoringPerformaComponent } from './buckwaframework/project/performa/monitoring-performa/monitoring-performa.component';
import { ECertificateDayComponent } from './buckwaframework/project/report/e-certificate-day/e-certificate-day.component';
import { ECertificateMonthlyComponent } from './buckwaframework/project/report/e-certificate-monthly/e-certificate-monthly.component';
import { ClientSignedComponent } from './buckwaframework/project/new-Request/client-signed/client-signed.component';
import { DescriptionComponent } from './buckwaframework/project/performa/description/description.component';
import { OutputVATComponent } from './buckwaframework/project/report/output-vat/output-vat.component';
import { ComponentsModule } from './buckwaframework/common/components/components.module';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    MonitoringPerformaComponent,
    ECertificateDayComponent,
    ECertificateMonthlyComponent,
    ClientSignedComponent,
    DescriptionComponent,
    OutputVATComponent,
  ],
  imports: [
    BrowserModule,
    SuiModule,
    ComponentsModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
