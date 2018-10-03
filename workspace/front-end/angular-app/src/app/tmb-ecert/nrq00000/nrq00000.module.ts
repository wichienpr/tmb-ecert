import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
// import { StoreModule } from '@ngrx/store';

// Routing
import { Nrq00000RoutingModule } from 'app/tmb-ecert/nrq00000/nrq00000-routing.module';
// Components
import { Nrq01000Component } from './nrq01000/nrq01000.component';
import { Nrq02000Component } from './nrq02000/nrq02000.component';
import { Nrq03000Component } from './nrq03000/nrq03000.component';
// Reducers
// import { exampleReducer } from './example/example.reducers';
// Module Components
import { ModalModule, CalendarModule, DropdownModule } from 'components/';
// Module Directives
import { DisableControlModule } from 'directives/';
import { EmptyStringPipe, AccountNumberPipe, DecimalFormatPipe, DateStringPipe } from 'pipes/';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    // Routing
    Nrq00000RoutingModule,
    // Components
    ModalModule,
    DropdownModule,
    CalendarModule,
    // Directives
    DisableControlModule,
    // StoreModule.forFeature('example', exampleReducer),
  ],
  declarations: [
    Nrq01000Component,
    Nrq02000Component,
    Nrq03000Component,
    // Pipes
    EmptyStringPipe,
    AccountNumberPipe,
    DecimalFormatPipe,
    DateStringPipe
  ]
})
export class Nrq00000Module { }
