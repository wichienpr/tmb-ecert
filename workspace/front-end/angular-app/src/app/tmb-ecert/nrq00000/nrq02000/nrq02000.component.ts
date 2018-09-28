import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';

import { Nrq02000Service } from './nrq02000.service';
import { Certificate, Lov } from 'tmb-ecert/models';
import { DropdownService } from 'services/';

declare var $: any;

@Component({
  selector: 'app-nrq02000',
  templateUrl: './nrq02000.component.html',
  styleUrls: ['./nrq02000.component.css'],
  providers: [Nrq02000Service]
})
export class Nrq02000Component implements OnInit {

  @ViewChild("form") form: NgForm;

  reqType: Observable<Lov[]>;
  customSeg: Observable<Lov[]>;
  payMethod: Observable<Lov[]>;
  subAccMethod: Observable<Lov[]>;

  reqTypeChanged: Certificate[];

  reqDate: string;

  constructor(
    private store: Store<{}>,
    private router: Router,
    private dropdown: DropdownService,
    private service: Nrq02000Service
  ) {
    this.reqTypeChanged = [];
  }

  ngOnInit() {
    // Current Date
    this.reqDate = this.service.getReqDate();
    // Dropdowns
    this.reqType = this.dropdown.getReqType();
    this.customSeg = this.dropdown.getCustomSeg();
    this.payMethod = this.dropdown.getpayMethod();
    this.subAccMethod = this.dropdown.getsubAccMethod();
    // Certificate
    this.store.select('certificate').subscribe(result => this.reqTypeChanged = result );
  }

  onSubmit(form: NgForm) {
    this.service.certificateUpdate(this.reqTypeChanged);
  }

  send() {
    $('#send-req').modal('show');
  }

  redirect() {
    $('#send-req').modal('hide');
    this.router.navigate(['performa']);
  }

  reqTypeChange(e) {
    this.service.reqTypeChange(e);
  }

  customSegChange(e) {
    console.log('customSegChange => ', e);
  }

  payMethodChange(e) {
    console.log('payMethodChange => ', e);
  }

  subAccMethodChange(e) {
    console.log('subAccMethodChange => ', e);
  }

}
