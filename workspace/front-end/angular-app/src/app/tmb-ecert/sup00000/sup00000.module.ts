import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Sup00000RoutingModule } from './sup00000-routing.module';
import { Sup01000Component } from './sup01000/sup01000.component';
import { sup01100Component } from './sup01100/sup01100.component';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Sup02000Component } from './sup02000/sup02000.component';
import { ModalModule, DropdownModule } from 'app/baiwa/common/components';
import { Sup03000Component } from './sup03000/sup03000.component';
import { PipesModule } from 'app/baiwa/common/pipes/pipes.module';
import { Sup03100Component } from './sup03100/sup03100.component';
import { StoreModule } from '@ngrx/store';
import { emailReducer } from 'app/tmb-ecert/sup00000/sup03000/sup03000.reducer';
import { roleReducer } from 'app/tmb-ecert/sup00000/sup01000/sup01000.reducer';
import { NgxSummernoteModule } from 'ngx-summernote';
import { DatatableModule } from 'app/baiwa/common/directives';

@NgModule({
  imports: [
    CommonModule,
    Sup00000RoutingModule
    ,ReactiveFormsModule
    ,FormsModule
    ,ModalModule
    ,DropdownModule
    ,PipesModule
    ,NgxSummernoteModule
    ,DatatableModule
    ,StoreModule.forFeature("sup00000" ,{ sup01000:roleReducer,sup03000: emailReducer}),
  ],
  declarations: [Sup01000Component, sup01100Component, Sup02000Component, Sup03000Component, Sup03100Component]
})
export class Sup00000Module { }
