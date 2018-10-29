import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

// Routing
import { Nrq00000RoutingModule } from 'app/tmb-ecert/nrq00000/nrq00000-routing.module';
// Components
import { Nrq01000Component } from './nrq01000/nrq01000.component';
import { Nrq02000Component } from './nrq02000/nrq02000.component';
// Module Components
import { ModalModule, CalendarModule, DropdownModule } from 'components/';
// Module Directives
import { DisableControlModule } from 'directives/';
import { PipesModule } from 'app/baiwa/common/pipes/pipes.module';

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
    // PipesModule
    PipesModule,
  ],
  declarations: [
    Nrq01000Component,
    Nrq02000Component,
  ]
})
export class Nrq00000Module { }
