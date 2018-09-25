import { NgModule } from '@angular/core';
import { StoreModule } from '@ngrx/store';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { ExampleRoutingModule } from './example-routing.module';
import { reducers } from './example.reducer';

import { Ex1Component } from './ex1/ex1.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ExampleRoutingModule,
    // NGRX
    StoreModule.forFeature('examples', reducers)
  ],
  declarations: [Ex1Component]
})
export class ExampleModule { }
