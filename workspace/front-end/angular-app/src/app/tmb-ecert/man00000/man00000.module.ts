import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Man00000RoutingModule } from './man00000-routing.module';
import { Man01000Component } from './man01000/man01000.component';
import { Man02000Component } from './man02000/man02000.component';
import { BrowserModule } from '@angular/platform-browser';
import { VgCoreModule } from 'videogular2/core';
import { VgControlsModule } from 'videogular2/controls';
import { VgOverlayPlayModule } from 'videogular2/overlay-play';
import { VgBufferingModule } from 'videogular2/buffering';


@NgModule({
  
  imports: [
    CommonModule,
    Man00000RoutingModule,
    //  vedio
    VgCoreModule,
    VgControlsModule,
    VgOverlayPlayModule,
    VgBufferingModule,
  ],
  declarations: [ Man01000Component, Man02000Component],
})
export class Man00000Module { }
