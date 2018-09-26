import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './baiwa/login/login.component';

import { FormsModule } from '@angular/forms';
import { userReducer } from './user.reducer';
import { StoreModule } from '@ngrx/store';
import { ModalModule } from 'components/modal/modal.module';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    
    // Components
    ModalModule,
    // Store
    StoreModule.forRoot({ user: userReducer }),
    StoreDevtoolsModule.instrument({
      maxAge: 25, // Retains last 25 states      
    }),
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
