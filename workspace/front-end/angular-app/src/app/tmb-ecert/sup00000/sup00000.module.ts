import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Sup00000RoutingModule } from './sup00000-routing.module';
import { Sup01000Component } from './sup01000/sup01000.component';
import { Sup01010Component } from './sup01010/sup01010.component';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Sup02000Component } from './sup02000/sup02000.component';

@NgModule({
  imports: [
    CommonModule,
    Sup00000RoutingModule
    ,ReactiveFormsModule
    ,FormsModule
  ],
  declarations: [Sup01000Component, Sup01010Component, Sup02000Component]
})
export class Sup00000Module { }
