import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Nrq00000RoutingModule } from 'app/tmb-ecert/nrq00000/nrq00000-routing.module';
import { Nrq01000Component } from 'app/tmb-ecert/nrq00000/nrq01000/nrq01000.component';
import { StoreModule } from '@ngrx/store';
import { nrq00000Reducer } from 'app/tmb-ecert/nrq00000/nrq00000.reducer';
import { Nrq02000Component } from './nrq02000/nrq02000.component';
import { ModalModule } from 'components/modal/modal.module';
import { DropdownModule } from 'components/dropdown/dropdown.module';

@NgModule({
  imports: [
    CommonModule,
    Nrq00000RoutingModule,
    // Components
    ModalModule,
    DropdownModule,
    // StoreModule.forRoot({ nrq01000: nrqReducer })
    StoreModule.forFeature('nrq01000', nrq00000Reducer),
  ],
  declarations: [Nrq01000Component, Nrq02000Component]
})
export class Nrq00000Module { }
