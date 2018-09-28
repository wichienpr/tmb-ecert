import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { StoreModule } from '@ngrx/store';

// Routing
import { Nrq00000RoutingModule } from 'app/tmb-ecert/nrq00000/nrq00000-routing.module';
// Components
import { Nrq01000Component } from 'app/tmb-ecert/nrq00000/nrq01000/nrq01000.component';
import { Nrq02000Component } from './nrq02000/nrq02000.component';
// Reducers
import { certificateReducer } from './nrq02000/nrq02000.reducers';
// Module Components
import { ModalModule, CalendarModule, DropdownModule } from 'components/';

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
    // StoreModule.forRoot({ nrq01000: nrqReducer })
    StoreModule.forFeature('certificate', certificateReducer),
  ],
  declarations: [Nrq01000Component, Nrq02000Component]
})
export class Nrq00000Module { }
