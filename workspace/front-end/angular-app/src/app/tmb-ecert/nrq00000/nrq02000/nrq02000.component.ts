import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { NgForm, FormGroup, FormControl, Validators } from '@angular/forms';
import { Store } from '@ngrx/store';

import { Nrq02000Service } from './nrq02000.service';
import { Certificate, Lov } from 'tmb-ecert/models';
import { DropdownService } from 'services/';
import { convertAccNo, revertAccNo } from 'app/baiwa/common/helpers';

declare var $: any;

@Component({
  selector: 'app-nrq02000',
  templateUrl: './nrq02000.component.html',
  styleUrls: ['./nrq02000.component.css'],
  providers: [Nrq02000Service]
})
export class Nrq02000Component implements OnInit {

  form: FormGroup = new FormGroup({
    reqTyped: new FormControl('', Validators.required),

    accNo: new FormControl('', Validators.required),
  });

  reqDate: string;
  dropdownObj: any;

  reqTypeChanged: Certificate[];

  constructor(
    private store: Store<{}>,
    private router: Router,
    private service: Nrq02000Service
  ) {
    this.reqTypeChanged = [];
  }

  ngOnInit() {
    // Current Date
    this.reqDate = this.service.getReqDate();
    this.dropdownObj = this.service.getDropdownObj();

    // Certificate
    this.store.select('certificate').subscribe(result => this.reqTypeChanged = result);
  }

  onSubmit() {
    let form = new NgForm([],[]);
    console.log(this.form);
    this.service.save(form, this.reqTypeChanged);
  }

  send() {
    this.service.send();
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

  toggleChk(index: number) {
    this.reqTypeChanged[index].value = null;
  }

  accNoBlur(): void {
    const { accNo } = this.form.controls;
    this.form.controls.accNo.setValidators([Validators.required, Validators.maxLength(13)]);
    this.form.controls.accNo.setValue(convertAccNo(accNo.value));
  }

  accNoFocus(): void {
    const { accNo } = this.form.controls;
    this.form.controls.accNo.setValue(revertAccNo(accNo.value));
  }

}
