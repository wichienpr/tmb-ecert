import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { NgForm, FormGroup, FormControl, Validators } from '@angular/forms';
import { Store } from '@ngrx/store';

import { Nrq02000Service } from './nrq02000.service';
import { Certificate } from 'tmb-ecert/models';
import { convertAccNo, revertAccNo } from 'app/baiwa/common/helpers';

declare var $: any;

@Component({
  selector: 'app-nrq02000',
  templateUrl: './nrq02000.component.html',
  styleUrls: ['./nrq02000.component.css'],
  providers: [Nrq02000Service]
})
export class Nrq02000Component implements OnInit {

  form: FormGroup;
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
    // Initial Data
    this.reqDate = this.service.getReqDate();
    this.dropdownObj = this.service.getDropdownObj();
    this.service.getForm().subscribe(form => {
      this.form = form
    });
  }

  onSubmit() {
    let form = new NgForm([], []);
    this.service.save(form, this.reqTypeChanged);
  }

  send() {
    this.service.send();
  }

  reqTypeChange(e) {
    this.service.reqTypeChange(e).then(certificate => {
      this.reqTypeChanged = certificate;
      this.reqTypeChanged.forEach((obj, index) => {
        if (index != 0) {
          if (this.form.controls[`chk${index}`]) {
            this.form.setControl(`chk${index}`, new FormControl('', Validators.required));
            this.form.setControl(`cer${index}`, new FormControl({ value: '', disabled: true }, Validators.required));
          } else {
            this.form.addControl(`chk${index}`, new FormControl('', Validators.required));
            this.form.addControl(`cer${index}`, new FormControl({ value: '', disabled: true }, Validators.required));
          }
        }
      })
    });
  }

  toggleChk(index) {
    this.form.controls[`cer${index}`].setValue('');
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
