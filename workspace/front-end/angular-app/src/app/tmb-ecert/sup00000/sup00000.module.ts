import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Sup00000RoutingModule } from './sup00000-routing.module';
import { Sup01000Component } from './sup01000/sup01000.component';
import { sup01100Component } from './sup01100/sup01100.component';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Sup02000Component } from './sup02000/sup02000.component';
import { ModalModule } from 'app/baiwa/common/components';

@NgModule({
  imports: [
    CommonModule,
    Sup00000RoutingModule
    ,ReactiveFormsModule
    ,FormsModule
    ,ModalModule
  ],
  declarations: [Sup01000Component, sup01100Component, Sup02000Component]
})
export class Sup00000Module { }
