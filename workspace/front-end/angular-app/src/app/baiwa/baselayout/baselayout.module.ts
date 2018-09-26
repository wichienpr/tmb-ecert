import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { BaselayoutRoutingModule } from './baselayout-routing.module';
import { SemanticBodyComponent } from './semantic-body/semantic-body.component';
import { SemanticMenuComponent } from './semantic-menu/semantic-menu.component';
import { SemanticFooterComponent } from './semantic-footer/semantic-footer.component';

@NgModule({
  imports: [
    CommonModule,
    BaselayoutRoutingModule,
  ],
  declarations: [SemanticBodyComponent, SemanticMenuComponent, SemanticFooterComponent]
})
export class BaselayoutModule { }
