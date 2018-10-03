import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Rep00000RoutingModule } from 'app/tmb-ecert/rep00000/rep00000-routing.module';
import { Rep01000Component } from 'app/tmb-ecert/rep00000/rep01000/rep01000.component';
import { Rep02000Component } from 'app/tmb-ecert/rep00000/rep02000/rep02000.component';
import { Rep02100Component } from 'app/tmb-ecert/rep00000/rep02000/rep02100/rep02100.component';
import { Rep03000Component } from 'app/tmb-ecert/rep00000/rep03000/rep03000.component';
import { StoreModule } from '@ngrx/store';
import { ModalModule, CalendarModule, DropdownModule }from 'components/';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { EmptyStringPipe, AccountNumberPipe, DecimalFormatPipe } from 'pipes/';
import { PipesModule } from 'app/baiwa/common/pipes/pipes.module';

@NgModule({
  imports: [
    CommonModule,
    Rep00000RoutingModule,
    FormsModule,
    ReactiveFormsModule,
    // ConponentsModule
    ModalModule,
    DropdownModule,
    CalendarModule,
    // PipesModule
    PipesModule
    // StoreModule.forRoot({ rep01000: repReducer })
  ],
  declarations: [
    Rep01000Component, 
    Rep02000Component, 
    Rep02100Component,
    Rep03000Component
  ]
})
export class Rep00000Module { }
