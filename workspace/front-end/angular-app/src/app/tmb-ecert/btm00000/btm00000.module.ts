import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Btm00000RoutingModule } from './btm00000-routing.module';
import { Btm01000Component } from './btm01000/btm01000.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ModalModule, DropdownModule, CalendarModule } from 'app/baiwa/common/components';
import { DatatableModule } from 'app/baiwa/common/directives';
import { pipe } from 'rxjs';
import { PipesModule } from 'app/baiwa/common/pipes/pipes.module';

@NgModule({
  imports: [
    CommonModule,
    Btm00000RoutingModule
    ,ReactiveFormsModule
    ,FormsModule
    ,ModalModule
    ,DropdownModule
    ,CalendarModule
    ,DatatableModule
    ,PipesModule
  ],
  declarations: [Btm01000Component]
})
export class Btm00000Module { }
