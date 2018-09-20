import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

// Components
import { AppComponent } from './app.component';
import { AppRoutingModule } from 'configs/app-routing.module';
import { LoginComponent } from 'projects/pages/login/login.component';

// Custom Component Modules
import { ModalModule } from "components/index";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent, 
  ],
  imports: [
    // Common Modules
    AppRoutingModule,
    BrowserModule,
    FormsModule,

    // Custom Component Modules
    ModalModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
