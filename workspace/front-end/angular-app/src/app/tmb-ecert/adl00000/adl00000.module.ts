import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Adl00000RoutingModule } from 'app/tmb-ecert/adl00000/adl00000-routing.module';
import { StoreModule } from '@ngrx/store';
import { ModalModule, CalendarModule, DropdownModule }from 'components/';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { EmptyStringPipe, AccountNumberPipe, DecimalFormatPipe } from 'pipes/';
import { PipesModule } from 'app/baiwa/common/pipes/pipes.module';
import { Adl01000Component } from 'app/tmb-ecert/adl00000/adl01000/adl01000.component';
import { DatatableModule } from 'app/baiwa/common/directives';

@NgModule({
  imports: [
    CommonModule,
    Adl00000RoutingModule,
    FormsModule,
    ReactiveFormsModule,
    // ConponentsModule
    ModalModule,
    DropdownModule,
    CalendarModule,
    // PipesModule
    PipesModule,
    DatatableModule,
    // StoreModule.forRoot({ adl01000: adlReducer })
  ],
  declarations: [
    Adl01000Component
  ]
})
export class Adl00000Module { }
