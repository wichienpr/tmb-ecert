import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Srn00000RoutingModule } from './srn00000-routing.module';
import { Srn01000Component } from './srn01000/srn01000.component';

import { ReactiveFormsModule } from '@angular/forms';
import { DisableControlModule, DatatableModule } from 'directives/';

import { PipesModule } from 'app/baiwa/common/pipes/pipes.module';


@NgModule({
  imports: [
    CommonModule,

    FormsModule,
    ReactiveFormsModule,

    Srn00000RoutingModule,

    DisableControlModule,
    DatatableModule,
    PipesModule,
  ],
  declarations: [Srn01000Component]
})
export class Srn00000Module { }
