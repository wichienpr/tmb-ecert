import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { BaselayoutRoutingModule } from './baselayout-routing.module';
import { SemanticBodyComponent } from './semantic-body/semantic-body.component';
import { SemanticMenuComponent } from './semantic-menu/semantic-menu.component';
import { SemanticFooterComponent } from './semantic-footer/semantic-footer.component';
import { ModalModule } from 'components/modal/modal.module';

@NgModule({
  imports: [
    CommonModule,
    BaselayoutRoutingModule,
    // Components
    ModalModule
  ],
  declarations: [SemanticBodyComponent, SemanticMenuComponent, SemanticFooterComponent]
})
export class BaselayoutModule { }
