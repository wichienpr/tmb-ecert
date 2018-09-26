import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Crs00000RoutingModule } from './crs00000-routing.module';
import { Crs01000Component } from './crs01000/crs01000.component';

@NgModule({
  imports: [
    CommonModule,
    Crs00000RoutingModule
  ],
  declarations: [Crs01000Component]
})
export class Crs00000Module { }