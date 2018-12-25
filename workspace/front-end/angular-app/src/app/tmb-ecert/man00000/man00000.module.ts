import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Man00000RoutingModule } from './man00000-routing.module';
import { Man01000Component } from './man01000/man01000.component';

@NgModule({
  imports: [
    CommonModule,
    Man00000RoutingModule
  ],
  declarations: [ Man01000Component]
})
export class Man00000Module { }
