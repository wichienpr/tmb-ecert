import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Crs00000RoutingModule } from './crs00000-routing.module';
import { Crs01000Component } from './crs01000/crs01000.component';

// Module Components
import { ModalModule, CalendarModule, DropdownModule } from 'components/';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
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