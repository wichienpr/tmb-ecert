import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { SuiModule } from 'ng2-semantic-ui';
import { AppComponent } from './app.component';
import { LoginComponent } from './buckwaframework/project/pages/login/login.component';
import { HomeComponent } from './buckwaframework/project/pages/home/home.component';
import { AppRoutingModule } from './buckwaframework/common/configs/app-routing.module';
import { MonitoringPerformaComponent } from './buckwaframework/project/performa/monitoring-performa/monitoring-performa.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    MonitoringPerformaComponent,
  
 
  ],
  imports: [
    BrowserModule, SuiModule,AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
