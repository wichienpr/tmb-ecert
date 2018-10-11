import { NgModule } from '@angular/core';
import { StoreModule } from '@ngrx/store';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { ExampleRoutingModule } from './example-routing.module';
import { reducers } from './example.reducer';

import { Ex1Component } from './ex1/ex1.component';
import { Ex2Component } from './ex2/ex2.component';
import { Ex3Component } from './ex3/ex3.component';
import { Ex4Component } from './ex4/ex4.component';
import { CalendarModule, ModalModule } from 'app/baiwa/common/components';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ExampleRoutingModule,
    // NGRX
    StoreModule.forFeature('examples', reducers),
    // Components
    CalendarModule,
    ModalModule
  ],
  declarations: [Ex1Component, Ex2Component, Ex3Component, Ex4Component]
})
export class ExampleModule { }
