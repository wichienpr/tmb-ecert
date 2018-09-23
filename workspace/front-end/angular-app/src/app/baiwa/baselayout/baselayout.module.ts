import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { BaselayoutRoutingModule } from './baselayout-routing.module';
import { SemanticBodyComponent } from './semantic-body/semantic-body.component';

@NgModule({
  imports: [
    CommonModule,
    BaselayoutRoutingModule
  ],
  declarations: [SemanticBodyComponent]
})
export class BaselayoutModule { }
