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
import { AjaxService, DropdownService, ModalService } from 'services/';

import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { ecerdashBoardReducer } from 'app/dash-board.reducer';
import { CanDeactivateGuard } from './baiwa/baselayout/deactivate.guard';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    HttpModule,

    // Components
    ModalModule,
    // Store
    StoreModule.forRoot({ user: userReducer , ecerdashboard : ecerdashBoardReducer }),
    StoreDevtoolsModule.instrument({
      maxAge: 25, // Retains last 25 states      
    }),
  ],
  providers: [
    AjaxService,
    DropdownService,
    ModalService,
    CanDeactivateGuard
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
