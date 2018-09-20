import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

// Components
import { AppComponent } from './app.component';
import { AppRoutingModule } from 'configs/app-routing.module';
import { LoginComponent } from 'projects/pages/login/login.component';

// Custom Component Modules
import { ModalModule } from "components/index";

// Store
import { StoreModule } from "@ngrx/store";
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { testReducer } from "reducers/index"
import { TestComponent } from 'projects/pages/test/test.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    TestComponent
  ],
  imports: [
    // Common Modules
    AppRoutingModule,
    BrowserModule,
    FormsModule,

    // Custom Component Modules
    ModalModule,

    // Store Module
    StoreModule.forRoot({
      test: testReducer
    }),
    StoreDevtoolsModule.instrument({
      maxAge: 10
    })
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
