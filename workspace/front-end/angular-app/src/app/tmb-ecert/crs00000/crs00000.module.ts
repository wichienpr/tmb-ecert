import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';


import { Crs01000Component } from 'app/tmb-ecert/crs00000/crs01000/crs01000.component';
import { Crs00000RoutingModule } from 'app/tmb-ecert/crs00000/crs00000-routing.module';

import { StoreModule } from '@ngrx/store';
import { ReactiveFormsModule } from '@angular/forms';



// Module Components
import { ModalModule, CalendarModule, DropdownModule } from 'components/';



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
  ],
  declarations: [Crs01000Component]
})
export class Crs00000Module { }