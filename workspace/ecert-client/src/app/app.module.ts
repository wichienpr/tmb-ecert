import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { SuiModule } from 'ng2-semantic-ui';
import { AppComponent } from './app.component';
import { LoginComponent } from './buckwaframework/project/pages/login/login.component';
import { AppRoutingModule } from './buckwaframework/common/configs/app-routing.module';
import { ComponentsModule } from './buckwaframework/common/components/components.module';
import { FormsModule } from '@angular/forms';
import { EmailConfigurationComponent } from './buckwaframework/project/setup/email-configuration/email-configuration.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,

    
  
    
  ],
  imports: [
    BrowserModule,
    SuiModule,
    ComponentsModule,
    AppRoutingModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
