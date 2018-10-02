import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Store } from '@ngrx/store';

import { Nrq02000Service } from './nrq02000.service';
import { Certificate } from 'tmb-ecert/models';
import { Acc, isValid } from 'helpers/';
import { Observable } from 'rxjs';

declare var $: any;

@Component({
  selector: 'app-nrq02000',
  templateUrl: './nrq02000.component.html',
  styleUrls: ['./nrq02000.component.css'],
  providers: [Nrq02000Service]
})
export class Nrq02000Component implements OnInit {

  form: FormGroup;
  files: any;
  reqDate: string;
  dropdownObj: any;
  isDownload: Observable<boolean>;

  reqTypeChanged: Certificate[];

  constructor(
    private store: Store<{}>,
    private router: Router,
    private service: Nrq02000Service
  ) {
    this.reqTypeChanged = [];
    this.files = {
      requestFile: null,
      copyFile: null,
      changeNameFile: null
    };
  }

  ngOnInit() {
    // Initial Data
    this.isDownload = this.service.isDownload();
    this.reqDate = this.service.getReqDate();
    this.dropdownObj = this.service.getDropdownObj();
    this.service.getForm().subscribe(form => {
      this.form = form
    });
  }

  formSubmit(form: FormGroup) {
    this.service.save(form, this.files, this.reqTypeChanged);
  }

  pdf() {
    this.service.pdf();
  }

  cancel() {
    this.service.cancel();
  }

  reqTypeChange(e) {
    this.service.reqTypeChange(e).then(certificate => {
      this.reqTypeChanged = certificate;
      this.reqTypeChanged.forEach((obj, index) => {2
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

  accNoPress(e) {
    return e.charCode >= 48 && e.charCode <= 57;
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

  changeUpload(control: string, data: any) {
    this.files[control] = data.target.files[0];
  }

  accNoBlur(): void {
    const { accNo } = this.form.controls;
    this.form.controls.accNo.setValidators([Validators.required, Validators.maxLength(13)]);
    this.form.controls.accNo.setValue(Acc.convertAccNo(accNo.value));
  }

  accNoFocus(): void {
    const { accNo } = this.form.controls;
    this.form.controls.accNo.setValue(Acc.revertAccNo(accNo.value));
  }

  validate(input: string, submitted: boolean) {
    return isValid(this.form, input, submitted);
  }

  validateChk() { // is invalid checkbox
    let has: boolean = true;
    this.reqTypeChanged.forEach((obj, index) => {
      if (index != 0) {
        if (this.form.controls['chk' + index].valid) {
          has = false;
        } else if (!has && this.form.controls['chk' + index].invalid) {
          this.form.get('chk' + index).clearValidators();
          this.form.get('chk' + index).updateValueAndValidity();
        }
      }
    });
    return has;
  }

}
