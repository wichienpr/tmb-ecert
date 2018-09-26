import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Nrq00000RoutingModule } from './nrq00000-routing.module';
import { Nrq000100Component } from './nrq000100/nrq000100.component';
import { StoreModule } from '@ngrx/store';
import { nrq00000Reducer } from './nrq00000.reducer';
import { Nrq000200Component } from './nrq000200/nrq000200.component';
import { ModalModule } from 'components/modal/modal.module';

@NgModule({
  imports: [
    CommonModule,
    Nrq00000RoutingModule,
    ModalModule,
    // StoreModule.forRoot({ nrq0100: nrq00000Reducer })
    StoreModule.forFeature('nrq0100', nrq00000Reducer),
  ],
  declarations: [Nrq000100Component, Nrq000200Component]
})
export class Nrq00000Module { }
