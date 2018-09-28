import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { StoreModule } from '@ngrx/store';

// Routing
import { Nrq00000RoutingModule } from 'app/tmb-ecert/nrq00000/nrq00000-routing.module';
// Components
import { Nrq01000Component } from 'app/tmb-ecert/nrq00000/nrq01000/nrq01000.component';
import { Nrq02000Component } from './nrq02000/nrq02000.component';
// Reducers
import { certificateReducer } from './nrq02000/nrq02000.reducers';
// Module Components
import { ModalModule } from 'components/modal/modal.module';
import { DropdownModule } from 'components/dropdown/dropdown.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    // Routing
    Nrq00000RoutingModule,
    // Components
    ModalModule,
    DropdownModule,
    // StoreModule.forRoot({ nrq01000: nrqReducer })
    StoreModule.forFeature('certificate', certificateReducer),
  ],
  declarations: [Nrq01000Component, Nrq02000Component]
})
export class Nrq00000Module { }
