import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';


import { Crs01000Component } from 'app/tmb-ecert/crs00000/crs01000/crs01000.component';
import { Crs02000Component } from 'app/tmb-ecert/crs00000/crs02000/crs02000.component';
import { Crs00000RoutingModule } from 'app/tmb-ecert/crs00000/crs00000-routing.module';

import { StoreModule } from '@ngrx/store';
import { ReactiveFormsModule } from '@angular/forms';

import { DisableControlModule, DatatableModule } from 'directives/';

// Module Components
import { ModalModule, CalendarModule, DropdownModule } from 'components/';
import { PipesModule } from 'app/baiwa/common/pipes/pipes.module';



@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    // Routing
    Crs00000RoutingModule,
    // Components
    ModalModule,
    DropdownModule,
    CalendarModule,
    DisableControlModule,
    DatatableModule,
    PipesModule,
  ],
  declarations: [Crs01000Component, Crs02000Component]
})
export class Crs00000Module { }